package main.java.myApp.repository;

import main.java.myApp.model.Episode;

import java.util.List;
import java.util.Optional;

public interface EpisodeRepository {
    /**
     * get a list of every episode from the database
     * @return a {@link List<Episode>} with every episode that exists in the database
     */
    List<Episode> findAll();
    /**
     * Finds an episode by its unique ID.
     * @param id the id of the episode
     * @return an {@link Optional} containing the episode if found, otherwise empty
     */
    Optional<Episode> findById(int id);
    /**
     * Finds all episodes associated with a specific season.
     * @param seasonId The ID of the parent season.
     * @return A {@link List<Episode>} of episodes for that season.
     */
    List<Episode> findBySeasonId(int seasonId);
    /**
     * saves an episode to the database
     * @param episode the actor to be saved
     */
    void save(Episode episode);
}
