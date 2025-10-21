package main.java.myApp.repository;


import main.java.myApp.model.Director;
import main.java.myApp.model.Gender;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class TextFileDirectorRepository implements DirectorRepository {
    private final String filePath;
    private int nextId;

    public TextFileDirectorRepository(String filePath) {
        this.filePath = filePath;
        initializeDatabaseFile();
        this.nextId = calculateNextId();
    }

    private void initializeDatabaseFile() {
    }
    /**
     * Calculates the next available ID
     * the "nextId" is the id number after the last id
     * @return the next available id
     */
    private int calculateNextId() {
        List<Director> directors = findAll();
        return directors.stream()
                .mapToInt(Director::getId)
                .max()
                .orElse(0) + 1;
    }
    /**
     * read all lines from the db
     * @return the list of directors from the db
     */
    private List<Director> readAllLines() {
        List<Director> directors = new ArrayList<>();

        try (Scanner directorScanner = new Scanner(new File(filePath))) {
            while (directorScanner.hasNextLine()) {
                String line =  directorScanner.nextLine();
                if (line.isBlank()) { // Skip empty lines
                    continue;
                }
                try {
                    directors.add(parseDirectorFromLine(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("malformed line in director file: " + line + " - Error: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("director database file not found: " + filePath);
        } catch (Exception e) {
            System.err.println("Error reading director from file: " + e.getMessage());
        }
        return directors;
    }
    /**
     * Overwrites the txt file
     * @param directors the list that's going to overwrite the db
     */
    private void writeAllLinesToFile(List<Director> directors) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
            for (Director director : directors) {
                writer.print(director.toDbString());
            }
        } catch (IOException e) {
            System.err.println("Error writing director to file: " + e.getMessage());
        }
    }
    /**
     * parses a single line from the db
     * @param line the line of the txt file to be parsed
     * @return returns the db line as a Director
     */
    private Director parseDirectorFromLine(String line) {
        String[] parts = line.split(",", 6);
        if (parts.length < 6) {
            throw new IllegalArgumentException("Invalid Director data format: " + line);
        }
        int id = Integer.parseInt(parts[0]);
        ArrayList<String> bestOf = new ArrayList<>();
        // separate tmp[4] which is Director's best work by "|"
        String[] bestOfArr = parts[5].split("\\|");
        Collections.addAll(bestOf, bestOfArr);
        // make dates
        String[] dateStrings = parts[3].split("-");
        int[] dateInts = {Integer.parseInt(dateStrings[0]), Integer.parseInt(dateStrings[1]), Integer.parseInt(dateStrings[2])};
        LocalDate birthDate = LocalDate.of(dateInts[0], dateInts[1], dateInts[2]);
        // make and return the director
        return new Director(id, parts[1], parts[2], birthDate, Gender.fromDbString(parts[4]), bestOf);
    }

    // DirectorRepository interface implementation
    @Override
    public List<Director> findAll() {
        return readAllLines();
    }

    @Override
    public Optional<Director> findByName(String lastName) {
        return findAll().stream().filter(d -> d.getLastName().equals(lastName)).findFirst();
    }

    @Override
    public Optional<Director> findById(int id) {
        return findAll().stream().filter(d -> d.getId() == id).findFirst();
    }

    @Override
    public void saveAll(List<Director> directors) {
        List<Director> directorsToSave = new ArrayList<>();
        writeAllLinesToFile(directors);
    }

    @Override
    public void save(Director director) {
        List<Director> directors = readAllLines();
        director.setId(nextId++);
        directors.add(director);
        writeAllLinesToFile(directors);
    }

    @Override
    public void deleteById(int id) {
        List<Director> directors = findAll();
        List<Director> updatedDirectors = directors.stream()
                .filter(director -> director.getId() != id)
                .toList();
        if (directors.size() > updatedDirectors.size()) {
            writeAllLinesToFile(updatedDirectors);
        } else {
            System.out.println("director with ID " + id + " not found. No deletion performed.");
        }
    }
    @Override
    public boolean existsByLastName(String lastName) {
        return findAll().stream().anyMatch(d -> d.getLastName().equalsIgnoreCase(lastName));
    }
}
