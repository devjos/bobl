package de.tum.resource;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.security.auth.login.FailedLoginException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import de.tum.model.Credentials;
import de.tum.model.DatabaseService;
import de.tum.model.SessionToken;

/**
 * Login resource.
 */
@Path("login")
@PermitAll
public class LoginResource {
  private static final Logger log = LogManager.getLogger();

  @Inject
  private DatabaseService db;

  /**
   * Return a new session token.
   *
   * @return session id
   */
  @POST
  public Response login(String content) {
    log.debug("Received login request");
    if (content == null || content.isEmpty()) {
      throw new WebApplicationException(Status.BAD_REQUEST);
    }

    Credentials creds = new Gson().fromJson(content, Credentials.class);
    if (!creds.isComplete()) {
      throw new WebApplicationException(Status.BAD_REQUEST);
    }

    try {
      SessionToken token = db.newSession(creds);
      String cookie = Base64.encodeBase64String(new Gson().toJson(token).getBytes());

      NewCookie c = new NewCookie("session", cookie);
      return Response.ok().cookie(c).build();
    } catch (FailedLoginException e) {
      log.debug("Login failed.", e);
      throw new WebApplicationException(Status.UNAUTHORIZED);
    }

  }
}
