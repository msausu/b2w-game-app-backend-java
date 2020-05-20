package com.b2w.game.external.swapi.queue;

import com.b2w.game.external.swapi.model.StarWarsResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;

/**
 *
 * @author msa
 * @see   https://swapi.dev/
 */
class SWAPI {

    private final String SWAPI_URI = "https://swapi.dev/api/planets/", URI_PARAM = "search";
    private final Client client = ClientBuilder.newClient().register(LoggingFilter.class).register(JacksonFeature.class);

    public StarWarsResponse getStarWarsPlanets(String name) {
        return client
                .target(SWAPI_URI)
                .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.TRUE)
                .queryParam(URI_PARAM, name)
                .request(MediaType.APPLICATION_JSON)
                .get(StarWarsResponse.class);
    }
}
