package com.b2w.game.planet.service.api;

import com.b2w.game.planet.service.api.resource.PlanetResource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;

/**
 * @author msa
 */
@ApplicationPath("/api")
public class Configuration extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(JacksonFeature.class);
        classes.add(JacksonJaxbJsonProvider.class);
        classes.add(ObjectMapperResolver.class);
        classes.add(PlanetResource.class);
        return classes;
    }
    
        @Override
    public Map<String, Object> getProperties() {
        final Map<String, Object> properties = new HashMap<>();
        properties.put("jersey.config.server.disableJsonProcessing", true);
        properties.put("jersey.config.disableMoxyJson", true);
        properties.put("jersey.config.disableMetainfServicesLookup", true);
        properties.put("jersey.config.server.provider.classnames", "org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature");
        return properties;
    }
}
