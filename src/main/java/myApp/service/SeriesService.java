package main.java.myApp.service;

import main.java.myApp.model.Episode;
import main.java.myApp.model.Genre;
import main.java.myApp.model.Season;
import main.java.myApp.model.Series;
import main.java.myApp.repository.EpisodeRepository;
import main.java.myApp.repository.SeasonRepository;
import main.java.myApp.repository.SeriesRepository;

import java.time.Year;
import java.util.*;

public class SeriesService {
    private final SeriesRepository seriesRepository;
    private final SeasonRepository seasonRepository;
    private final EpisodeRepository episodeRepository;

    public SeriesService(SeriesRepository seriesRepo, SeasonRepository seasonRepo, EpisodeRepository episodeRepo) {
        this.seriesRepository = seriesRepo;
        this.seasonRepository = seasonRepo;
        this.episodeRepository = episodeRepo;
    }

    /**
     * Retrieves a series by its ID.
     * @param id The ID of the series to find.
     * @return The found series object.
     * @throws SeriesNotFoundException if no series with the given ID exists.
     */
    public Series getSeriesById(int id) throws SeriesNotFoundException {
        return seriesRepository.findById(id)
                .orElseThrow(() -> new SeriesNotFoundException("Series with ID " + id + " not found."));
    }
    /**
     * Retrieves a season by its ID,
     * @param id the ID of the season to find
     * @return the found season
     * @throws SeasonNotFoundException if no season with the given ID exists.
     */
    public Season getSeasonById(int id) throws SeasonNotFoundException {
        return seasonRepository.findById(id)
                .orElseThrow(() -> new SeasonNotFoundException("Season with ID " + id + " not found."));
    }
    /**
     * Retrieves a list of seasons by their paren series' ID.
     * @param seriesId The ID of the parent series of the seasons to find.
     * @return the {@link List<Season>} of seasons from the series or an empty list
     */
    public List<Season> getSeasonsBySeriesId(int seriesId) {
        return seasonRepository.findBySeriesId(seriesId);
    }
    /**
     * Retrieves an episode by its ID.
     * @param id The ID of the episode to find.
     * @return The found Episode object.
     * @throws EpisodeNotFoundException if no Episode with the given ID exists.
     */
    public Episode getEpisodeById(int id) throws EpisodeNotFoundException {
        return episodeRepository.findById(id)
                .orElseThrow(() -> new EpisodeNotFoundException("Movie with ID " + id + " not found."));
    }
    /**
     * Retrieves a list of Episodes by their paren Season's ID.
     * @param seasonId The ID of the parent Season of the episodes to find.
     * @return the {@link List<Episode>} of episodes from that season or an empty list
     */
    public List<Episode> getEpisodesBySeasonId(int seasonId) {
        return episodeRepository.findBySeasonId(seasonId);
    }
    /**
     * Retrieves all series from the repository.
     * @return A {@link List<Series>} of all series.
     */
    public List<Series> getAllSeries() {
        return seriesRepository.findAll();
    }
    /**
     * Retrieves series with matching title
     * @param title the title of the series
     * @return the series with the matching title
     * @throws SeriesNotFoundException if series with that title does not exist
     */
    public Series getSeriesByTitle(String title) throws SeriesNotFoundException {
        Optional<Series> series = seriesRepository.findByTitle(title);
        if (series.isEmpty()) {
            throw new SeriesNotFoundException("Series with title: " + title + " was not found");
        } else {
            return series.get();
        }
    }
    /**
     * Retrieves a season by its number
     * @param seriesId the id of the season's parent series
     * @param seasonNumber the number of the season
     * @return the season with the matching number and parent series
     * @throws SeriesNotFoundException if there's no series matching the give seriesId
     * @throws SeasonNotFoundException if there's no season with that number of the parent series
     */
    public Season getSeasonBySeasonNumber(int seriesId, int seasonNumber) throws SeriesNotFoundException, SeasonNotFoundException{
        Optional<Series> series = seriesRepository.findById(seriesId);
        Optional<Season> season = seasonRepository.findSeasonBySeasonNumber(seriesId, seasonNumber);
        if (series.isEmpty()) {
            throw new SeriesNotFoundException("Series with ID: " + seriesId + " was not found");
        } else if (season.isEmpty()) {
            throw new SeasonNotFoundException("season " + seasonNumber + " of series with id: " + seriesId + "was not found");
        } else {
            return season.get();
        }
    }
    /**
     * Adds a new series to the system after validating the provided data.
     * @param title The title of the Series.
     * @param genre The genre of the Series.
     * @return The newly created Series object with its assigned ID.
     * @throws InvalidSeriesDataException if the input data is invalid.
     * @throws SeriesAlreadyExistsException if a series with the same title already exists (propagated from repository).
     */
    public Series addSeries(String title, Genre genre) throws SeriesAlreadyExistsException, InvalidSeriesDataException{
        if (title.isBlank()) {
            throw new InvalidSeriesDataException("Title cannot be blank");
        } else if (genre == null) {
            throw new InvalidSeriesDataException("Genre cannot be null");
        }
        List<Series> series = seriesRepository.findAll();
        Optional<Series> existingSeries = series.stream()
                .filter(currSeries -> currSeries.getTitle().equalsIgnoreCase(title))
                .findFirst();
        if  (existingSeries.isPresent()) {
            throw new SeriesAlreadyExistsException("Series with title: \"" + title + "\" already exists");
        } else {
            Series newSeries = new Series(title, genre, Collections.emptyMap());
            seriesRepository.save(newSeries);
            return newSeries;
        }
    }

