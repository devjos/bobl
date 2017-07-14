package de.tum.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.FailedLoginException;

import de.tum.model.Credentials;
import de.tum.model.DatabaseService;
import de.tum.model.Demand;
import de.tum.model.SessionToken;
import de.tum.util.RandomUtil;

public class MemoryDatabaseService implements DatabaseService {

  private final Map<String, String> user = new HashMap<>();
  private final Map<String, SessionToken> sessions = new HashMap<>();
  private final Map<String, List<Demand>> demands = new HashMap<>();
  private int demand_id = 0;

  @Override
  public Credentials newUser() {
    Credentials creds = new Credentials(RandomUtil.generate(10), RandomUtil.generatePassword());
    user.put(creds.getUser(), creds.getPassword());
    return creds;
  }

  @Override
  public SessionToken login(Credentials creds) throws FailedLoginException {
    String pw = user.get(creds.getUser());
    if (pw == null) {
      throw new FailedLoginException("no such user");
    }

    if (creds.getPassword() == null || creds.getPassword().isEmpty()) {
      throw new FailedLoginException("empty pw");
    }

    if (!pw.equals(creds.getPassword())) {
      throw new FailedLoginException("wrong password");
    }

    SessionToken token = SessionToken.generate(creds.getUser());
    sessions.put(creds.getUser(), token);
    return token;
  }

  @Override
  public void addDemand(String userID, Demand demand) {
    List<Demand> demandList = demands.get(userID);
    if (demandList == null) {
      demandList = new ArrayList<>();
      demand.setID(demand_id++);
      demands.put(userID, demandList);
    }

    demandList.add(demand);
  }

  @Override
  public Collection<Demand> getDemands(String userID) {
    return demands.get(userID);
  }

  @Override
  public void verifySession(SessionToken token) throws FailedLoginException {
    SessionToken storedToken = sessions.get(token.getUser());

    if (!storedToken.getToken().equals(token.getToken())) {
      throw new FailedLoginException("Invalid token.");
    }

    if (storedToken.isExpired()) {
      throw new FailedLoginException("token expired.");
    }

  }

}
