package de.tum.model;

import java.util.Collection;

public class DemandWrapper {

  private Collection<Demand> demands;

  public DemandWrapper(Collection<Demand> demands) {
    this.demands = demands;
  }

  public Collection<Demand> getDemands() {
    return demands;
  }

}
