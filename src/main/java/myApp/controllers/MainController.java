package main.java.myApp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.java.myApp.ScreenManager;
import main.java.myApp.controllers.movies.MovieCardController;
import main.java.myApp.controllers.movies.MovieDetailsController;
import main.java.myApp.controllers.series.SeriesCardController;
import main.java.myApp.controllers.series.SeriesDetailsController;
import main.java.myApp.model.Genre;
import main.java.myApp.model.Media;
import main.java.myApp.model.Movie;
import main.java.myApp.model.Series;
import main.java.myApp.service.MovieService;
import main.java.myApp.service.SearchService;
import main.java.myApp.service.SeriesService;
import main.java.myApp.util.Session;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private final SeriesService seriesService;
    private final MovieService movieService;
    private final SearchService searchService;
    private final ScreenManager screenManager;

    public MainController(MovieService movieService, SeriesService seriesService, SearchService searchService, ScreenManager screenManager) {
        this.movieService = movieService;
        this.seriesService = seriesService;
        this.searchService = searchService;
        this.screenManager = screenManager;
    }

    private static final int PAGE_SIZE = 6;
    private int moviePageIndex = 0;
    private int seriesPageIndex = 0;

    @FXML
    private HBox movieCardContainer;
    @FXML
    private HBox seriesCardContainer;
    @FXML
    private Button movieLeftArrow;
    @FXML
    private Button movieRightArrow;
    @FXML
    private Button seriesLeftArrow;
    @FXML
    private Button seriesRightArrow;
    @FXML
    private Label currentUserLabel;
    @FXML
    private Button contextMenuButton;
    @FXML
    private ChoiceBox<String> searchFilterChoiceBox;
    @FXML
    private StackPane searchInputContainer;

    // this node represents the current input of the search box
    private Node currentInput;

    private final List<Movie> movies = new ArrayList<>(); // from service
    private final List<Series> series = new ArrayList<>(); // from service

    // on init add all movies and series to the UI
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
    }

    // initialize this view by adding the movies and series on the screen, handling user label,
    // context menu,
    private void init() {
        // clear the arrays because this method is also used for refreshing.
        series.clear();
        movies.clear();
        List<Movie> db_movies = movieService.getAllMovies();
        List<Series> db_series = seriesService.getAllSeries();
        db_series.sort(Comparator.comparingDouble(Series::getUserRatingAverage).reversed());
        db_movies.sort(Comparator.comparingDouble(Movie::getUserRatingAverage).reversed());
        series.addAll(db_series);
        movies.addAll(db_movies);
        showMoviePage(0);
        showSeriesPage(0);
        currentUserLabel.setText("User: " + Session.getUser().getUsername());

        searchFilterChoiceBox.setOnAction(this::handleSearchFilter);
        // pre choose title
        searchFilterChoiceBox.setValue("Title");

        // make the "context menu" open with left click
        contextMenuButton.setOnAction(e -> {
            ContextMenu menu = contextMenuButton.getContextMenu();
            if (menu != null) {
                // Show below the button (you can change the positioning)
                menu.show(contextMenuButton, Side.BOTTOM, 0, 0);
            }
        });
    }

    // handles when search filter changes to also change the input field
    private void handleSearchFilter(ActionEvent actionEvent) {
        searchInputContainer.getChildren().clear();
        String filterType = searchFilterChoiceBox.getValue();
        switch (filterType) {
            case "Title" -> {
                TextField textField = new TextField();
                textField.setPromptText("Enter movie/series title" + filterType + "...");
                textField.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-background-radius: 6;");
                currentInput = textField;
            }
            case "Actor" , "Director"-> {
                TextField textField = new TextField();
                textField.setPromptText("Search for movie with a specific" + filterType + "...");
                textField.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-background-radius: 6;");
                currentInput = textField;
            }
            case "Genre" -> {
                ChoiceBox<Genre> genreBox = new ChoiceBox<>();
                genreBox.getItems().setAll(Genre.values());
                genreBox.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
                genreBox.getStylesheets().add(getClass().getResource("/main/resources/css/style.css").toExternalForm());
                currentInput = genreBox;
            }
            case "Min IMDb Rating", "Min User Rating" -> {
                Spinner<Double> ratingSpinner = new Spinner<>(0.0, 10.0, 5.0, 0.1);
                ratingSpinner.setEditable(true);
                ratingSpinner.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
                currentInput = ratingSpinner;
            }
            default -> {
                TextField fallback = new TextField();
                fallback.setPromptText("Search...");
                fallback.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-background-radius: 6;");
                currentInput = fallback;
            }
        }
        searchInputContainer.getChildren().add(currentInput);
    }
    // alias to refresh movies and series
    public void refresh() {
        init();
    }

    // handle the add buttons (open the relevant view as a modal from fxml)
    @FXML
    private void addNewDirector(ActionEvent e) {
        screenManager.showPopup("/main/resources/fxml/directors/addDirector.fxml", "Add Director");
    }
    @FXML
    private void addNewSeries(ActionEvent e) {
        screenManager.showPopup("/main/resources/fxml/series/addSeries.fxml", "Add Series");
        refresh();
    }
    @FXML
    private void addNewSeason(ActionEvent e) {
        screenManager.showPopup("/main/resources/fxml/series/addSeason.fxml", "Add Season");
    }
    @FXML
    private void addNewEpisode(ActionEvent e) {
        screenManager.showPopup("/main/resources/fxml/series/addEpisode.fxml", "Add episode");
    }
    @FXML
    private void addNewActor(ActionEvent e) {
        screenManager.showPopup("/main/resources/fxml/actors/addActor.fxml", "Add Actor");
    }
    @FXML
    private void addNewMovie(ActionEvent e) {
        screenManager.showPopup("/main/resources/fxml/movies/addMovie.fxml", "Add Movie");
        refresh();
    }

    /** MOVIE CARD */
    private Node createMovieCard(Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/movies/movieCard.fxml"));
            VBox card = loader.load();

            MovieCardController controller = loader.getController();
            controller.setData(movie);

            card.setOnMouseClicked(e -> openMovieDetail(movie));
            return card;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** SERIES CARD */
    private Node createSeriesCard(Series series) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/series/seriesCard.fxml"));
            VBox card = loader.load();

            SeriesCardController controller = loader.getController();
            controller.setData(series);

            card.setOnMouseClicked(e -> openSeriesDetail(series));
            return card;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // show ay movie page
    private void showMoviePage(int pageIndex) {
        movieCardContainer.getChildren().clear();
        int start = pageIndex * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, movies.size());

        for (int i = start; i < end; i++) {
            Movie movie = movies.get(i);
            movieCardContainer.getChildren().add(createMovieCard(movie));
        }

        // enable/disable arrows
        movieLeftArrow.setDisable(pageIndex == 0);
        movieRightArrow.setDisable(end >= movies.size());
    }
    private void showSeriesPage(int pageIndex) {
        seriesCardContainer.getChildren().clear();
        int start = pageIndex * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, series.size());

        for (int i = start; i < end; i++) {
            Series s = series.get(i);
            seriesCardContainer.getChildren().add(createSeriesCard(s));
        }

        seriesLeftArrow.setDisable(pageIndex == 0);
        seriesRightArrow.setDisable(end >= series.size());
    }

    @FXML
    private void onMovieLeft() {
        if (moviePageIndex > 0) {
            moviePageIndex--;
            showMoviePage(moviePageIndex);
        }
    }

    @FXML
    private void onMovieRight() {
        if ((moviePageIndex + 1) * PAGE_SIZE < movies.size()) {
            moviePageIndex++;
            showMoviePage(moviePageIndex);
        }
    }

    @FXML
    private void onSeriesLeft() {
        if (seriesPageIndex > 0) {
            seriesPageIndex--;
            showSeriesPage(seriesPageIndex);
        }
    }

    @FXML
    private void onSeriesRight() {
        if ((seriesPageIndex + 1) * PAGE_SIZE < series.size()) {
            seriesPageIndex++;
            showSeriesPage(seriesPageIndex);
        }
    }


    /** Navigation to detail pages */
    private void openMovieDetail(Movie movie) {
        screenManager.showScreen("/main/resources/fxml/movies/movieDetails.fxml", (MovieDetailsController c) -> c.displayMovie(movie));
    }

    private void openSeriesDetail(Series series) {
        screenManager.showScreen("/main/resources/fxml/series/seriesDetails.fxml", (SeriesDetailsController c) -> c.displaySeries(series));
    }
    // -- handle search button --
    @FXML
    private void onSearch(ActionEvent e) {
        String filter = searchFilterChoiceBox.getValue();

        switch (filter) {
            case "Title" -> {
                String query = ((TextField) currentInput).getText();
                if (query.isBlank()) {
                    return;
                }
                List<Media> searchResults = searchService.searchMedia(query);
                screenManager.showScreen("/main/resources/fxml/searchResults.fxml", (SearchResultsController c) -> c.setResults(searchResults));
            }
            case "Actor" -> {
                String query = ((TextField) currentInput).getText();
                if (query.isBlank()) {
                    return;
                }
                List<Media> searchResults = new ArrayList<>();
                searchResults.addAll(searchService.searchMovieByActor(query));
                screenManager.showScreen("/main/resources/fxml/searchResults.fxml", (SearchResultsController c) -> c.setResults(searchResults));
            }
            case "Director" -> {
                String query = ((TextField) currentInput).getText();
                if (query.isBlank()) {
                    return;
                }
                List<Media> searchResults = new ArrayList<>();
                searchResults.addAll(searchService.searchMovieByDirector(query));
                screenManager.showScreen("/main/resources/fxml/searchResults.fxml", (SearchResultsController c) -> c.setResults(searchResults));
            }
            case "Genre" -> {
                Genre genre = ((ChoiceBox<Genre>) currentInput).getValue();
                if (genre == null) {
                    return;
                }
                List<Media> searchResults = new ArrayList<>();
                searchResults.addAll(searchService.searchMovieByGenre(genre));
                searchResults.addAll(searchService.searchSeriesByGenre(genre));
                screenManager.showScreen("/main/resources/fxml/searchResults.fxml", (SearchResultsController c) -> c.setResults(searchResults));
            }
            case "Min IMDb Rating" -> {
                Double rating = ((Spinner<Double>) currentInput).getValue();
                List<Media> searchResults = new ArrayList<>();
                searchResults.addAll(searchService.searchMovieByImdbRating(rating));
                screenManager.showScreen("/main/resources/fxml/searchResults.fxml", (SearchResultsController c) -> c.setResults(searchResults));
            }
            case "Min User Rating" -> {
                Double rating = ((Spinner<Double>) currentInput).getValue();
                List<Media> searchResults = new ArrayList<>();
                searchResults.addAll(searchService.searchSeriesByUserRating(rating));
                searchResults.addAll(searchService.searchMovieByUserRating(rating));
                screenManager.showScreen("/main/resources/fxml/searchResults.fxml", (SearchResultsController c) -> c.setResults(searchResults));
            }
            default -> {
                // maybe fallback to title search or show error
            }
        }
    }

}