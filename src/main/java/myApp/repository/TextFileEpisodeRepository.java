package main.java.myApp.repository;

import main.java.myApp.model.Episode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class TextFileEpisodeRepository implements EpisodeRepository{
    private int nextId;
    private final String filePath;

    public TextFileEpisodeRepository(String filePath) {
        this.filePath = filePath;
        nextId = calculateNextId();
    }



    private int calculateNextId() {
        List<Episode> episodes = findAll();
        return episodes.stream()
                .mapToInt(Episode::getId)
                .max()
                .orElse(0) + 1;
    }

    /**
     * overwrites the db
     * @param episodes the list of episode that will overwrite the db
     */
    private void writeAllLinesToFile(List<Episode> episodes) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
            for (Episode episode : episodes) {
                writer.print(episode.toDbString());
            }
        } catch (IOException e) {
            System.err.println("Error writing episode to file: " + e.getMessage());
        }
    }
    private List<Episode> readAllLines() {
        List<Episode> episodes = new ArrayList<>();
        try (Scanner episodeScanner = new Scanner(new File(filePath))) {
            while (episodeScanner.hasNextLine()) {
                String line = episodeScanner.nextLine();
                if (line.isBlank()) { // Skip empty lines
                    continue;
                }
                try {
                    episodes.add(parseEpisodeFromLine(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("malformed line in episode file: " + line + " - Error: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Episode database file not found: " + filePath);
        } catch (Exception e) {
            System.err.println("Error reading episode from file: " + e.getMessage());
        }
        return episodes;
    }

    /**
     * parses a singular line from the database
     * @param line a line from the episodes db txt file
     * @return on object of type episodes made from the db info
     */
    private Episode parseEpisodeFromLine(String line) {
        String[] parts = line.split(",", 6);
        int episodeId = Integer.parseInt(parts[0]);
        int seasonId = Integer.parseInt(parts[1]);
        int duration = Integer.parseInt(parts[2]);
        double imdbRating = Double.parseDouble(parts[4]);
        return new Episode(episodeId, seasonId, duration, parts[3], imdbRating, parts[5]);
    }


    @Override
    public List<Episode> findAll() {
        return readAllLines();
    }

    @Override
    public Optional<Episode> findById(int id) {
        return findAll().stream().filter(episode -> episode.getId() == id).findFirst();
    }

    @Override
    public List<Episode> findBySeasonId(int seasonId) {
        return findAll().stream().filter(episode -> episode.getSeasonId() == seasonId).toList();
    }

    @Override
    public void save(Episode episode) {
        List<Episode> episodes = findAll();
        episode.setId(nextId++);
        episodes.add(episode);
        writeAllLinesToFile(episodes);
    }
}
