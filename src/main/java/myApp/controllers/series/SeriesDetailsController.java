package main.java.myApp.controllers.series;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import main.java.myApp.ScreenManager;
import main.java.myApp.model.Episode;
import main.java.myApp.model.Season;
import main.java.myApp.model.Series;
import main.java.myApp.service.SeriesService;
import main.java.myApp.util.Session;

import java.net.URL;
import java.time.Year;
import java.util.List;
import java.util.ResourceBundle;

public class SeriesDetailsController implements Initializable {
    private final SeriesService seriesService;
    private final ScreenManager screenManager;


    public SeriesDetailsController(SeriesService seriesService, ScreenManager screenManager) {
        this.seriesService = seriesService;
        this.screenManager = screenManager;
    }

    @FXML
    private ImageView posterImageView;
    @FXML
    private FlowPane genreFlow;
    @FXML
    private VBox episodesVBox;
    @FXML
    private Label seriesTitleLabel;
    @FXML
    private ChoiceBox<Season> seasonChoiceBox;
    @FXML
    private HBox metaHbox;
    @FXML
    private Label currentUserLabel;
    @FXML
    private VBox infoVbox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        seasonChoiceBox.setOnAction(this::addEpisodes);
    }

    // helper method to create the episode label
    private Label createEpisodeLabel(int episodeNumber, Episode episode) {
        Label episodeLabel = new Label();
        episodeLabel.setStyle("-fx-background-color: #222; -fx-text-fill: #ccc; -fx-padding: 8 12; -fx-background-radius: 8; -fx-cursor: hand");
        episodeLabel.setText("Episode " + episodeNumber + " - ★ " + episode.getImdbRating() + "/10");
        // store the episode object inside the label
        episodeLabel.setUserData(episode);

        // make it clickable
        episodeLabel.setOnMouseClicked(event -> {
            Episode clicked = (Episode) episodeLabel.getUserData();
            showEpisodeDetails(clicked);
        });

        return episodeLabel;
    }
    // helper method to create the genre label
    private Label createGenreLabel(String text) {
        text = text.toLowerCase();
        Label genre = new Label(text);
        genre.setStyle("-fx-background-color: #222; -fx-text-fill: #ccc; -fx-padding: 6 10; -fx-background-radius: 16");
        return genre;
    }
    // helper method to create the Mata(metadata) label
    private Label createMetaLabel(Year releaseYear, int seasonCount) {
        Label label = new Label();
        if (seasonCount == 1) {
            label.setText(releaseYear + " · " + seasonCount + " Season");
        } else {
            label.setText(releaseYear + " · " + seasonCount + " Seasons");
        }
        label.setStyle("-fx-text-fill:  #ccc; -fx-font-size: 14;");
        return label;
    }
    // helper method to create the user rating label
    private Label createUserRatingLabel(Double rating, int ratingSize) {
        String ratingStr = String.format("%.1f", rating);
        Label label = new Label("User Rating: ★ " + ratingStr + "/10 (" + ratingSize + ")");
        label.setStyle("-fx-text-fill:  #ccc; -fx-font-size: 14;");
        return label;
    }
    // helper method to create "your rating" Node (specific user's rating node)
    private Node createYourRatingNode(Series series) {
        Label label = new Label();
        HBox hBox = new HBox();
        Hyperlink addRating = new Hyperlink();

        int currUserId = Session.getUser().getId();

        // this may be null thus I'm using Integer and not int
        Integer currUserRating = series.getUserRating().get(currUserId);

        addRating.setOnAction((event) -> {
            // consider passing also the ActionEvent
            addRating(series);
        });
        addRating.setStyle("-fx-font-size: 14;");
        label.setStyle("-fx-text-fill:  #ccc; -fx-font-size: 14;");
        if  (currUserRating == null) {
            addRating.setText("Rate this series!");
            return addRating;
        } else {
            label.setText("Your Rating: ★ " + currUserRating + "/10");
            addRating.setText("Change rating!");
        }

        hBox.getChildren().addAll(label,  addRating);
        return hBox;
    }

    // show a popup with the episode details
    private void showEpisodeDetails(Episode episode) {
        screenManager.showPopup("/main/resources/fxml/series/episodeDetails.fxml", "Episode details", (EpisodeDetailsController c) -> c.displayEpisodeDetails(episode));
    }
    // method to add/change rating to a series
    private void addRating(Series series) {
        screenManager.showPopup("/main/resources/fxml/series/changeSeriesRating.fxml", "Change rating", (ChangeSeriesRatingController c) -> c.setSeries(series));
        refresh(series);
    }

    // refresh
    private void refresh(Series series) {
        Series currSeries = seriesService.getSeriesById(series.getId());
        displaySeries(currSeries);
    }
    // helper method that adds the needed episodes to the UI (the vbox)
    private void addEpisodes(ActionEvent actionEvent) {
        List<Episode> episodes = seriesService.getEpisodesBySeasonId(seasonChoiceBox.getValue().getId());
        episodesVBox.getChildren().clear();
        for (int i = 0; i < episodes.size(); i++) {
            Label currEpisode = createEpisodeLabel(i + 1, episodes.get(i));
            episodesVBox.getChildren().add(currEpisode);
        }

    }
    // go back to the main.fxml when the back button's pressed
    @FXML
    private void onBack(ActionEvent actionEvent) {
        screenManager.showScreen("/main/resources/fxml/main.fxml");
    }

    public void displaySeries(Series series) {
        List<Season> seriesSeasons = seriesService.getSeasonsBySeriesId(series.getId());

        currentUserLabel.setText("User: " + Session.getUser().getUsername());
        // clear this in case of a refresh or placeholder data in fxml
        metaHbox.getChildren().clear();
        infoVbox.getChildren().clear();
        genreFlow.getChildren().clear();

        Node youRatingLabel = createYourRatingNode(series);
        infoVbox.getChildren().add(youRatingLabel);

        // set title and genre
        seriesTitleLabel.setText(series.getTitle());
        genreFlow.getChildren().add(createGenreLabel(series.getGenre().toString()));
        // if there is a series with no season and no episodes there's no point of going to "SeriesDetails"
        // prompt the user to add season and episodes and go back to main.fxml
        if (seriesSeasons.isEmpty()) {
            screenManager.showAlert("Info", "No seasons or episodes found for this series. Add seasons and episode!", Alert.AlertType.INFORMATION);
            return;
        }
        // handle metadata area
        Label metaLabel = createMetaLabel(seriesSeasons.getLast().getReleaseYear(), seriesSeasons.size());
        Label userRatingLabel = createUserRatingLabel(series.getUserRatingAverage(), series.getUserRating().size());
        metaHbox.getChildren().addAll(metaLabel, userRatingLabel);



        // this is done this way so that "season X" is displayed correctly but still has a Season model as value
        // if this wasn't here the toString() would be displayed to the user
        seasonChoiceBox.getItems().addAll(seriesSeasons);
        seasonChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Season season) {
                return (season == null) ? "" : "Season: " + season.getSeasonNumber();
            }
            @Override
            public Season fromString(String string) {
                return null;
            }
        });
        // select the first season by default
        seasonChoiceBox.getSelectionModel().select(0);

        String posterPath = "/main/resources/images/series/s" + series.getId() + ".jpg";
        try {
            posterImageView.setImage(new Image(getClass().getResourceAsStream(posterPath)));
            posterImageView.setCache(false);
        } catch (Exception e) {
            posterImageView.setImage(null);
        }
    }
}
