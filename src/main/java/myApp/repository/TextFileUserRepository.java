package main.java.myApp.repository;

import main.java.myApp.model.User;

import java.io.*;
import java.util.*;

public class TextFileUserRepository implements UserRepository{
    private final String filePath;
    private int nextId;


    public TextFileUserRepository(String filePath) {
        this.filePath = filePath;
        nextId = calculateNextId();
    }


    /**
     * calculates next available id
     * @return next available id
     */
    private int calculateNextId() {
        List<User> users = findAll();
        return users.stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;
    }
    private List<User> readAllLines() {
        List<User> users = new ArrayList<>();

        try (Scanner userScanner = new Scanner(new File(filePath))) {
            while (userScanner.hasNextLine()) {
                String line = userScanner.nextLine();
                if (line.trim().isEmpty()) { // Skip empty lines
                    continue;
                }
                try {
                    users.add(parseUserFromLine(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("malformed line in users file: " + line + " - Error: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("user database file not found: " + filePath);
        } catch (Exception e) {
            System.err.println("Error reading users from file: " + e.getMessage());
        }
        return users;
    }

    private User parseUserFromLine(String line) {
        String[] parts = line.split(",", 5);
        int id = Integer.parseInt(parts[0]);
        return new User(id, parts[1], parts[2], parts[3], parts[4]);
    }

    private void writeAllLinesToFile(List<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
            for (User user : users) {
                writer.print(user.toDbString());
            }
        } catch (IOException e) {
            System.err.println("Error writing users to file: " + e.getMessage());
        }
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
        writeAllLinesToFile(users);
    }

    @Override
    public void saveAll(List<User> user) {
        writeAllLinesToFile(user);
    }

    @Override
    public void deleteById(int id) {
        List<User> users = findAll();
        List<User> updatedUsers = users.stream()
                .filter(user -> user.getId() != id)
                .toList();
        if (users.size() > updatedUsers.size()) {
            writeAllLinesToFile(updatedUsers);
        } else {
            System.out.println("user with ID " + id + " not found. No deletion performed.");
        }
    }
    @Override
    public boolean existsByUsername(String username) {
        return findAll().stream().anyMatch(user -> user.getUsername().equalsIgnoreCase(username));
    }
}
