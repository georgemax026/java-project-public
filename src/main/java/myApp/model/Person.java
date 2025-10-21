package main.java.myApp.model;

import java.time.LocalDate;

abstract class Person implements RepositoryRequirements {
  private int id;
  private String firstName;
  private String lastName;
  private final LocalDate birthDate;
  private final Gender gender;


  // constructors
  public Person(int id, String firstName, String lastName, LocalDate birthDate, Gender gender) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthDate = birthDate;
    this.gender = gender;
  }

  public Person(String firstName, String lastName, LocalDate birthDate, Gender gender) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthDate = birthDate;
    this.gender = gender;
  }

  // getters
  public int getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public Gender getGender() {
    return gender;
  }
  // setters
  public void setId(int id) {
    this.id = id;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  // toString implementation
  @Override
  public String toString() {
      return "id = " + id + " ," + "firstName = " + firstName + ", lastName = " + lastName + ", birthDate = " + birthDate;
  }
}
