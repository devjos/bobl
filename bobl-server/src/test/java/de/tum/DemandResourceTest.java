package de.tum;


import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import de.tum.model.Demand;
import de.tum.services.MySQLDatabaseService;

public class DemandResourceTest {

  private HttpServer server;
  private WebTarget target;

  @Before
  public void setUp() throws Exception {
    server = Main.startServer(new MySQLDatabaseService(false));
    Client c = ClientBuilder.newClient();

    // uncomment the following line if you want to enable
    // support for JSON in the client (you also have to uncomment
    // dependency on jersey-media-json module in pom.xml and Main.startServer())
    // --
    // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

    target = c.target(Main.BASE_URI);
  }

  @After
  public void tearDown() throws Exception {
    server.stop();
  }


  @Test
  public void test() {
    Demand d = new Demand();

    Response response = target.path("demand").request().post(Entity.json(new Gson().toJson(d)));
    assertEquals(204, response.getStatus());

  }
}
