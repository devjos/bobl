package de.tum.resource;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import de.tum.model.Credentials;
import de.tum.model.DatabaseService;

/**
 * Login resource.
 */
@Path("login")
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
  @Produces(MediaType.APPLICATION_JSON)
  public String getIt(String content) {
    log.debug("Received login request");
    if (content == null || content.isEmpty()) {
      throw new WebApplicationException(Status.BAD_REQUEST);
    }

    Credentials creds = new Gson().fromJson(content, Credentials.class);
    if (!creds.isComplete()) {
      throw new WebApplicationException(Status.BAD_REQUEST);
    }

    return new Gson().toJson(db.newSession(creds));
  }
}
