package de.tum.model;

public class Credentials {

  private final String user;
  private final String password;

  public Credentials(String user, String password) {
    this.user = user;
    this.password = password;
  }

  public String getUser() {
    return user;
  }

  public String getPassword() {
    return password;
  }

  public boolean isComplete() {
    return user != null && !user.isEmpty() && password != null && !password.isEmpty();
  }

}
