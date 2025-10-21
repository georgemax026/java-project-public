package main.java.myApp.model;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Objects;

public class Director extends Person {
  private final ArrayList<String> bestOf;

//  Constructors
  public Director(int id, String firstName, String lastName, LocalDate birthDate, Gender gender,
                  ArrayList<String> bestOf) {
    super(id, firstName, lastName, birthDate, gender);
    this.bestOf = bestOf;
  }

  public Director(String firstName, String lastName, LocalDate birthDate, Gender gender,
                  ArrayList<String> bestOf) {
    super(firstName, lastName, birthDate, gender);
    this.bestOf = bestOf;
  }

  public ArrayList<String> getBestOf() {
    return bestOf;
  }
  // add a single movie or Series to the director's bestOf
  public void addBestOf(String title) {
    bestOf.add(title);
  }


  //  Method for DB String Representation
  public String toDbString() {
    StringBuilder bestOf = new StringBuilder();
    boolean first = true;
    for (String s : this.bestOf) {
      if (first) {
        bestOf.append(s);
        first = false;
      } else {
        bestOf.append("|").append(s);
      }
    }
    return getId() + "," +getFirstName() + "," + getLastName() + "," + getBirthDate() + "," + getGender().toString().charAt(0) + "," + bestOf + '\n';
  }

  @Override
  public String toString() {
    return super.toString() + ", bestOf=" + bestOf + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Director director = (Director) o;
    return Objects.equals(getFirstName(), director.getFirstName()) &&
            Objects.equals(getLastName(), director.getLastName()) &&
            Objects.equals(getBirthDate(), director.getBirthDate()) &&
            Objects.equals(getGender(), director.getGender()) &&
            Objects.equals(bestOf, director.getBestOf());
  }
}
