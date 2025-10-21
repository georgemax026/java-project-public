package main.java.myApp.repository;

import main.java.myApp.model.Season;

import java.io.*;
import java.time.Year;
import java.util.*;

public class TextFileSeasonRepository implements SeasonRepository {
    private int nextId;
    private final String filePath;

    public TextFileSeasonRepository(String filePath) {
        this.filePath = filePath;
        nextId = calculateNextId();
    }


    /**
     * calculates next available id
     * @return next available id
     */
    private int calculateNextId() {
        List<Season> seasons = findAll();
        return seasons.stream()
                .mapToInt(Season::getId)
                .max()
                .orElse(0) + 1;
    }

    /**
     * overwrites the db
     * @param seasons the list of seasons that will overwrite the db
     */
    private void writeAllLinesToFile(List<Season> seasons) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
            for (Season season : seasons) {
                writer.print(season.toDbString());
            }
        } catch (IOException e) {
            System.err.println("Error writing season to file: " + e.getMessage());
        }
    }
    private List<Season> readAllLines() {
        List<Season> seasons = new ArrayList<>();
        try (Scanner seasonScanner = new Scanner(new File(filePath))) {
            while (seasonScanner.hasNextLine()) {
                String line = seasonScanner.nextLine();
                if (line.trim().isEmpty()) { // Skip empty lines
                    continue;
                }
                try {
                    seasons.add(parseSeasonFromLine(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("malformed line in season file: " + line + " - Error: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("season database file not found: " + filePath);
        } catch (Exception e) {
            System.err.println("Error reading season from file: " + e.getMessage());
        }
        return seasons;
    }

    /**
     * parses a singular line from the database
     * @param line a line from the seasons db txt file
     * @return on object of type seasons made from the db info
     */
    private Season parseSeasonFromLine(String line) {
        String[] parts = line.split(",", 4);
        int seasonId = Integer.parseInt(parts[0]);
        int seriesId = Integer.parseInt(parts[1]);
        int seasonCount = Integer.parseInt(parts[2]);
        Year releaseYear = Year.of(Integer.parseInt(parts[3]));
        return new Season(seasonId, seriesId, seasonCount, releaseYear);
    }


    @Override
    public List<Season> findAll() {
        return readAllLines();
    }
    @Override
    public Optional<Season> findById(int id) {
        return findAll().stream().filter(season -> season.getId() == id).findFirst();
    }

    @Override
    public List<Season> findBySeriesId(int seriesId) {
        return findAll().stream().filter(season -> season.getSeriesId() == seriesId).toList();
    }

    @Override
    public Optional<Season> findSeasonBySeasonNumber(int seriesId, int seasonNumber) {
        return findAll().stream().filter(season -> (season.getSeriesId() == seriesId && season.getSeasonNumber() == seasonNumber)).findFirst();
    }

    @Override
    public void saveAll(List<Season> seasons) {
        writeAllLinesToFile(seasons);
    }

    @Override
    public void save(Season season) {
        List<Season> seasons = findAll();
        season.setId(nextId++);
        seasons.add(season);
        writeAllLinesToFile(seasons);
    }
}
