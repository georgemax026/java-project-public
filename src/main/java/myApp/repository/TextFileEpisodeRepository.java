package main.java.myApp.repository;

import main.java.myApp.model.Episode;

import java.util.List;
import java.util.Optional;

public class TextFileEpisodeRepository extends TextFileRepository<Episode> implements EpisodeRepository {

    public TextFileEpisodeRepository(String filePath) {
        super(filePath);
        nextId = calculateNextId();
    }

    /**
     * parses a singular line from the database
     * @param line a line from the episodes db txt file
     * @return on object of type episodes made from the db info
     */
    @Override
    protected Episode parseLine(String line) {
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
        writeAllLines(episodes);
    }
}
