package main.java.myApp.model;

import java.time.LocalDate;
import java.util.Objects;

public class Actor extends Person {
  private final Race race;

// Constructors
  public Actor(int id, String firstName, String lastName, LocalDate birthDate, Gender gender, Race race) {
    super(id, firstName, lastName, birthDate, gender);
    this.race = race;
  }

  public Actor(String firstName, String lastName, LocalDate birthDate, Gender gender, Race race) {
    super(firstName, lastName, birthDate, gender);
    this.race = race;
  }
  // getters
  public Race getRace() {
    return this.race;
  }

  //  Method for DB String Representation
  public String toDbString() {
    return this.getId() + "," + this.getFirstName() + "," + this.getLastName() + "," + this.getBirthDate() + "," + this.getGender().toString().charAt(0) + "," + this.getRace() + '\n';
  }
// override toString, equals, and hashCode
  @Override
  public String toString() {
    return super.toString() + ", Race: " + this.race;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Actor actor = (Actor) o;
    return Objects.equals(getFirstName(), actor.getFirstName()) &&
            Objects.equals(getLastName(), actor.getLastName()) &&
            Objects.equals(getBirthDate(), actor.getBirthDate()) &&
            Objects.equals(getGender(), actor.getGender()) &&
            Objects.equals(getRace(), actor.getRace());
  }
}
