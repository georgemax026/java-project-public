package main.java.myApp.repository;

import main.java.myApp.model.Genre;
import main.java.myApp.model.Movie;

import java.io.*;
import java.time.Year;
import java.util.*;

public class TextFileMovieRepository implements MovieRepository{
    private int nextId;
    private final String filePath;

    public TextFileMovieRepository(String filePath) {
        this.filePath = filePath;
        nextId = calculateNextId();
    }

    /**
     * calculates next available id
     * @return next available id
     */
    private int calculateNextId() {
        List<Movie> movies = findAll();
        return movies.stream()
                .mapToInt(Movie::getId)
                .max()
                .orElse(0) + 1;
    }
    private List<Movie> readAllLines() {
        List<Movie> movies = new ArrayList<>();
        try (Scanner movieScanner = new Scanner(new File(filePath))) {
            while (movieScanner.hasNextLine()) {
                String line = movieScanner.nextLine();
                if (line.isBlank()) { // Skip empty lines
                    continue;
                }
                try {
                    movies.add(parseMovieFromLine(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("malformed line in director file: " + line + " - Error: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Actor database file not found: " + filePath);
        } catch (Exception e) {
            System.err.println("Error reading users from file: " + e.getMessage());
        }
        return movies;
    }

    /**
     * parses a singular line from the database
     * @param line a line from the movie db txt file
     * @return on object of type Movie made from the db info
     */
    private Movie parseMovieFromLine(String line) {
        String[] parts = line.split(",", 9);
        int id = Integer.parseInt(parts[0]);
        Year releaseYear = Year.of(Integer.parseInt(parts[2]));
        int length = Integer.parseInt(parts[4]);
        double imdbRating = Double.parseDouble(parts[6]);
        // check if movies has no user ratings to avoid errors
        if (parts[8].trim().isEmpty()) {
            return new Movie(id, parts[1], releaseYear, Genre.fromDbString(parts[3]), length, parts[5], imdbRating, parts[7], new HashMap<Integer, Integer>());
        } else {
            String[] userRatingString = parts[8].trim().split("\\|");
            // save each user's rating to a Map
            Map<Integer, Integer> userRating = new HashMap<>();
            for (String rating : userRatingString) {
                String[] ratingData = rating.split(":", 2);
                userRating.put(Integer.parseInt(ratingData[0]), Integer.parseInt(ratingData[1]));
            }
            return new Movie(id, parts[1], releaseYear, Genre.fromDbString(parts[3]), length, parts[5], imdbRating, parts[7], userRating);
        }
    }

    /**
     * overwrites the db
     * @param movies the list of movies that will overwrite the db
     */
    private void writeAllLines(List<Movie> movies) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
            for (Movie movie : movies) {
                writer.print(movie.toDbString());
            }
        } catch (IOException e) {
            System.err.println("Error writing movies to file: " + e.getMessage());
        }
    }


    // MovieRepository interface implementation
    @Override
    public List<Movie> findAll() {
        return readAllLines();
    }

    @Override
    public Optional<Movie> findById(int id) {
        return findAll().stream().filter(movie -> movie.getId() == id).findFirst();
    }

    @Override
    public Optional<Movie> findByTitle(String title) {
        return findAll().stream().filter(movie -> movie.getTitle().equalsIgnoreCase(title)).findFirst();
    }

    @Override
    public void save(Movie movie) {
        List<Movie> movies = findAll();
        movie.setId(nextId++);
        movies.add(movie);
        writeAllLines(movies);
    }

    @Override
    public void saveAll(List<Movie> movies) {
        writeAllLines(movies);
    }

    @Override
    public void deleteById(int id) {
        List<Movie> movies = findAll();
        List<Movie> updatedMovies = movies.stream()
                .filter(movie -> movie.getId() != id)
                .toList();
        if (movies.size() > updatedMovies.size()) {
            writeAllLines(updatedMovies);
        } else {
            System.out.println("Movie with ID " + id + " not found. No deletion performed.");
        }
    }

    @Override
    public void modifyRating(int movieId, int user, int rating) {
        List<Movie> movies = findAll();
        Optional<Movie> toModify = movies.stream().filter(movie -> movie.getId() == movieId).findFirst();
        if (toModify.isPresent()) {
            // modify rating
            Movie movie = toModify.get();
            movie.setUserRating(user, rating);
        } else {
            throw new IllegalArgumentException("Movie with id: " + movieId + " not found");
        }
        writeAllLines(movies);
    }
    @Override
    public boolean existsByTitle(String title) {
        return findAll().stream().anyMatch(m -> m.getTitle().equalsIgnoreCase(title));
    }
}
