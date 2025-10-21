package main.java.myApp.repository;

import main.java.myApp.model.User;

import java.util.*;

public class TextFileUserRepository extends TextFileRepository<User> implements UserRepository{

    public TextFileUserRepository(String filePath) {
        super(filePath);
        nextId = calculateNextId();
    }


    @Override
    protected User parseLine(String line) {
        String[] parts = line.split(",", 5);
        int id = Integer.parseInt(parts[0]);
        return new User(id, parts[1], parts[2], parts[3], parts[4]);
    }

    // UserRepository interface implementation
    @Override
    public List<User> findAll() {
        return readAllLines();
    }

    @Override
    public Optional<User> findById(int id) {
        return findAll().stream().filter(user -> user.getId() == id).findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findAll().stream().filter(user -> user.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    @Override
    public void save(User user) {
        List<User> users = findAll();
        user.setId(nextId++);
        users.add(user);
        writeAllLines(users);
    }

    @Override
    public void saveAll(List<User> user) {
        writeAllLines(user);
    }

    @Override
    public void deleteById(int id) {
        List<User> users = findAll();
        List<User> updatedUsers = users.stream()
                .filter(user -> user.getId() != id)
                .toList();
        if (users.size() > updatedUsers.size()) {
            writeAllLines(updatedUsers);
        } else {
            System.out.println("user with ID " + id + " not found. No deletion performed.");
        }
    }
    @Override
    public boolean existsByUsername(String username) {
        return findAll().stream().anyMatch(user -> user.getUsername().equalsIgnoreCase(username));
    }
}
