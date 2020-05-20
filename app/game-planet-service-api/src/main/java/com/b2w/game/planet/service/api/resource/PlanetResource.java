package com.b2w.game.planet.service.api.resource;

import com.b2w.game.planet.dao.PlanetDao;
import com.b2w.game.planet.dao.PlanetWFilmDao;
import com.b2w.game.planet.model.Planet;
import com.b2w.game.planet.model.PlanetWFilm;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import static javax.ws.rs.core.Response.Status.*;

/**
 * Since the spec is brief I assume it's OK to choose the protocol: I favor
 * simplicity and security (not informing the client concerning back-end
 * errors/availability that's another API).
 *
 * @author msa
 *
 */
@Path("planeta")
public class PlanetResource {

    @Context
    @NotNull
    private UriInfo base;

    @Inject
    @NotNull
    private PlanetWFilmDao dao;

    private Response one(PlanetWFilm planet) {
        return planet == null ? Response.status(NOT_FOUND).build() : Response.ok(planet).build();
    }

    private Response all() {
        List<PlanetWFilm> list = dao.listw();
        return Response
                .ok(new GenericEntity<List<PlanetWFilm>>(list) {
                })
                .status(list.size() == PlanetDao.MAX_RESULTS ? REQUEST_ENTITY_TOO_LARGE : OK)
                .build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response lista(@QueryParam("nome") @Size(min = 4, max = 60) @Pattern(regexp = Planet.NM_PAT) String nome) {
        return nome != null ? one(dao.readw(nome)) : all();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response id(@NotNull @PathParam("id") @Size(min = 1, max = 30) @Pattern(regexp = Planet.ID_PAT) String id) {
        return one(dao.idw(id));
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response novo(@NotNull @Valid Planet planet) {
        if (dao.create(planet)) {
            URI resource = null;
            try {
                Planet chk = dao.read(PlanetDao.QryType.NOME, planet);
                if (chk != null) {
                    resource = new URI(base.getBaseUri() + "/" + chk.getId());
                }
            } catch (URISyntaxException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "resource creating error {0}", e.getMessage());
            }
            return resource == null ? Response.status(CREATED).build() : Response.created(resource).build();
        }
        // let's assume the db is ok and the resource doesn't exist 
        return Response.status(CONFLICT).build();
    }

    @DELETE
    @Path("{id}")
    public Response exclui(@NotNull @PathParam("id") @Size(min = 1, max = 30) @Pattern(regexp = Planet.ID_PAT) String id) {
        return Response.status(dao.remove(id) ? NO_CONTENT : CONFLICT).build();
    }
}
