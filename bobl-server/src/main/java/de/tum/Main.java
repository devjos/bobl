package de.tum;

import java.io.IOException;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import de.tum.filters.AuthFilter;
import de.tum.filters.CorsRequestFilter;
import de.tum.filters.LoggingFilter;
import de.tum.model.DatabaseService;
import de.tum.services.MySQLDatabaseService;

/**
 * Main class.
 *
 */
public class Main {

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   * 
   * @param class1
   * 
   * @return Grizzly HTTP server.
   * @throws IOException
   */
  public static HttpServer startServer() throws IOException {
    ServerConfig conf = ServerConfig.loadDefault();
    return startServer(conf, new MySQLDatabaseService(conf.getDbUser(), conf.getDbPassword()));
  }

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   * 
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer(ServerConfig conf, DatabaseService dbService) {
    // create a resource config that scans for JAX-RS resources and providers
    // in de.tum.in.net package
    final ResourceConfig rc = new ResourceConfig().register(new MyApplicationBinder(dbService))
        .packages(true, "de.tum.resource").register(LoggingFilter.class).register(AuthFilter.class)
        .register(CorsRequestFilter.class).register(RolesAllowedDynamicFeature.class);

    rc.property("jersey.config.server.provider.packages",
        "org.glassfish.jersey.filter;de.tum.filters.AuthFilter");
    rc.property("javax.ws.rs.container.ContainerRequestFilter",
        "org.glassfish.jersey.filter.LoggingFilter;de.tum.filters.AuthFilter");


    // create and start a new instance of grizzly http server
    // exposing the Jersey application at BASE_URI
    return GrizzlyHttpServerFactory.createHttpServer(conf.getURI(), rc);
  }

  /**
   * Main method.
   * 
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    final HttpServer server = startServer();
    System.out.println("Jersey app started with WADL available at " + "\nHit enter to stop it...");
    System.in.read();
    server.stop();
  }
}

