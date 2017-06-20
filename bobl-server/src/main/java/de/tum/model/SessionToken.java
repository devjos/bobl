package de.tum.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class SessionToken {

  // do not modify, object is parsed as JSON
  private String token;
  private String expirationDate;

  public SessionToken(String token) {
    this.token = Objects.requireNonNull(token);
    this.expirationDate = LocalDateTime.now().plusDays(30).toString();
  }

  public String getToken() {
    return token;
  }

  public String getExpirationDate() {
    return expirationDate;
  }



}
