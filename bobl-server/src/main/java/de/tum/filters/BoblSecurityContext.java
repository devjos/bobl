package de.tum.filters;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

public class BoblSecurityContext implements SecurityContext {

  private String user;
  private String scheme;

  public BoblSecurityContext(String user, String scheme) {
    this.user = user;
    this.scheme = scheme;
  }

  @Override
  public String getAuthenticationScheme() {
    return SecurityContext.BASIC_AUTH;
  }

  @Override
  public Principal getUserPrincipal() {
    return () -> user;
  }

  @Override
  public boolean isSecure() {
    return "https".equals(this.scheme);
  }

  @Override
  public boolean isUserInRole(String s) {
    // we don't have user roles at the moment
    return true;
  }

}
