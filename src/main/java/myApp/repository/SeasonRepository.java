package main.java.myApp.repository;

import main.java.myApp.model.Season;

import java.util.List;
import java.util.Optional;

public interface SeasonRepository {
    /**
     * get a list of every season from the database
     * @return a {@link List<Season>} with every season that exists in the database
     */
    List<Season> findAll();
    /**
     * Finds a season by its unique ID.
     * @param id the id of the season
     * @return an {@link Optional} containing the season if found, otherwise empty
     */
    Optional<Season> findById(int id);
    /**
     * Finds a season by its number.
     * @param seriesId the id of the parentSeries.
     * @param seasonNumber the number of the season to find.
     * @return an {@link Optional} containing the season if found, otherwise empty
     */
    Optional<Season> findSeasonBySeasonNumber(int seriesId, int seasonNumber);
    /**
     * Finds all seasons associated with a specific parent series.
     * @param seriesId The ID of the parent series.
     * @return A {@link List<Season>} of seasons for that series.
     */
    List<Season> findBySeriesId(int seriesId);
    /**
     * saves a list of seasons to the database
     * @param seasons the {@link List<Season>} of seasons to be saved
     */
    void saveAll(List<Season> seasons);
    /**
     * saves a season to the database
     * @param season the season to be saved
     */
    void save(Season season);
}
