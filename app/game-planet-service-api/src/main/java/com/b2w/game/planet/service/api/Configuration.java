package com.b2w.game.planet.service.api;

import com.b2w.game.planet.service.api.resource.PlanetResource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.logging.LoggingFeature;

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
        properties.put(CommonProperties.JSON_PROCESSING_FEATURE_DISABLE, true);
        properties.put(CommonProperties.MOXY_JSON_FEATURE_DISABLE, true);
        properties.put(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL, "DEBUG");
        properties.put(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "DEBUG");
        properties.put(LoggingFeature.LOGGING_FEATURE_VERBOSITY, LoggingFeature.Verbosity.PAYLOAD_ANY);
        properties.put(LoggingFeature.LOGGING_FEATURE_VERBOSITY_SERVER, LoggingFeature.Verbosity.PAYLOAD_ANY);
        properties.put(CommonProperties.METAINF_SERVICES_LOOKUP_DISABLE_SERVER, true);
        return properties;
    }
}
