package de.tum.model;

import java.util.Collection;

public interface DatabaseService {

  Credentials newUser();

  SessionToken newSession(Credentials creds);

  void addDemand(Demand demand);

  Collection<Demand> getDemands(String userID);

}
