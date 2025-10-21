package main.java.myApp.controllers.series;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import main.java.myApp.ScreenManager;
import main.java.myApp.controllers.actors.ActorDetailsController;
import main.java.myApp.controllers.directors.DirectorDetailsController;
import main.java.myApp.model.*;
import main.java.myApp.service.SearchService;
import main.java.myApp.service.SeriesService;

import java.util.List;

public class EpisodeDetailsController {
    private final SeriesService seriesService;
    private final SearchService searchService;
    private final ScreenManager screenManager;

    public EpisodeDetailsController(SeriesService seriesService, SearchService searchService, ScreenManager screenManager) {
        this.seriesService = seriesService;
        this.searchService = searchService;
        this.screenManager = screenManager;
    }

    @FXML
    private Label titleLabel;
    @FXML
    private Label seasonLabel;
    @FXML
    private Label directorLabel;
    @FXML
    private Label protagonistLabel;
    @FXML
    private Label ratingLabel;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label durationLabel;

    // method to add the episode details to the view
    public void displayEpisodeDetails(Episode episode) {
        Season currSeason = seriesService.getSeasonById(episode.getSeasonId());
        Series currSeries = seriesService.getSeriesById(currSeason.getSeriesId());
        String[] actorNames = episode.getProtagonist().split(" ");
        String[] directorNames = episode.getDirector().split(" ");
        List<Actor> actor = searchService.searchActorByName(actorNames[1]);
        List<Director> director = searchService.searchDirectorByName(directorNames[1]);

        if (actor.isEmpty()) {
            protagonistLabel.setText(episode.getProtagonist());
        } else {
            gridPane.getChildren().remove(protagonistLabel);
            Hyperlink actorLink = new Hyperlink(episode.getProtagonist());
            actorLink.setStyle("-fx-text-fill: white");
            actorLink.setOnAction(event -> {
                screenManager.showPopup("/main/resources/fxml/actors/actorDetails.fxml", "Actor Details", (ActorDetailsController c) -> c.displayDetails(actor.getFirst()));
            });
            gridPane.add(actorLink, 1, 5);
        }
        if (director.isEmpty()) {
            directorLabel.setText(episode.getDirector());
        } else {
            gridPane.getChildren().remove(directorLabel);
            Hyperlink directorLink = new Hyperlink(episode.getDirector());
            directorLink.setStyle("-fx-text-fill: white");
            directorLink.setOnAction(event -> {
                screenManager.showPopup("/main/resources/fxml/directors/directorDetails.fxml", "Director Details", (DirectorDetailsController c) -> c.displayDetails(director.getFirst()));
            });
            gridPane.add(directorLink, 1, 2);
        }
        int hours = episode.getDuration() / 60;
        int minutes = episode.getDuration() % 60;
        String lengthStr;
        if (hours > 0 && minutes > 0) {
            lengthStr =  hours + "h " + minutes + "m";
        } else if (hours > 0) {
            lengthStr = hours + "h";
        } else {
            lengthStr = minutes + "m";
        }
        durationLabel.setText(lengthStr);
        titleLabel.setText(currSeries.getTitle());
        seasonLabel.setText(Integer.toString(currSeason.getSeasonNumber()));
        ratingLabel.setText(Double.toString(episode.getImdbRating()));
    }
}
