package main.java.myApp.model;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
  private int id;
  private String firstName;
  private String lastName;
  private String username;
  private String email;
  private final String EMAIL_REGEX =
          "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
  private final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);


  // Constructors
  public User(int id, String firstName, String lastName, String username, String email) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    setEmail(email);
  }
  public User(String firstName, String lastName, String username, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    setEmail(email);
  }

  // getters

  public int getId() {
    return this.id;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public String getEmail() {
    return this.email;
  }

  public String getUsername() {
    return this.username;
  }

  //setters
  public void setId(int id) {
    this.id = id;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
      throw new IllegalArgumentException("Email cannot be null or empty.");
    }
    if (!isValidEmailFormat(email)) {
      throw new IllegalArgumentException("Invalid email format: " + email);
    }
    this.email = email.trim();
  }

  // method to validate if the email is an actual email
  private boolean isValidEmailFormat(String email) {
    Matcher matcher = EMAIL_PATTERN.matcher(email);
    return matcher.matches();
  }

  // toString for db representation
  public String toDbString() {
      return id + "," + firstName + "," + lastName + "," + username + "," + email + "\n";
  }

  // toString implementation
  public String toString() {
    return "id = " + this.id + ", firstName = " + this.firstName + ", lastName = " + this.lastName + ", username = " + this.username + ", email = " + this.email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(firstName, user.getFirstName()) &&
            Objects.equals(lastName, user.getLastName()) &&
            Objects.equals(username, user.getUsername()) &&
            Objects.equals(email, user.getEmail());
  }
}
