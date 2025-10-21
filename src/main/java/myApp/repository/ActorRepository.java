package main.java.myApp.repository;

import main.java.myApp.model.Actor;

import java.util.List;
import java.util.Optional;

public interface ActorRepository {
    /**
     * get all the actors from the database
     * @return a {@link List<Actor>} with every actor from the database
     */
    List<Actor> findAll();
    /**
     * Finds an actor by their unique ID.
     * @param id the id of the actor
     * @return an {@link Optional} containing the actor if found, otherwise empty
     */
    Optional<Actor> findById(int id);
    /**
     * Finds an actor by their lastname.
     * @param lastname the lastname of the actor
     * @return an {@link Optional} containing the actor if found, otherwise empty
     */
    Optional<Actor> findByName(String lastname);
    /**
     * saves an actor to the database
     * @param actor the actor to be saved
     */
    void save(Actor actor);
    /**
     * saves a list of actors to the database
     * @param actors the {@link List<Actor>} of actor to be saved
     */
    void saveAll(List<Actor> actors);
    /**
     * deletes the actor with the specific id form the database
     * @param id the ID of the actor to be deleted
     */
    void deleteById(int id);
    /**
     * checks if an actor with the same lastname exists
     * @param lastname the last name of the actor
     * @return true if an actor with that lastname already exists and false if not
     */
    boolean existsByLastName(String lastname);
}
