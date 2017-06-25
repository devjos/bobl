package de.tum.resource;

import java.util.Collection;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import de.tum.model.DatabaseService;
import de.tum.model.Demand;
import de.tum.model.DemandWrapper;

/**
 * Demand resource.
 */
@Path("demand")
public class DemandResource {

  private static final Logger log = LogManager.getLogger();

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
    try {
      d.verify();
    } catch (IllegalStateException e) {
      log.debug("Invalid demand received.", e);
      throw new WebApplicationException(Status.BAD_REQUEST);
    }
    db.addDemand(user, d);

  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getDemands(@Context SecurityContext sc) {
    String user = sc.getUserPrincipal().getName();
    Collection<Demand> demands = db.getDemands(user);

    return new Gson().toJson(new DemandWrapper(demands));
  }


}
