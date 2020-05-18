package com.b2w.model.api.resources;

import com.b2w.game.planet.dao.PlanetDao;
import com.b2w.game.planet.model.Planet;
import com.b2w.game.planet.model.PlanetWFilm;
import com.b2w.game.planet.service.api.resource.PlanetResource;
import java.util.List;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status.*;
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

    @Test
    @Timeout(value = 10000L * PlanetDao.MAX_RESULTS) // subject to cache
    public void listaTest() {
        Response res = planet.lista(null);
        assertEquals(OK.getStatusCode(), res.getStatus());
        List<PlanetWFilm> list = res.readEntity(new GenericType<List<PlanetWFilm>>(){});
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
    public void byMissingIdTest() {
        String id = "" + Math.abs(new java.util.Random().nextLong());
        assertEquals(NOT_FOUND.getStatusCode(), planet.id(id).getStatus());
    }
    
    @Test
    public void byInvalidIdTest() {
        assertEquals(BAD_REQUEST.getStatusCode(), planet.id("%").getStatus());
    }
    
    @Test
    public void byMissingNameTest() {
        assertEquals(NOT_FOUND.getStatusCode(), planet.lista("X").getStatus());
    }
    
    @Test
    public void byNomeTest() {
        Response res = planet.lista("Coruscant");
        PlanetWFilm p = (PlanetWFilm)res.readEntity(PlanetWFilm.class);
        assertEquals(OK.getStatusCode(), res.getStatus());
        assertNotNull(p);
        assertEquals("temperado", p.getClima());
    }
    
    @Test
    public void excluiTest() {
        String id = "11002"; // Champala 
        Response res1 = planet.exclui(id), res2 = planet.id(id);
        assertEquals(NO_CONTENT.getStatusCode(), res1.getStatus());
        assertEquals(NOT_FOUND.getStatusCode(), res2.getStatus());
    }
}
