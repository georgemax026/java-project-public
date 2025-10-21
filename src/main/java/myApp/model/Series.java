package main.java.myApp.model;

import java.util.Map;
import java.util.Objects;

public class Series extends Media {

  public Series(int id, String title, Genre genre, Map<Integer, Integer> userRating) {
    super(id, title, genre, userRating);
  }
  public Series(String title, Genre genre, Map<Integer, Integer> userRating) {
    super(title, genre, userRating);
  }

  // method for db String representation
  public String toDbString() {
  // seriesId, title, genre, user_rating (key:value)
    Map<Integer, Integer> userRatings = getUserRating();
    // Check if the series has user rating, and if not just leave the field empty
    if (userRatings.isEmpty()) {
      return getId() + "," + getTitle() + "," + getGenre() + "," + "\n";
    } else {
      StringBuilder userRatingsBuilder = new StringBuilder();
      userRatings.forEach((key, value) -> {
        userRatingsBuilder.append(key).append(":").append(value).append("|");
      });
      // remove the last character that is an excessive "|"
      String ratings = userRatingsBuilder.substring(0, userRatingsBuilder.length() - 1);

      return getId() + "," + getTitle() + "," + getGenre() + "," + ratings + "\n";
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Series series = (Series) o;
    return Objects.equals(getTitle(), series.getTitle()) &&
            Objects.equals(getGenre(), series.getGenre()) &&
            Objects.equals(getUserRating(), series.getUserRating());
  }
}
