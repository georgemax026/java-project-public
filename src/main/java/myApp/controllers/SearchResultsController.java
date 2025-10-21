package main.java.myApp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.myApp.ScreenManager;
import main.java.myApp.controllers.movies.MovieDetailsController;
import main.java.myApp.controllers.series.SeriesDetailsController;
import main.java.myApp.model.Media;
import main.java.myApp.model.Movie;
import main.java.myApp.model.Series;

import java.util.List;

public class SearchResultsController {
    private final ScreenManager screenManager;

    public SearchResultsController(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }


    @FXML
    private VBox resultsContainer;

    public void setResults(List<Media> results) {
        resultsContainer.getChildren().clear();

        for (Media media : results) {
            HBox card = createResultCard(media);
            resultsContainer.getChildren().add(card);
        }
    }

    private HBox createResultCard(Media media) {
        HBox card = new HBox(15);
        card.setStyle("-fx-background-color: #2a2a2a; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");
        card.setPrefHeight(60);

        // on mouse hover
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #3a3a3a; -fx-padding: 10; -fx-background-radius: 6;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #2a2a2a; -fx-padding: 10; -fx-background-radius: 6;"));

        card.setOnMouseClicked(e -> {
            if  (media instanceof Movie) {
                screenManager.showScreen("/main/resources/fxml/movies/movieDetails.fxml", (MovieDetailsController c) -> c.displayMovie((Movie) media));
            } else {
                screenManager.showScreen("/main/resources/fxml/series/seriesDetails.fxml", (SeriesDetailsController c) -> c.displaySeries((Series) media));
            }
        });

        // Title label
        Label titleLabel = new Label(media.getTitle());
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold;");

        // Ratings label
        String imdbPart = (media instanceof Movie m)
                ? String.format("IMDb: %.1f | ", m.getImdbRating())
                : "";
        String type = (media instanceof Movie) ? "Movie" : "Series";

        Label detailsLabel = new Label(String.format(
                "%sUser Rating: %.1f | %s",
                imdbPart,
                media.getUserRatingAverage(),
                type
        ));
        detailsLabel.setStyle("-fx-text-fill: #ccc; -fx-font-size: 14;");

        card.getChildren().addAll(titleLabel, detailsLabel);
        return card;
    }
    // on back return to the main view
    @FXML
    private void onBack(ActionEvent e) {
        screenManager.showScreen("/main/resources/fxml/main.fxml");
    }
}
