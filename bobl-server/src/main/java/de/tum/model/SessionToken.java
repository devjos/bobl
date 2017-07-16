package de.tum.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.Gson;

import de.tum.util.RandomUtil;

public class SessionToken {

  private String user;
  private String token;
  private String expirationDate;

  public SessionToken(String user) {
    this(user, DigestUtils.sha256Hex(RandomUtil.generate(20)));
  }

  public SessionToken(String user, String token) {
    this.user = Objects.requireNonNull(user);
    this.token = Objects.requireNonNull(token);
    this.expirationDate = LocalDateTime.now().plusDays(30).toString();
  }

  public String getToken() {
    return token;
  }

  public String getExpirationDate() {
    return expirationDate;
  }

  public String getUser() {
    return user;
  }

  public Cookie toCookie() {
    return new Cookie("session", Base64.encodeBase64String(new Gson().toJson(this).getBytes()));
  }

  public String toCookieString() {
    return new NewCookie(toCookie()).toString();
  }

  public static SessionToken fromCookie(Cookie c) {
    String val = new String(Base64.decodeBase64(c.getValue()));
    return new Gson().fromJson(val, SessionToken.class);
  }

  public static SessionToken generate(String user) {
    return new SessionToken(user, RandomUtil.generate(12));
  }

  public boolean isExpired() {
    return LocalDateTime.parse(expirationDate).isBefore(LocalDateTime.now());
  }

}
