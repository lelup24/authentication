package de.tutorial.authentication.backend.user;

import java.util.Objects;

public class UserRegisterationDto {
  private String username;
  private String password;

  public String getUsername() {
    return username;
  }

  public UserRegisterationDto setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public UserRegisterationDto setPassword(String password) {
    this.password = password;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserRegisterationDto that = (UserRegisterationDto) o;
    return Objects.equals(username, that.username) && Objects.equals(password, that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password);
  }
}
