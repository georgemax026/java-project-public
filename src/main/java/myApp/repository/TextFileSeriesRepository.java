package main.java.myApp.repository;

import main.java.myApp.model.Genre;
import main.java.myApp.model.Series;

import java.io.*;
import java.util.*;

public class TextFileSeriesRepository implements SeriesRepository {
    private int nextId;
    private final String filePath;

    public TextFileSeriesRepository(String filePath) {
        this.filePath = filePath;
        nextId = calculateNextId();
    }


    /**
     * calculates next available id
     * @return next available id
     */
    private int calculateNextId() {
        List<Series> series = findAll();
        return series.stream()
                .mapToInt(Series::getId)
                .max()
                .orElse(0) + 1;
    }
    private List<Series> readAllLines() {
        List<Series> series = new ArrayList<>();
        try (Scanner seriesScanner = new Scanner(new File(filePath))) {
            while (seriesScanner.hasNextLine()) {
                String line = seriesScanner.nextLine();
                if (line.isBlank()) { // Skip empty lines
                    continue;
                }
                try {
                    series.add(parseSeriesFromLine(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("malformed line in series file: " + line + " - Error: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("series database file not found: " + filePath);
        } catch (Exception e) {
            System.err.println("Error reading series from file: " + e.getMessage());
        }
        return series;
    }

    /**
     * parses a singular line from the database
     * @param line a line from the series db txt file
     * @return on object of type Series made from the db info
     */
    private Series parseSeriesFromLine(String line) {
        String[] parts = line.split(",", 4);
        int id = Integer.parseInt(parts[0]);
        // check if series has user ratings
        if (parts[3].trim().isEmpty()) {
            return new Series(id, parts[1], Genre.fromDbString(parts[2]), new HashMap<Integer, Integer>());
        } else {
            // split and manage the user ratings
            String[] userRatingString = parts[3].trim().split("\\|");
            Map<Integer, Integer> userRating = new HashMap<>();
            for (String rating : userRatingString) {
                String[] ratingData = rating.split(":", 2);
                userRating.put(Integer.parseInt(ratingData[0]), Integer.parseInt(ratingData[1]));
            }
            return new Series(id, parts[1], Genre.fromDbString(parts[2]), userRating);
        }
    }
    /**
     * overwrites the db
     * @param seriesList the list of series that will overwrite the db
     */
    private void writeAllLinesToFile(List<Series> seriesList) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
            for (Series series : seriesList) {
                writer.print(series.toDbString());
            }
        } catch (IOException e) {
            System.err.println("Error writing series to file: " + e.getMessage());
        }
    }


    // SeriesRepository interface implementation
    @Override
    public List<Series> findAll() {
        return readAllLines();
    }

    @Override
    public Optional<Series> findById(int id) {
        return findAll().stream().filter(series -> series.getId() == id).findFirst();
    }

    @Override
    public Optional<Series> findByTitle(String title) {
        return findAll().stream().filter(series -> series.getTitle().equalsIgnoreCase(title)).findFirst();
    }

    @Override
    public void save(Series series) {
        List<Series> seriesList = findAll();
        series.setId(nextId++);
        seriesList.add(series);
        writeAllLinesToFile(seriesList);
    }

    @Override
    public void modifyRating(int seriesId, int user, int rating) {
        List<Series> seriesList = findAll();
        Optional<Series> toModify = seriesList.stream().filter(series -> series.getId() == seriesId).findFirst();
        if (toModify.isPresent()) {
            // modify rating
            Series series = toModify.get();
            series.setUserRating(user, rating);
        } else {
            throw new IllegalArgumentException("Series with id: " + seriesId + " not found");
        }
        writeAllLinesToFile(seriesList);
    }
    @Override
    public boolean existsByTitle(String title) {
        return findAll().stream().anyMatch(series -> series.getTitle().equalsIgnoreCase(title));
    }
}
