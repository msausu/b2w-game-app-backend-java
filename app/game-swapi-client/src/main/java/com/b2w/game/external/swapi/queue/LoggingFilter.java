
package com.b2w.game.external.swapi.queue;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

/**
 *
 * @author msa
 */
class LoggingFilter implements ClientRequestFilter {
    private static final Logger LOG = Logger.getLogger(LoggingFilter.class.getName());

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        if (requestContext != null && requestContext.hasEntity()) LOG.log(Level.ALL, requestContext.getEntity().toString());
    }

}