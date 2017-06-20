package de.tum.model;

import java.util.Collection;

import javax.security.auth.login.FailedLoginException;

public interface DatabaseService {

  Credentials newUser();

  SessionToken newSession(Credentials creds) throws FailedLoginException;

  void addDemand(Demand demand);

  Collection<Demand> getDemands(String userID);

  void authenticate(Credentials creds) throws FailedLoginException;

  void verifySession(SessionToken token) throws FailedLoginException;

}
