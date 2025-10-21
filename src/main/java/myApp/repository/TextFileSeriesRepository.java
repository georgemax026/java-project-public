package main.java.myApp.repository;

import main.java.myApp.model.Genre;
import main.java.myApp.model.Series;

import java.util.*;

public class TextFileSeriesRepository extends TextFileRepository<Series> implements SeriesRepository {

    public TextFileSeriesRepository(String filePath) {
        super(filePath);
        nextId = calculateNextId();
    }


    /**
     * parses a singular line from the database
     * @param line a line from the series db txt file
     * @return on object of type Series made from the db info
     */
    @Override
    protected Series parseLine(String line) {
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
        writeAllLines(seriesList);
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
        writeAllLines(seriesList);
    }
    @Override
    public boolean existsByTitle(String title) {
        return findAll().stream().anyMatch(series -> series.getTitle().equalsIgnoreCase(title));
    }
}
