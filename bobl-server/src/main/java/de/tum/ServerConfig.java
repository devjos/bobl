package de.tum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import javax.ws.rs.core.UriBuilder;

public class ServerConfig {

  private static final File CONF_FILE = new File("conf", "server.properties");
  private static final String PORT = "port";
  private static final String DB_USER = "db.user";
  private static final String DB_PASSWORD = "db.password";

  private final int port;
  private final String dbUser;
  private final String dbPassword;

  public ServerConfig(int port, String dbUser, String dbPassword) {
    this.port = port;
    this.dbUser = dbUser;
    this.dbPassword = dbPassword;
  }

  public ServerConfig(Properties props) {
    this.port = Integer.parseInt(getNonEmptyProperty(props, PORT));
    this.dbUser = getNonEmptyProperty(props, DB_USER);
    this.dbPassword = props.getProperty(DB_PASSWORD, "");
  }


  public int getPort() {
    return this.port;
  }

  public String getDbUser() {
    return dbUser;
  }

  public String getDbPassword() {
    return dbPassword;
  }

  public URI getURI() {
    return UriBuilder.fromUri("http://0.0.0.0").port(port).build();
  }

  public static ServerConfig load(File confFile) throws IOException {
    Properties props = new Properties();
    try (InputStream in = new FileInputStream(confFile)) {
      props.load(in);
    }
    return new ServerConfig(props);
  }

  public static ServerConfig loadDefault() throws IOException {
    return load(CONF_FILE);
  }

  private static String getNonEmptyProperty(Properties props, String key) {
    String value = props.getProperty(key);
    if (value == null || value.isEmpty()) {
      throw new IllegalArgumentException("The property " + key + " does not exist or is empty.");
    }
    return value.trim();
  }

}
