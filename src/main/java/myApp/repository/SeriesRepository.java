package main.java.myApp.repository;

import main.java.myApp.model.Series;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository {
    /**
     * get a list of every series from the database
     * @return a {@link List<Series>} with every series that exists in the database
     */
    List<Series> findAll();
    /**
     * Finds a series by its unique ID.
     * @param id the id of the series
     * @return an {@link Optional} containing the series if found, otherwise empty
     */
    Optional<Series> findById(int id);
    /**
     * Finds a series by its title.
     * @param title the title of the series
     * @return an {@link Optional} containing the series if found, otherwise empty
     */
    Optional<Series> findByTitle(String title);
    /**
     * saves a series to the database
     * @param series the series to be saved
     */
    void save(Series series);
    /**
     * modify a specific user's rating of a specific series
     * @param seriesId the series to be rated
     * @param userId the id of the user that rates the series
     * @param rating the user's rating of the series
     */
    void modifyRating(int seriesId, int userId, int rating);
    /**
     * checks if a movie with the same title already exists
     * @param title the title of the movie to search
     * @return true if a movie with the same title exists and false if it doesn't
     */
    boolean existsByTitle(String title);
}
