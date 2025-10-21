package main.java.myApp.repository;

import main.java.myApp.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorRepository {
    /**
     * get a list of every director from the database
     * @return a {@link List<Director>} with every director that exists in the database
     */
    List<Director> findAll();
    /**
     * Finds a director by their unique ID.
     * @param id the id of the director
     * @return an {@link Optional} containing the director if found, otherwise empty
     */
    Optional<Director> findById(int id);
    /**
     * Finds an actor by their lastname.
     * @param lastname the lastname of the director
     * @return an {@link Optional} containing the director if found, otherwise empty
     */
    Optional<Director> findByName(String lastname);
    /**
     * saves a director to the database
     * @param director the actor to be saved
     */
    void save(Director director);
    /**
     * saves a list of directors to the database
     * @param directors the {@link List<Director>} of movies to be saved
     */
    void saveAll(List<Director> directors);
    /**
     * deletes the director with the specific id form the database
     * @param id the ID of the director to be deleted
     */
    void deleteById(int id);
    /**
     * checks if a director with the same lastname exists
     * @param lastname the last name of the director
     * @return true if a director with that lastname already exists and false if not
     */
    boolean existsByLastName(String lastname);
}
