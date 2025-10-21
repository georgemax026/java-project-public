package main.java.myApp.model;

import java.util.ArrayList;
import java.util.Map;

public abstract class Media implements RepositoryRequirements {
  private int id;
  private final String title;
  private final Map<Integer, Integer> userRating;
  private final Genre genre;
  private double userRatingAverage;


  // Constructors
  public Media(int id, String title, Genre genre, Map<Integer, Integer> userRating) {
    this.id = id;
    this.title = title;
    this.genre = genre;
    this.userRating = userRating;
    userRatingAverage = calculateUserRatingAverage();
  }
  public Media(String title, Genre genre, Map<Integer, Integer> userRating) {
    this.title = title;
    this.genre = genre;
    this.userRating = userRating;
    userRatingAverage = calculateUserRatingAverage();
  }

  // getters
  public int getId() {
    return this.id;
  }

  public Map<Integer, Integer> getUserRating() {
    return this.userRating;
  }

  public String getTitle() {
    return this.title;
  }

  public Genre getGenre() {
    return this.genre;
  }

  public double getUserRatingAverage() {
    return this.userRatingAverage;
  }


  // setters
  public void setId(int id) {
    this.id = id;
  }

  /**
   * used to add user rating or change existing user rating
   * @param userId the id of the user that has the grade
   * @param grade the grade the user will choose for this media
   */
  public void setUserRating(int userId, int grade) {
    // put the rating in the Map
    userRating.put(userId, grade);
    // update the user average
    userRatingAverage = calculateUserRatingAverage();
  }
  // helper method to calculate the rating average
  private double calculateUserRatingAverage() {
    ArrayList<Integer> ratings = new ArrayList<>();
    int i = 0;
    userRating.forEach((user, rating) -> {
      ratings.add(rating);
    });
    Integer sum = 0;
    for (Integer rating : ratings) {
      sum += rating;
    }
    return (sum / (double) ratings.size());
  }
  public String toString() {
    // maybe add getUserRatingAverage()
      return "id = " + id + ", title = " + title + ", genre = " + genre + ", userRating = " + userRating;
  }
}
