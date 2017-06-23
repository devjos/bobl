package de.tum.services;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import javax.security.auth.login.FailedLoginException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.model.Credentials;
import de.tum.model.DatabaseService;
import de.tum.model.Demand;
import de.tum.model.SessionToken;

public class MySQLDatabaseService implements DatabaseService, Closeable {

  private static final Logger log = LogManager.getLogger();
  private static final SecureRandom random = new SecureRandom();
  private Connection conn;

  private final String NEW_USER = "INSERT INTO User (password) VALUES (?)";


  public MySQLDatabaseService() throws IOException {

    try {
      this.conn =
          DriverManager.getConnection("jdbc:mysql://localhost/test?user=minty&password=greatsqldb");

    } catch (SQLException ex) {
      // handle any errors
      log.error("SQLException: " + ex.getMessage());
      log.error("SQLState: " + ex.getSQLState());
      log.error("VendorError: " + ex.getErrorCode());
    }


  }



  @Override
  public void close() throws IOException {
    try {
      conn.close();
    } catch (SQLException e) {
      log.error("Could not close MySQL", e);
    }
  }

  @Override
  public Credentials newUser() {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement(NEW_USER, Statement.RETURN_GENERATED_KEYS);

      // generate random password
      String password = new BigInteger(130, random).toString(32);
      String digest = DigestUtils.sha256Hex(password);
      stmt.setString(1, digest);
      stmt.executeUpdate();
      ResultSet set = stmt.getGeneratedKeys();
      if (set.next()) {
        int user_id = set.getInt(1);
        return new Credentials(Integer.toString(user_id), password);
      } else {
        log.error("Did not get user_id back.");
      }

    } catch (SQLException e) {
      log.error("Cannot add new user.", e);
    } finally {
      close(stmt);
    }

    return null;
  }



  @Override
  public SessionToken login(Credentials creds) throws FailedLoginException {
    // TODO generate session token and save in DB
    return new SessionToken("user", "accessFooBar");
  }



  @Override
  public void addDemand(String userID, Demand demand) {
    // TODO Auto-generated method stub
  }



  @Override
  public Collection<Demand> getDemands(String userID) {
    // TODO Auto-generated method stub
    return null;
  }

  private void close(Statement stmt) {
    if (stmt != null)
      try {
        stmt.close();
      } catch (SQLException e) {
        log.error("Could not close stmt.", e);
      }
  }

  @Override
  public void verifySession(SessionToken token) throws FailedLoginException {
    // TODO Auto-generated method stub

  }

}
