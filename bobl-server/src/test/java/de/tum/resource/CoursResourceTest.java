package de.tum.resource;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.tum.Main;
import de.tum.ServerConfig;
import de.tum.model.DatabaseService;
import de.tum.services.MemoryDatabaseService;

public class CoursResourceTest {
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


  @Test
  public void simple() {

    Response r = target.request("signup").options();
    assertEquals(200, r.getStatus());

    r = target.request("login").options();
    assertEquals(200, r.getStatus());

    r = target.request("foo/bar/blub").options();
    assertEquals(200, r.getStatus());

  }
}
