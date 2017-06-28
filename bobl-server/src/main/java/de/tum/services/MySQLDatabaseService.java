package de.tum.services;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.security.auth.login.FailedLoginException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.model.Credentials;
import de.tum.model.DatabaseService;
import de.tum.model.Demand;
import de.tum.model.SessionToken;
import de.tum.util.Password;
import de.tum.util.RandomUtil;

public class MySQLDatabaseService implements DatabaseService, Closeable {

  private static final Logger log = LogManager.getLogger();
  private Connection conn;

  private final String NEW_USER = "INSERT INTO User (password) VALUES (?)";
  private final String SELECT_USER = "SELECT password FROM User WHERE user_id = ?";

  private final String NEW_TOKEN =
      "INSERT INTO Session (user_id, token, expiration_date) VALUES (?, ?, ?)";
  private final String SELECT_TOKEN =
      "SELECT expiration_date FROM Session WHERE user_id = ? AND token = ?";

  private final String NEW_DEMAND =
      "INSERT INTO Demand (user_id, title, source, sourceLatitude, sourceLongitude, "
          + "destination, destinationLatitude, destinationLongitude, outboundTime, "
          + "waybackTime, weekdays) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, b?)";
  private final String SELECT_DEMAND =
      "SELECT demand_id, title, source, sourceLatitude, sourceLongitude, "
          + "destination, destinationLatitude, destinationLongitude, outboundTime, "
          + "waybackTime, weekdays FROM Demand WHERE user_id = ?";


  public MySQLDatabaseService(String user, String password) throws IOException {

    try {
      this.conn = DriverManager.getConnection("jdbc:mysql://localhost/bobl", user, password);

    } catch (SQLException ex) {
      // handle any errors
      log.error("SQLException: " + ex.getMessage());
      log.error("SQLState: " + ex.getSQLState());
      log.error("VendorError: " + ex.getErrorCode());
      throw new IOException("Could not connect to DB", ex);
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
      // generate random password
      String password = RandomUtil.generatePassword();
      String saltedHash = Password.getSaltedHash(password);

      stmt = conn.prepareStatement(NEW_USER, Statement.RETURN_GENERATED_KEYS);
      stmt.setString(1, saltedHash);
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

    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement(SELECT_USER, Statement.RETURN_GENERATED_KEYS);
      stmt.setString(1, creds.getUser());
      ResultSet set = stmt.executeQuery();
      if (set.next()) {
        String storedSaltedHash = set.getString(1);

        if (!Password.check(creds.getPassword(), storedSaltedHash)) {
          throw new FailedLoginException("Incorrect password.");
        }

        close(stmt);

        SessionToken sessionToken = new SessionToken(creds.getUser());

        stmt = conn.prepareStatement(NEW_TOKEN);
        stmt.setString(1, sessionToken.getUser());
        stmt.setString(2, sessionToken.getToken());
        stmt.setString(3, sessionToken.getExpirationDate());

        stmt.execute();

        return sessionToken;

      } else {
        log.error("Did not get user_id back.");
      }

    } catch (SQLException e) {
      log.error("Cannot add new user.", e);
      throw new FailedLoginException("SQL Exception");
    } finally {
      close(stmt);
    }

    throw new FailedLoginException("Unknwon error.");
  }



  @Override
  public void addDemand(String userID, Demand demand) {
    PreparedStatement stmt = null;
    try {

      stmt = conn.prepareStatement(NEW_DEMAND);
      stmt.setInt(1, Integer.parseInt(userID));
      stmt.setString(2, demand.getTitle());
      stmt.setString(3, demand.getSource());
      stmt.setBigDecimal(4, new BigDecimal(demand.getSourceLatitude()));
      stmt.setBigDecimal(5, new BigDecimal(demand.getSourceLongitude()));
      stmt.setString(6, demand.getDestination());
      stmt.setBigDecimal(7, new BigDecimal(demand.getDestinationLatitude()));
      stmt.setBigDecimal(8, new BigDecimal(demand.getDestinationLongitude()));
      stmt.setString(9, demand.getOutboundTime());
      stmt.setString(10, demand.getWaybackTime());

      StringBuilder builder = new StringBuilder();
      byte[] weekdays = demand.getWeekdays();
      for (int i = 0; i < weekdays.length; i++) {
        builder = builder.append(weekdays[i] == 1 ? "1" : "0");
      }
      stmt.setString(11, builder.toString());

      stmt.executeUpdate();

    } catch (SQLException e) {
      log.error("Cannot retrieve token.", e);
    } finally {
      close(stmt);
    }

  }



  @Override
  public Collection<Demand> getDemands(String userID) {
    List<Demand> demandList = null;
    PreparedStatement stmt = null;
    try {

      stmt = conn.prepareStatement(SELECT_DEMAND);
      stmt.setString(1, userID);

      ResultSet results = stmt.executeQuery();
      demandList = new ArrayList<>();
      while (results.next()) {
        String title = results.getString(2);
        String source = results.getString(3);
        String sourceLatitude = results.getString(4);
        String sourceLongitude = results.getString(5);
        String destination = results.getString(6);
        String destinationLatitude = results.getString(7);
        String destinationLongitude = results.getString(8);
        String outboundTime = results.getString(9);
        String waybackTime = results.getString(10);

        int weekdaysInt = results.getInt(11);
        // parse weekdays
        byte[] weekdays = new byte[7];
        for (byte i = 0; i < weekdays.length; i++) {
          weekdays[i] = getBit(weekdaysInt, 6 - i);
        }

        Demand d = new Demand(title, source, sourceLatitude, sourceLongitude, destination,
            destinationLatitude, destinationLongitude, outboundTime, waybackTime, weekdays);
        demandList.add(d);
      }



    } catch (SQLException e) {
      log.error("Cannot retrieve token.", e);
    } finally {
      close(stmt);
    }
    return demandList;
  }

  private byte getBit(int number, int bit) {
    return (byte) ((number >> bit) & 1);
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

    PreparedStatement stmt = null;
    try {

      stmt = conn.prepareStatement(SELECT_TOKEN);
      stmt.setString(1, token.getUser());
      stmt.setString(2, token.getToken());

      ResultSet set = stmt.executeQuery();
      if (set.next()) {
        String date = set.getString(1);
        date = date.replaceFirst(" ", "T");
        LocalDateTime time = LocalDateTime.parse(date);

        if (time.isBefore(LocalDateTime.now())) {
          throw new FailedLoginException("Token has expired.");
        }

        // success

      } else {
        throw new FailedLoginException("Did not get user_id back.");
      }

    } catch (SQLException e) {
      log.error("Cannot retrieve token.", e);
      throw new FailedLoginException("SQLException");
    } finally {
      close(stmt);
    }

  }

}
