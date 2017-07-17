package de.tum.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
@PreMatching
public class CorsRequestFilter implements ContainerRequestFilter {

  private final static Logger log = LogManager.getLogger();

  @Override
  public void filter(ContainerRequestContext requestCtx) throws IOException {
    log.info("Executing REST request filter");

    // When HttpMethod comes as OPTIONS, just acknowledge that it accepts...
    if (requestCtx.getRequest().getMethod().equals("OPTIONS")) {
      log.info("HTTP Method (OPTIONS) - Detected!");

      // Just send a OK signal back to the browser
      requestCtx.abortWith(Response.ok().build());
    }
  }
}
