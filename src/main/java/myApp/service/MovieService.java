package main.java.myApp.service;

import main.java.myApp.model.Genre;
import main.java.myApp.model.Movie;
import main.java.myApp.repository.MovieRepository;

import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class MovieService {
    private final MovieRepository movieRepository;

    /**
     * Constructs a MovieService with a given repository.
     * @param movieRepository The data access layer for movies.
     */
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Retrieves a movie by its ID.
     * @param id The ID of the movie to find.
     * @return The found Movie object.
     * @throws MovieNotFoundException if no movie with the given ID exists.
     */
    public Movie getMovieById(int id) throws MovieNotFoundException {
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie with ID " + id + " not found."));
    }

    /**
     * Retrieves all movies from the repository
     * @return A {@link List<Movie>} of all movies.
     */
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
    /**
     * Retrieves a movie by its title.
     * @param title The title of the movie to find.
     * @return The found Movie object.
     * @throws MovieNotFoundException if no movie with the given title exists.
     */
    public Movie getMovieByTitle(String title) throws MovieNotFoundException {
        return movieRepository.findByTitle(title)
                .orElseThrow(() -> new MovieNotFoundException("Movie with title " + title + " not found."));
    }
    /**
     * Adds a new movie to the system after validating the provided data.
     * @param title The title of the movie.
     * @param releaseYear The year the movie was released.
     * @param genre The genre of the movie.
     * @param length The length of the movie in minutes.
     * @param director The director.
     * @param imdbRating The IMDB rating.
     * @param protagonist The lead actor
     * @return The newly created Movie object.
     * @throws InvalidMovieDataException if the input data is invalid.
     * @throws MovieAlreadyExistsException if a movie with the same title already exists (propagated from repository).
     */
    public Movie addMovie(String title, Year releaseYear, Genre genre, int length,
                             String director, double imdbRating, String protagonist) throws InvalidMovieDataException, MovieAlreadyExistsException {
        // validate input
        if (title.isBlank()) {
            throw new InvalidMovieDataException("Movie title cannot be empty.");
        } else if (releaseYear.isAfter(Year.now().plusYears(1))) {
            throw new InvalidMovieDataException("Release year cannot be in the distant future.");
        } else if (length <= 0) {
            throw new InvalidMovieDataException("Movie length must be a positive number.");
        }
        List<Movie> movies = movieRepository.findAll();
        Optional<Movie> existingMovie = movies.stream()
                .filter(currMovie -> currMovie.getTitle().equalsIgnoreCase(title))
                .findFirst();
        if  (existingMovie.isPresent()) {
            throw new MovieAlreadyExistsException("Movie with title: \"" + title + "\" already exists");
        } else {
            Movie newMovie = new Movie(title, releaseYear, genre, length, director, imdbRating, protagonist, Collections.emptyMap());
            movieRepository.save(newMovie);
            return newMovie;
        }
    }
    /**
     * Deletes a movie from the system.
     * @param id The ID of the movie to delete.
     * @throws MovieNotFoundException if no movie with the given ID exists.
     */
    public void deleteMovie(int id) {
        // --- Business Logic: Ensure the movie exists before attempting to delete ---
        if (movieRepository.findById(id).isEmpty()) {
            throw new MovieNotFoundException("Cannot delete. Movie with ID " + id + " not found.");
        }
        movieRepository.deleteById(id);
    }
    /**
     * Allows a user to add or update their rating for a movie.
     * @param movieId The ID of the movie to rate.
     * @param userId The ID of the user giving the rating.
     * @param rating The rating value (1-10).
     * @throws MovieNotFoundException if the movie ID does not exist.
     * @throws InvalidMovieDataException if the rating value is outside the valid range.
     */
    public void rateMovie(int movieId, int userId, int rating) {
        if (rating < 1 || rating > 10) {
            throw new InvalidMovieDataException("Rating must be between 1 and 10.");
        }
        try {
            movieRepository.modifyRating(movieId, userId, rating);
        } catch (IllegalArgumentException e) {
            throw new MovieNotFoundException("Cannot rate. Movie with ID " + movieId + " not found.");
        }
    }
    /**
     * checks if a movie with the same title already exists
     * @param title the title of the movie to search
     * @return true if a movie with the same title exists and false if it doesn't
     */
    public boolean movieExists(String title) {
        return movieRepository.existsByTitle(title);
    }
}
