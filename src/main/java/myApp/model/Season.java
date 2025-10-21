package main.java.myApp.model;

import java.time.Year;
import java.util.Objects;

public class Season implements RepositoryRequirements {
  private int id;
  private final int seriesId;
  private final Year releaseYear;
  private final int seasonNumber;

  public Season(int id, int seriesId, int seasonNumber, Year releaseYear) {
    this.id = id;
    this.seriesId = seriesId;
    this.seasonNumber = seasonNumber;
    this.releaseYear = releaseYear;
  }

  public Season(int seriesId, int seasonNumber, Year releaseYear) {
    this.seriesId = seriesId;
    this.seasonNumber = seasonNumber;
    this.releaseYear = releaseYear;
  }
// getters
  public int getId() {
    return this.id;
  }

  public int getSeriesId() {
    return this.seriesId;
  }

  public Year getReleaseYear() {
    return this.releaseYear;
  }

  public int getSeasonNumber() {
    return this.seasonNumber;
  }

//  setters
  public void setId(int id) {
    this.id = id;
  }

  // toString for db representation
  public String toDbString() {
    return id + "," + seriesId + "," + seasonNumber + "," + releaseYear + "\n";
  }

  //toString implementation
  @Override
  public String toString() {
    return "id = " + this.id + ", seriesId = " + this.seriesId +", seasonNumber = " + this.seasonNumber + ", releaseYear = " + this.releaseYear;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Season season = (Season) o;
    return Objects.equals(seasonNumber, season.getSeasonNumber()) &&
            Objects.equals(releaseYear, season.getReleaseYear());
  }
}
