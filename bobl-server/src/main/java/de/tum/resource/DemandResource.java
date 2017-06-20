package de.tum.resource;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

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
  public void upload(String content) {
    Demand d = new Gson().fromJson(content, Demand.class);
    db.addDemand(d);
  }


}
