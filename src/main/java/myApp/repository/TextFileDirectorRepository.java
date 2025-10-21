package main.java.myApp.repository;


import main.java.myApp.model.Director;
import main.java.myApp.model.Gender;

import java.time.LocalDate;
import java.util.*;

public class TextFileDirectorRepository extends TextFileRepository<Director> implements DirectorRepository {

    public TextFileDirectorRepository(String filePath) {
        super(filePath);
        this.nextId = calculateNextId();
    }

    /**
     * parses a single line from the db
     * @param line the line of the txt file to be parsed
     * @return returns the db line as a Director
     */
    @Override
    protected Director parseLine(String line) {
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
        writeAllLines(directors);
    }

    @Override
    public void save(Director director) {
        List<Director> directors = readAllLines();
        director.setId(nextId++);
        directors.add(director);
        writeAllLines(directors);
    }

    @Override
    public void deleteById(int id) {
        List<Director> directors = findAll();
        List<Director> updatedDirectors = directors.stream()
                .filter(director -> director.getId() != id)
                .toList();
        if (directors.size() > updatedDirectors.size()) {
            writeAllLines(updatedDirectors);
        } else {
            System.out.println("director with ID " + id + " not found. No deletion performed.");
        }
    }
    @Override
    public boolean existsByLastName(String lastName) {
        return findAll().stream().anyMatch(d -> d.getLastName().equalsIgnoreCase(lastName));
    }
}
