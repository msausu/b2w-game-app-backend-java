package com.b2w.game.planet.service.api.resource;

import com.b2w.game.planet.model.PlanetWFilm;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;

/**
 *
 * @author msa
 */
class PlanetClient {

    private final String URI;
    private final Client client = ClientBuilder.newClient().register(LoggingFilter.class).register(JacksonFeature.class);

    public PlanetClient(String URI) {
        try {
            this.URI = new URI(URI).toURL().toString();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public List<PlanetWFilm> getPlanetByName(String name) {
        return client
                .target(URI)
                .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.TRUE)
                .queryParam("nome", name)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<PlanetWFilm>>() {
                });
    }

    public List<PlanetWFilm> getPlanetById(String id) {
        try {
            return client
                    .target(new URI(URI + "/" + id).toURL().toString())
                    .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.TRUE)
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<PlanetWFilm>>() {
                    });
        } catch (URISyntaxException | MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public List<PlanetWFilm> getPlanetTodos() {
        return client
                .target(URI)
                .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.TRUE)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<PlanetWFilm>>() {
                });
    }
}
