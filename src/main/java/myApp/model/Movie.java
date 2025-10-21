package main.java.myApp.model;

import java.time.Year;
import java.util.Map;
import java.util.Objects;

public class Movie extends Media {
  private final int duration;
  private double imdbRating;
  private final Year releaseYear;
  private final String director;
  private final String protagonist;

  // Constructors
  public Movie(int id, String title, Year releaseYear, Genre genre, int duration, String director,
      double imdbRating, String protagonist, Map<Integer, Integer> userRating) {
    super(id, title, genre, userRating);
    this.releaseYear = releaseYear;
    this.duration = duration;
    this.imdbRating = imdbRating;
    this.director = director;
    this.protagonist = protagonist;
  }

  public Movie(String title, Year releaseYear, Genre genre, int duration, String director,
               double imdbRating, String protagonist, Map<Integer, Integer> userRating) {
    super(title, genre, userRating);
    this.releaseYear = releaseYear;
    this.duration = duration;
    this.imdbRating = imdbRating;
    this.director = director;
    this.protagonist = protagonist;
  }

  public int getDuration() {
    return this.duration;
  }

  public double getImdbRating() {
    return this.imdbRating;
  }

  public Year getReleaseYear() {
    return this.releaseYear;
  }

  public String getDirector() {
    return this.director;
  }

  public String getProtagonist() {
    return this.protagonist;
  }

  public void setImdbRating(double newRating) {
    this.imdbRating = newRating;
  }

  // toString for db representation
  public String toDbString() {
    Map<Integer, Integer> userRatings = getUserRating();
    // Check if the movie has user rating, and if not just leave the field empty
    if (userRatings.isEmpty()) {
      return getId() + "," + getTitle() + "," + releaseYear + "," + getGenre() + "," + duration + "," + director + "," + imdbRating + "," + protagonist + "," + "\n";
    } else {
      StringBuilder userRatingsBuilder = new StringBuilder();
      getUserRating().forEach((key, value) -> {
        userRatingsBuilder.append(key).append(":").append(value).append("|");
      });
      // remove the last character that is an excessive "|"
      String ratings = userRatingsBuilder.toString().substring(0, userRatingsBuilder.length() - 1);
      return getId() + "," + getTitle() + "," + releaseYear + "," + getGenre() + "," + duration + "," + director + "," + imdbRating + "," + protagonist + "," + ratings + "\n";
    }
  }

  @Override
  public String toString() {
    return super.toString() + ", duration=" + duration + ", imdbRating=" + imdbRating + ", releaseYear=" + releaseYear + ", director=" + director + ", protagonist=" + protagonist;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Movie movie = (Movie) o;
    return Objects.equals(getTitle(), movie.getTitle()) &&
            Objects.equals(releaseYear, movie.getReleaseYear()) &&
            Objects.equals(getGenre(), movie.getGenre()) &&
            Objects.equals(duration, movie.getDuration()) &&
            Objects.equals(director, movie.getDirector()) &&
            Objects.equals(imdbRating, movie.getImdbRating()) &&
            Objects.equals(protagonist, movie.getProtagonist()) &&
            Objects.equals(getUserRating(), movie.getUserRating());
  }
}
