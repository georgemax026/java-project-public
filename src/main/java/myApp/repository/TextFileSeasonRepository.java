package main.java.myApp.repository;

import main.java.myApp.model.Season;

import java.time.Year;
import java.util.*;

public class TextFileSeasonRepository extends TextFileRepository<Season> implements SeasonRepository {

    public TextFileSeasonRepository(String filePath) {
        super(filePath);
        nextId = calculateNextId();
    }

    /**
     * parses a singular line from the database
     * @param line a line from the seasons db txt file
     * @return on object of type seasons made from the db info
     */
    @Override
    protected Season parseLine(String line) {
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
        writeAllLines(seasons);
    }

    @Override
    public void save(Season season) {
        List<Season> seasons = findAll();
        season.setId(nextId++);
        seasons.add(season);
        writeAllLines(seasons);
    }
}
