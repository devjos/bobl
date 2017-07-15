package de.tum.filters;

import java.io.IOException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.security.auth.login.FailedLoginException;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.model.DatabaseService;
import de.tum.model.SessionToken;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

  private static final Logger log = LogManager.getLogger();
  public static final String AUTHENTICATION_HEADER = "Authorization";

  @Inject
  private DatabaseService db;

  @Override
  public void filter(ContainerRequestContext request) throws IOException {

    String method = request.getMethod();
    String path = request.getUriInfo().getPath(true);

    if (method.equals("GET")
        && (path.equals("application.wadl") || path.equals("application.wadl/xsd0.xsd"))) {
      return;
    }

    Cookie c = request.getCookies().get("session");
    if (c != null) {
      SessionToken token = SessionToken.fromCookie(c);

      try {
        db.verifySession(token);
        String scheme = request.getUriInfo().getRequestUri().getScheme();
        request.setSecurityContext(new BoblSecurityContext(token.getUser(), scheme));
        log.debug("{} successfully authenticated", token.getUser());
      } catch (FailedLoginException e) {
        log.debug("Failed authentication.", e);
        throw new WebApplicationException(Status.UNAUTHORIZED);
      }

    } else {
      log.debug("No session cookie provided");
    }

  }

}
