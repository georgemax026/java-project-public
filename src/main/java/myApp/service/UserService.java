package main.java.myApp.service;

import main.java.myApp.model.User;
import main.java.myApp.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    /**
     * Constructs a UserService with a given repository.
     * @param userRepository The data access layer for User.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /**
     * Retrieves a user by their unique ID.
     * @param id The ID of the user to find.
     * @return The found User object.
     * @throws UserNotFoundException if no user with the given ID exists.
     */
    public User getUserById(int id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID: " + id + " Not found"));
    }

    /**
     * Retrieves all Users from the repository.
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    /**
     * adds a new user to the system
     * @param firstName The first name of the new user
     * @param lastName The last name of the new user
     * @param username The username of the new user
     * @param email The username of the new user
     * @return The new user
     * @throws InvalidUserDataException if first name or last name is blank
     * @throws UserAlreadyExistsException if a user with this username already exists
     */
    public User addUser(String firstName, String lastName, String username, String email) {
        // check if the data is valid
        if (firstName.isEmpty() || lastName.isEmpty()) {
            throw new InvalidUserDataException("User must have a firstName and a lastName");
        }
        // try catch block for user throwing an exception about the email
        List<User> users = userRepository.findAll();
        Optional<User> existingUser = users.stream()
                .filter(currUser -> currUser.getUsername().equalsIgnoreCase(username))
                .findFirst();
        if  (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with username:  " + username + " already exists");
        } else {
            User newUser = new User(firstName, lastName, username, email);
            userRepository.save(newUser);
            return newUser;
        }
    }

    /**
     * retrieves a user by their given username
     * @param username the username of the user
     * @return the User with the given username
     * @throws UserNotFoundException if a user with the given username doesn't exist
     */
    public User getUserByUsername(String username) throws UserNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with username: " + username + " does not exist");
        } else {
            return user.get();
        }
    }
    /**
     * checks if a user with the same username already exists
     * @param username the username of the user to search
     * @return true if a user with the same username exists and false if not
     */
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }
}