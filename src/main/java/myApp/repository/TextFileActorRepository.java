package main.java.myApp.repository;

import main.java.myApp.model.Actor;
import main.java.myApp.model.Gender;
import main.java.myApp.model.Race;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class TextFileActorRepository implements ActorRepository {
    private final String filePath;
    private int nextId;


    public TextFileActorRepository(String filePath) {
        this.filePath = filePath;
        this.nextId = calculateNextId();
    }

    /**
     * Calculates the next available ID
     * the "nextId" is the id number after the last id
     * @return the next available id
     */
    private int calculateNextId() {
        List<Actor> actors = findAll();
        return actors.stream()
                .mapToInt(Actor::getId)
                .max()
                .orElse(0) + 1;
    }

    /**
     * read all lines from the db
     * @return the list of actors from the db
     */
    private List<Actor> readAllLines() {
        List<Actor> actors = new ArrayList<>();
        try (Scanner actorScanner = new Scanner(new File(filePath))) {
            while (actorScanner.hasNextLine()) {
                String line = actorScanner.nextLine();
                if (line.isBlank()) { // Skip empty lines
                    continue;
                }
                try {
                    actors.add(parseActorFromLine(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("malformed line in actors file: " + line + " - Error: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Actor database file not found: " + filePath);
        } catch (Exception e) {
            System.err.println("Error reading actors from file: " + e.getMessage());
        }
        return actors;
    }

    /**
     * Overwrites the txt file
     * @param actors the list that's going to overwrite the db
     */
    private void writeAllLinesToFile(List<Actor> actors) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
            for (Actor actor : actors) {
                writer.print(actor.toDbString());
            }
        } catch (IOException e) {
            System.err.println("Error writing actors to file: " + e.getMessage());
        }
    }

    /**
     * parses a single line from the db
     * @param line the line of the txt file to be parsed
     * @return returns the db line as an Actor
     */
    private Actor parseActorFromLine(String line) {
        String[] parts = line.split(",", 6);
        if (parts.length < 6) {
            throw new IllegalArgumentException("Invalid actor data format: " + line);
        }
        int id = Integer.parseInt(parts[0]);
        String[] dateStrings = parts[3].split("-");
        int[] dateParts = {Integer.parseInt(dateStrings[0]), Integer.parseInt(dateStrings[1]), Integer.parseInt(dateStrings[2])};
        LocalDate birthDate = LocalDate.of(dateParts[0], dateParts[1], dateParts[2]);
        return new Actor(id, parts[1], parts[2], birthDate, Gender.fromDbString(parts[4]), Race.fromDbString(parts[5]));
    }

    // ActorRepository Interface Implementations
    @Override
    public List<Actor> findAll() {
        return readAllLines();
    }

    @Override
    public Optional<Actor> findById(int id) {
        return findAll().stream().filter(actor -> actor.getId() == id).findFirst();
    }

    @Override
    public Optional<Actor> findByName(String lastName) {
        return findAll()
                .stream()
                .filter(actor -> actor.getLastName().equalsIgnoreCase(lastName)).findAny();
    }

    @Override
    public void save(Actor actor) {
        List<Actor> actors = findAll();
        actor.setId(nextId++);
        actors.add(actor);
        writeAllLinesToFile(actors);
    }

    @Override
    public void saveAll(List<Actor> actors) {
        writeAllLinesToFile(actors);
    }


    @Override
    public void deleteById(int id) {
        List<Actor> actors = findAll();
        List<Actor> updatedActors = actors.stream()
                .filter(actor -> actor.getId() != id)
                .toList();
        if (updatedActors.size() < actors.size()) {
            writeAllLinesToFile(updatedActors);
        } else {
            System.out.println("Actor with ID " + id + " not found. No deletion performed.");
        }
    }
    @Override
    public boolean existsByLastName(String lastName) {
        return findAll().stream().anyMatch(actor -> actor.getLastName().equalsIgnoreCase(lastName));
    }
}
