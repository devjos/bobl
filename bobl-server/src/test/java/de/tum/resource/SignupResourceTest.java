package de.tum.resource;


import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import de.tum.Main;
import de.tum.model.Credentials;
import de.tum.model.DatabaseService;
import de.tum.services.MemoryDatabaseService;

public class SignupResourceTest {

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
  public void test() {
    String response = target.path("signup").request().post(null, String.class);

    Credentials creds = new Gson().fromJson(response, Credentials.class);
    assertNotNull(creds);
    assertNotNull(creds.getUser());
    assertNotNull(creds.getPassword());

  }
}
