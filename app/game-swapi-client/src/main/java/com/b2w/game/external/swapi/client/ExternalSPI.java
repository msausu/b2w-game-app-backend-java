package com.b2w.game.external.swapi.client;

import com.b2w.game.external.swapi.model.StarWarsResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;

/**
 *
 * @author msa
 */
class ExternalSPI {

    private final String SWAPI_URI = "https://swapi.dev/api/planets/", URI_PARAM = "search";
    private final Client client = ClientBuilder.newClient().register(LoggingFilter.class).register(JacksonFeature.class);

    public StarWarsResponse getStarWarsPlanets(String name) {
        StarWarsResponse res = null;
        // make this safe: endpoint/api my change
        try {
            res = client
                .target(SWAPI_URI)
                .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.TRUE)
                .queryParam(URI_PARAM, name)
                .request(MediaType.APPLICATION_JSON)
                .get(StarWarsResponse.class);
        } catch (Exception e) {
            Logger
                .getLogger(ExternalSPI.class.getName())
                .log(Level.INFO, "external swapi exception {0}", e.getMessage());
        }
        return res != null ? res : null;
    }
}
