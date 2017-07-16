package de.tum.resource;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import de.tum.Main;
import de.tum.ServerConfig;
import de.tum.model.Credentials;
import de.tum.model.DatabaseService;
import de.tum.model.SessionToken;
import de.tum.services.MemoryDatabaseService;

public class LoginResourceTest {

  private HttpServer server;
  private WebTarget target;
  private DatabaseService db;

  @Before
  public void setUp() throws Exception {
    db = new MemoryDatabaseService();
    ServerConfig conf = ServerConfig.loadDefault();
    server = Main.startServer(conf, db);
    Client c = ClientBuilder.newClient();

    // uncomment the following line if you want to enable
    // support for JSON in the client (you also have to uncomment
    // dependency on jersey-media-json module in pom.xml and Main.startServer())
    // --
    // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

    target = c.target(conf.getURI());
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
    Credentials creds = db.newUser();

    Response response = target.path("login").request()
        .post(Entity.entity(new Gson().toJson(creds), MediaType.APPLICATION_JSON));
    assertEquals(200, response.getStatus());

    Cookie c = Cookie.valueOf(response.getHeaderString("bobl-cookie"));
    assertNotNull(c);
    SessionToken token = SessionToken.fromCookie(c);
    assertTrue(token.getToken().matches("[a-zA-Z0-9]+"));
    assertEquals(creds.getUser(), token.getUser());

  }


  @Test
  public void emptyRequest() {
    Response response =
        target.path("login").request().post(Entity.entity(null, MediaType.APPLICATION_JSON));
    assertEquals(400, response.getStatus());
  }
}
