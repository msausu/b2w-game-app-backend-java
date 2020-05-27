package com.b2w.game.planet.service.api.resource;

import com.b2w.game.planet.dao.PlanetDao;
import com.b2w.game.planet.model.Planet;
import com.b2w.game.planet.model.PlanetWFilm;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status.*;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Timeout;
import org.microshed.testing.jaxrs.RESTClient;
import org.microshed.testing.jupiter.MicroShedTest;
import org.microshed.testing.testcontainers.ApplicationContainer;
import org.testcontainers.junit.jupiter.Container;

/**
 *
 * @author msa
 */
@MicroShedTest
public class PlanetApiIT {

    @Container
    public static ApplicationContainer app = new ApplicationContainer().withAppContextRoot("/");

    @RESTClient
    public static PlanetResource planet;
    
    // async services
    private PlanetClient asyncPlanet = new PlanetClient("http://localhost:8080/api/planeta");
    
    @Test
    @Timeout(value = 10L * PlanetDao.MAX_RESULTS)
    public void listaTest() throws InterruptedException, ExecutionException {
        List<PlanetWFilm> list = asyncPlanet.getPlanetTodos();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }
    
    @Test
    public void novoValidTest() {
        Response res = planet.novo(new Planet("Tatooine", "quente", "desertos"));
        assertEquals(CREATED.getStatusCode(), res.getStatus());
    }
    
    @Test
    public void novoInvalidContentTest() {
        Response res = planet.novo(new Planet("", "frio", "polar"));
        assertEquals(BAD_REQUEST.getStatusCode(), res.getStatus());
    }
    
    @Test
    public void novoValidExistingTest() {
        Response res = planet.novo(new Planet("Coruscant", "temperado", "mountanhas"));
        assertEquals(CONFLICT.getStatusCode(), res.getStatus());
    }
    
    @Test
    public void byMissingIdTest() throws InterruptedException, ExecutionException, URISyntaxException, MalformedURLException {
        String id = "" + Math.abs(new java.util.Random().nextLong());
        assertThrows(javax.ws.rs.NotFoundException.class, () -> asyncPlanet.getPlanetById(id));
    }
    
    @Test
    public void byInvalidIdTest() {
        assertThrows(javax.ws.rs.BadRequestException.class, () -> asyncPlanet.getPlanetById("%25"));
    }
    
    @Test
    public void byInvalidNomeTest() {
        assertThrows(javax.ws.rs.BadRequestException.class, () -> asyncPlanet.getPlanetByName("%25"));
    }

    @Test
    public void byMissingNomeTest() {
        String nome = "XX_" + Math.abs(new java.util.Random().nextInt());
        assertThrows(javax.ws.rs.NotFoundException.class, () -> asyncPlanet.getPlanetByName(nome));
    }
    
    @Test
    public void byNomeTest() throws InterruptedException, ExecutionException {
        List<PlanetWFilm> p = (List<PlanetWFilm>) asyncPlanet.getPlanetByName("Coruscant");
        assertNotNull(p);
        assertFalse(p.isEmpty());
        assertEquals("temperado", p.get(0).getClima());
    }
    
    @Test
    public void excluiTest() {
        String id = "11002"; // Champala 
        Response res1 = planet.exclui(id);
        assertEquals(NO_CONTENT.getStatusCode(), res1.getStatus());
        assertThrows(javax.ws.rs.NotFoundException.class, () -> asyncPlanet.getPlanetById(id));
    }
}
