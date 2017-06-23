package de.tum.resource;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.google.gson.Gson;

import de.tum.model.DatabaseService;
import de.tum.model.Demand;

/**
 * Demand resource.
 */
@Path("demand")
public class DemandResource {

  @Inject
  private DatabaseService db;

  /**
   * Upload a new demand.
   */
  @POST
  @RolesAllowed("user")
  public void upload(@Context SecurityContext sc, String content) {
    if (content == null || content.isEmpty()) {
      throw new WebApplicationException(Status.BAD_REQUEST);
    }

    String user = sc.getUserPrincipal().getName();
    Demand d = new Gson().fromJson(content, Demand.class);
    db.addDemand(user, d);
  }


}
