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

    if (method.equals("OPTIONS")) {
      log.debug("OPTIONS, no auth");
      return;
    }
    if (method.equals("POST") && ((path.equals("signup") || path.equals("login")))) {
      log.debug("Signup and login, will skip authentication.");
      return;
    }

    String c = request.getHeaderString("bobl-cookie");
    if (c != null && !c.isEmpty()) {
      SessionToken token = SessionToken.fromCookie(Cookie.valueOf(c));

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
      log.info("No session cookie provided. User is not authorized.");
      throw new WebApplicationException(Status.UNAUTHORIZED);
    }

  }

}
