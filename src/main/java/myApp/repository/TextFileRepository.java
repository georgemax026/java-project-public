package main.java.myApp.repository;

import main.java.myApp.model.RepositoryRequirements;

import java.io.*;
import java.util.*;

abstract public class TextFileRepository<T extends RepositoryRequirements> {
    protected String filePath;
    protected int nextId;

    public TextFileRepository(String filePath) {
        this.filePath = filePath;
    }
    /**
     * calculates next available id
     * @return next available id
     */
    protected int calculateNextId() {
        List<T> list = readAllLines();
        return list.stream()
                .mapToInt(T::getId)
                .max()
                .orElse(0) + 1;
    }
    /**
     * overwrites the db with the given list
     * @param objList the list of objects that will be used to overwrite the DB
     */
    // todo: throw some type of exceptions if this fails to open a file or whatever
    protected void writeAllLines(List<T> objList) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
            for (T obj : objList) {
                writer.print(obj.toDbString());
            }
        } catch (IOException e) {
            System.err.println("Error writing movies to file: " + e.getMessage());
        }
    }
    /**
     * reads all the lines from the db and returns a list of the object (T)
     * @return the list of T from the db
     */
    // todo: throw runtime exceptions if unable to open a txt file
    protected List<T> readAllLines() {
        List<T> results = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isBlank()) { // Skip empty lines
                    continue;
                }
                try {
                    results.add(parseLine(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("malformed line in + " + filePath + ": " + line + " - Error: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("database file not found: " + filePath);
        } catch (Exception e) {
            System.err.println("Error reading something from file: " + filePath);
        }
        return results;
    }
    /**
     * This function is supposed to be overwritten by the children classes and adapted to the needs of each txt "db"
     * @param line a line from the db txt file
     * @return on object of type T made from the db info
     */
    protected abstract T parseLine(String line);
}
