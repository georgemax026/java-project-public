package main.java.myApp.service;

import main.java.myApp.model.*;
import main.java.myApp.repository.*;

import java.util.ArrayList;
import java.util.List;


public class SearchService {
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;
    private final SeasonRepository seasonRepository;
    private final EpisodeRepository episodeRepository;


    /**
     * Constructs a SearchService with a given repository.
     */
    public SearchService (ActorRepository actorRepository, DirectorRepository directorRepository, MovieRepository movieRepository
            , SeriesRepository seriesRepository, SeasonRepository seasonRepository, EpisodeRepository episodeRepository) {
        this.actorRepository = actorRepository;
        this.directorRepository = directorRepository;
        this.movieRepository = movieRepository;
        this.seriesRepository = seriesRepository;
        this.seasonRepository = seasonRepository;
        this.episodeRepository = episodeRepository;
    }

    /**
     * searches for media matching the query
     * @param query the query to search media for
     * @return a {@link List<Media>} of Media that match the query (Media could be Movies or Series) or an empty List.
     */
    public List<Media> searchMedia(String query) {
        ArrayList<Media> allContent = new ArrayList<>();
        allContent.addAll(movieRepository.findAll());
        allContent.addAll(seriesRepository.findAll());
        return allContent.stream().filter(media -> (media.getTitle()
                .toLowerCase()
                .contains(query.toLowerCase())))
                .toList();
    }
    /**
     * search for a director via name
     * @param query the search query for the name
     * @return a {@link List<Director>} of directors that match the query or an empty list if none do
     */
    public List<Director> searchDirectorByName(String query) {
        return directorRepository.findAll().stream().filter(director -> (director.getFirstName() + " " + director.getLastName())
                .toLowerCase()
                .contains(query.toLowerCase()))
                .toList();
    }
    /**
     * search for an actor via name
     * @param query the search query for the name
     * @return a {@link List<Actor>} of actors that match the query or an empty list if none do
     */
    public List<Actor> searchActorByName(String query) {
        return actorRepository.findAll().stream().filter(director -> (director.getFirstName() + " " + director.getLastName())
                        .toLowerCase()
                        .contains(query.toLowerCase()))
                .toList();
    }
    /**
     * search for a movie via title
     * @param query the search query for the title
     * @return a {@link List<Movie>} of movies that match the query or an empty list if none do
     */
    public List<Movie> searchMovieByTitle(String query) {
        return movieRepository.findAll().stream().filter(movie -> movie.getTitle()
                        .toLowerCase()
                        .contains(query.toLowerCase()))
                .toList();
    }
    /**
     * searches for movies of a specific genre
     * @param genre the genre to search for
     * @return a {@link List<Movie>} of the movie that march the genre or an empty list
     */
    public List<Movie> searchMovieByGenre(Genre genre) {
        return movieRepository.findAll().stream()
                .filter(movie -> (movie.getGenre() == genre))
                .toList();
    }
    /**
     * search for a movie by minimum IMDb rating
     * @param minRating the minimum rating for the movie
     * @return a {@link List<Movie>} of movies that have rating above minRating or an empty list if none do
     */
    public List<Movie> searchMovieByImdbRating(double minRating) {
        return movieRepository.findAll().stream().filter(movie -> (movie.getImdbRating() >= minRating)).toList();
    }
    /**
     * search for a movie by minimum user rating
     * @param minRating the minimum rating for the movie
     * @return a {@link List<Movie>} of movies that have rating above minRating or an empty list if none do
     */
    public List<Movie> searchMovieByUserRating(double minRating) {
        return movieRepository.findAll().stream().filter(movie -> (movie.getUserRatingAverage() >= minRating)).toList();
    }

    /**
     * search for movies that have a specific actor
     * @param query the query of the actor name
     * @return a {@link List<Movie>} of movies with that actor or an empty list
     */
    public List<Movie> searchMovieByActor(String query) {
        return movieRepository.findAll().stream().filter(movie -> (movie.getProtagonist()
                    .toLowerCase()
                    .contains(query.toLowerCase())))
                .toList();
    }
    /**
     * search for movies that have a specific director
     * @param query the query of the director name
     * @return a {@link List<Movie>} of movie with that director or an empty list
     */
    public List<Movie> searchMovieByDirector(String query) {
        return movieRepository.findAll().stream().filter(movie -> (movie.getDirector()
                        .toLowerCase()
                        .contains(query.toLowerCase())))
                .toList();
    }


    /**
     * search for series via title
     * @param query the query for title
     * @return a {@link List<Series>} of series that match the query or an empty list.
     */
    public List<Series> searchSeriesByTitle(String query) {
        return seriesRepository.findAll().stream().filter(series -> (series.getTitle()
                    .toLowerCase()
                    .contains(query.toLowerCase())))
                .toList();
    }
    /**
     * searches for series of a specific genre
     * @param genre the genre to search for
     * @return a {@link List<Series>} of the series that match the genre or an empty list
     */
    public List<Series> searchSeriesByGenre(Genre genre) {
        return seriesRepository.findAll().stream()
                .filter(series -> (series.getGenre() == genre))
                .toList();
    }
    /**
     * search for series by minimum user rating
     * @param minRating the minimum rating for the series
     * @return a {@link List<Series>} of series that have rating above minRating or an empty list if none do
     */
    public List<Series> searchSeriesByUserRating(double minRating) {
        return seriesRepository.findAll().stream().filter(movie -> (movie.getUserRatingAverage() >= minRating)).toList();
    }
}