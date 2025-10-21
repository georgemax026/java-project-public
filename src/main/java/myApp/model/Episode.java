package main.java.myApp.model;

import java.util.Objects;

public class Episode {
  private int id;
  private final int seasonId;
  private final int duration;
  private final String director;
  private double imdbRating;
  private final String protagonist;

  public Episode(int id, int seasonId, int duration, String director, double imdbRating, String protagonist) {
    this.id = id;
    this.seasonId = seasonId;
    this.duration = duration;
    this.director = director;
    this.imdbRating = imdbRating;
    this.protagonist = protagonist;
  }

  public Episode(int seasonId,  int duration, String director, double imdbRating, String protagonist) {
    this.seasonId = seasonId;
    this.duration = duration;
    this.director = director;
    this.imdbRating = imdbRating;
    this.protagonist = protagonist;
  }

  // getters
  public int getId() {
    return id;
  }

  public int getSeasonId() {
    return seasonId;
  }

  public int getDuration() {
    return duration;
  }

  public double getImdbRating() {
    return imdbRating;
  }

  public String getDirector() {
    return director;
  }

  public String getProtagonist() {
    return protagonist;
  }

  // setters
  public void setId(int id) {
    this.id = id;
  }

  public void setImdbRating(double newRating) {
    this.imdbRating = newRating;
  }

  // toString method for db representation
  public String toDbString() {
    return id + "," + seasonId + "," + duration + "," + director + "," + imdbRating + "," + protagonist + "\n";
  }

  // toString implementation
  @Override
  public String toString() {
    return "id = " + id + ", seasonId = " + seasonId + ", duration = " + duration + ", director = " + director + ", imdbRating = " + imdbRating + ", protagonist = " + protagonist;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Episode episode = (Episode) o;
    return Objects.equals(duration, episode.duration) &&
            Objects.equals(director, episode.director) &&
            Objects.equals(imdbRating, episode.imdbRating) &&
            Objects.equals(protagonist, episode.protagonist);
  }

}
