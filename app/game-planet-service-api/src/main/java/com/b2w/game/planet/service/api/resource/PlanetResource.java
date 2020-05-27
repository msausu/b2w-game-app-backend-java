package com.b2w.game.planet.service.api.resource;

import com.b2w.game.planet.dao.PlanetDao;
import com.b2w.game.planet.dao.PlanetWFilmDao;
import com.b2w.game.planet.model.Planet;
import com.b2w.game.planet.model.PlanetWFilm;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
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
    
    /**
     * 500 Request failed Jackson issue for a single entity:
     * https://github.com/eclipse-ee4j/jersey/issues/3672
     * StandardWrapperValve[com.b2w.game.planet.service.api.Configuration]:
     * Servlet.service() for servlet
     * com.b2w.game.planet.service.api.Configuration threw exception
     * java.lang.IllegalArgumentException: Class
     * com.b2w.game.planet.model.PlanetWFilm not subtype of [simple type, class
     * java.util.concurrent.CompletableFuture<javax.ws.rs.core.Response>]
     */
    private Response one(PlanetDao.QryType type, String info) {
        List<PlanetWFilm> list = new ArrayList<>();
        PlanetWFilm planet = dao.read(type, info);
        if (planet != null) {
            list.add(planet);
        }
        // not a "plain entity", wrap "entity in list"
        return list.isEmpty() ? 
                Response.status(NOT_FOUND).build() : 
                Response.ok(new GenericEntity<List<PlanetWFilm>>(list) {}).status(OK).build();
    }

    private Response all() {
        List<PlanetWFilm> list = (List<PlanetWFilm>)dao.list();
        return Response
                .ok(new GenericEntity<List<PlanetWFilm>>(list) {
                })
                .status(list.size() == PlanetDao.MAX_RESULTS ? REQUEST_ENTITY_TOO_LARGE : OK)
                .build();
    }

    /**
     * @param nome if nome (name) is absent (null) returns all planets
     * @return HTTP Status 200/400/404 200 -> always a list
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public CompletionStage<Response> lista(@QueryParam("nome") @Size(min = 1, max = 60) @Pattern(regexp = Planet.NM_PAT) final String nome) {
        return CompletableFuture.supplyAsync(() -> nome != null ? one(PlanetDao.QryType.NOME, nome) : all());
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public CompletionStage<Response> id(@NotNull @PathParam("id") @Size(min = 1, max = 30) @Pattern(regexp = Planet.ID_PAT) String id) {
        return CompletableFuture.supplyAsync(() -> one(PlanetDao.QryType.ID, id));
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
        return Response.status(dao.remove(id) ? NO_CONTENT : GONE).build();
    }
}
