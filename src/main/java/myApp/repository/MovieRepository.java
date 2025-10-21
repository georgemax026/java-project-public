package main.java.myApp.repository;

import main.java.myApp.model.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {
    /**
     * get a list of every movie from the database
     * @return a {@link List<Movie>} with every movie that exists in the database
     */
    List<Movie> findAll();
    /**
     * Finds a movie by its unique ID.
     * @param id the id of the movie
     * @return an {@link Optional} containing the movie if found, otherwise empty
     */
    Optional<Movie> findById(int id);
    /**
     * Finds a movie by its title.
     * @param title the title of the movie
     * @return an {@link Optional} containing the movie if found, otherwise empty
     */
    Optional<Movie> findByTitle(String title);
    /**
     * saves a movie to the database
     * @param movie the movie to be saved
     */
    void save(Movie movie);
    /**
     * saves a list of movies to the database
     * @param movies the {@link List<Movie>} of movies to be saved
     */
    void saveAll(List<Movie> movies);
    /**
     * modify a specific user's rating of a specific movie
     * @param movieId the movie to be rated
     * @param userId the id of the user that rates the movie
     * @param rating the user's rating of the movie
     */
    void modifyRating(int movieId, int userId, int rating);
    /**
     * deletes the movie with the specific id form the database
     * @param id the ID of the movie to be deleted
     */
    void deleteById(int id);
    /**
     * checks if a movie with the same title already exists
     * @param title the title of the movie to search
     * @return true if a movie with the same title exists and false if it doesn't
     */
    boolean existsByTitle(String title);
}
