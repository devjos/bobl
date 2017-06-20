package de.tum;


import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import de.tum.model.Credentials;
import de.tum.model.SessionToken;
import de.tum.services.MySQLDatabaseService;

public class LoginResourceTest {

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

  /**
   * Test to see that the message "Got it!" is sent in the response.
   */
  @Test
  public void createNewSessionID() {
    Credentials creds = new Credentials("user", "password");
    Response response = target.path("login").request()
        .post(Entity.entity(new Gson().toJson(creds), MediaType.APPLICATION_JSON));
    String entity = response.readEntity(String.class);
    SessionToken token = new Gson().fromJson(entity, SessionToken.class);
    assertTrue(token.getToken().matches("[a-zA-Z0-9]+"));
  }
}