    /**
     *  adds a new episode to the system
     * @param seasonId id of the season that have the new episode
     * @param duration duration of the episode in minutes
     * @param director the director of the episode
     * @param imdbRating imdb rating of the specific episode
     * @param protagonist the lead actor of the episode
     * @return instance of the newly created episode
     * @throws InvalidEpisodeDataException if you try to add an episode to a season that doesn't exist in the database, or if the durations is negative or if the imdbRating is negative or > 10.0
     */
    public Episode addEpisode(int seasonId, int duration, String director, double imdbRating, String protagonist) throws InvalidEpisodeDataException {
        Episode newEpisode;
        Optional<Season> seasonOfEpisode = seasonRepository.findById(seasonId);
        if (seasonOfEpisode.isEmpty()) {
            throw new InvalidEpisodeDataException("Cannot add episode to non existent season. Provided seasonId = " + seasonId);
        } else if (duration < 0) {
            throw new InvalidEpisodeDataException("Duration can't be negative");
        } else if (imdbRating < 0.0 || imdbRating > 10.0) {
            throw new InvalidEpisodeDataException("imdb rating must be between 0.0 and 10.0");
        } else {
            newEpisode = new Episode(seasonId, duration, director, imdbRating, protagonist);
        }
        episodeRepository.save(newEpisode);
        return newEpisode;
    }

    /**
     * adds new season to a known Series to the database
     * @param seriesId the id of the series that will own this season
     * @param seasonNumber the number of the season (ie season1, season2 etc.)
     * @param releaseYear the year that the season was released
     * @return an instance of the new season
     * @throws InvalidSeriesDataException if you try to add a season to a series that's not in the db or the provided release year is in the future
     * @throws SeasonAlreadyExistsException if a season with the given seasonNumber already exists to the given parent series
     */
    public Season addSeason(int seriesId, int seasonNumber, Year releaseYear)
            throws InvalidSeriesDataException, SeasonAlreadyExistsException {
        Optional<Series> seriesOfSeason = seriesRepository.findById(seriesId);
        if (seriesOfSeason.isEmpty()) {
            throw new InvalidSeriesDataException("Cannot add season to non existent series. Provided seriesId = " + seriesId);
        } else if (releaseYear.isAfter(Year.now().plusYears(1)))  {
            throw new InvalidSeriesDataException("Season cannot be from the future year: " + releaseYear.toString());
        }

        List<Season> allSeasons = seasonRepository.findAll();
        // get the list of the seasons that belong to the parent series
        List<Season> currSeriesSeason = allSeasons.stream().filter(season -> season.getSeriesId() == seriesId).toList();
        Optional<Season> existingSeason = currSeriesSeason.stream()
                .filter(currSeason -> (currSeason.getSeasonNumber() == seasonNumber))
                .findFirst();
        if  (existingSeason.isPresent()) {
            throw new SeasonAlreadyExistsException("Season \"" + seasonNumber + "\" from Series with id \" "
                    + seriesId + "\" already exists");
        } else {
            Season newSeason = new Season(seriesId, seasonNumber, releaseYear);
            seasonRepository.save(newSeason);
            return newSeason;
        }
    }

    /**
     * Allows a user to add or update their rating for a series.
     * @param seriesId The ID of the series to modify/add rating.
     * @param userId The ID of the user giving the rating.
     * @param rating The rating value (1-10).
     * @throws SeriesNotFoundException if the series ID does not exist.
     * @throws InvalidSeriesDataException if the rating value is outside the valid range.
     */
    public void rateSeries(int seriesId, int userId, int rating) throws InvalidSeriesDataException, SeriesNotFoundException {
        if (rating < 1 || rating > 10) {
            throw new InvalidSeriesDataException("Rating must be between 1 and 10.");
        }
        try {
            seriesRepository.modifyRating(seriesId, userId, rating);
        } catch (IllegalArgumentException e) {
            throw new SeriesNotFoundException("Cannot rate. Series with ID " + seriesId + " not found.");
        }
    }
    /**
     * checks if a series with the same title already exists
     * @param title the title of the series to search
     * @return true if a series with the same title exists and false if it doesn't
     */
    public boolean seriesExists(String title) {
        return seriesRepository.existsByTitle(title);
    }
}
