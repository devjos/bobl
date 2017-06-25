package de.tum.resource;


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

import de.tum.Main;
import de.tum.model.Credentials;
import de.tum.model.DatabaseService;
import de.tum.model.Demand;
import de.tum.model.SessionToken;
import de.tum.services.MemoryDatabaseService;

public class DemandResourceTest {

  private HttpServer server;
  private WebTarget target;
  private DatabaseService db;

  @Before
  public void setUp() throws Exception {
    db = new MemoryDatabaseService();
    server = Main.startServer(db);
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
  public void ok() throws Exception {
    Credentials creds = db.newUser();
    SessionToken token = db.login(creds);

    byte[] weekdays = {0, 3, 5};
    Demand d = new Demand("Title", "source", "3.4567", "5.6737", "destination", "5.678", "1.2345",
        "12:30", weekdays);

    Response response = target.path("demand").request().cookie(token.toCookie())
        .post(Entity.json(new Gson().toJson(d)));
    assertEquals(204, response.getStatus());

  }

  @Test
  public void noAuthentication() throws Exception {
    byte[] weekdays = {0, 3, 5};
    Demand d = new Demand("Title", "source", "3.4567", "5.6737", "destination", "5.678", "1.2345",
        "12:30", weekdays);


    Response response = target.path("demand").request().post(Entity.json(new Gson().toJson(d)));
    assertEquals(403, response.getStatus());
  }

  @Test
  public void emptyRequest() throws Exception {
    Credentials creds = db.newUser();
    SessionToken token = db.login(creds);

    Response response = target.path("demand").request().cookie(token.toCookie()).post(null);
    assertEquals(400, response.getStatus());
  }
}
