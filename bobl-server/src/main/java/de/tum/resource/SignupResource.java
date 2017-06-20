package de.tum.resource;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import de.tum.model.DatabaseService;

/**
 * Session resource.
 */
@Path("signup")
public class SignupResource {

  @Inject
  private DatabaseService db;

  /**
   * Return a new user.
   *
   * @return user
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public String getAnalysis() {
    return new Gson().toJson(db.newUser());
  }

}
