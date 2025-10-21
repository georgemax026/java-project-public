package main.java.myApp.repository;

import main.java.myApp.model.User;

import java.util.List;
import java.util.Optional;


public interface UserRepository {
    /**
     * get a list of every user from the database
     * @return a {@link List<User>} with every user that exists in the database
     */
    List<User> findAll();
    /**
     * Finds a user by their unique ID.
     * @param id the id of the user
     * @return an {@link Optional} containing the user if found, otherwise empty
     */
    Optional<User> findById(int id);
    /**
     * Finds a user by their username.
     * @param username the username of the user
     * @return an {@link Optional} containing the user if found, otherwise empty
     */
    Optional<User> findByUsername(String username);
    /**
     * saves a user to the database
     * @param user the user to be saved
     */
    void save(User user);
    /**
     * saves a list of users to the database
     * @param user a {@link List<User>} of the users to be saved
     */
    void saveAll(List<User> user);
    /**
     * delete a specific user
     * @param id the id of the user to be deleted
     */
    void deleteById(int id);
    /**
     * checks if a user with the same username already exists
     * @param username the username of the user to search
     * @return true if a user with the same username exists and false if not
     */
    boolean existsByUsername(String username);
}
