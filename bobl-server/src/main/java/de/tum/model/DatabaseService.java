package de.tum.model;

import java.util.Collection;

import javax.security.auth.login.FailedLoginException;

public interface DatabaseService {

  Credentials newUser();

  /**
   * Authenticates the user and generates a new session token.
   * 
   * @param creds
   * @return
   * @throws FailedLoginException
   */
  SessionToken login(Credentials creds) throws FailedLoginException;

  void addDemand(String userID, Demand demand);

  Collection<Demand> getDemands(String userID);

  void verifySession(SessionToken token) throws FailedLoginException;

  Demand getDemand(String demand_id);

  void deleteDemand(int demand_id, String userID);

}
