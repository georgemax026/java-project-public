package main.java.myApp.controllers.series;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;
import main.java.myApp.ScreenManager;
import main.java.myApp.model.Actor;
import main.java.myApp.model.Director;
import main.java.myApp.model.Season;
import main.java.myApp.model.Series;
import main.java.myApp.service.*;
import main.java.myApp.util.Validator;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddEpisodeController implements Initializable {
    private final SeriesService seriesService;
    private final ActorService actorService;
    private final DirectorService directorService;
    private final ScreenManager screenManager;

    public AddEpisodeController(SeriesService seriesService, ActorService actorService, DirectorService directorService, ScreenManager screenManager) {
        this.actorService = actorService;
        this.seriesService = seriesService;
        this.directorService = directorService;
        this.screenManager = screenManager;
    }

    @FXML
    private ChoiceBox<Series> seriesChoiceBox;
    @FXML
    private ChoiceBox<Season> seasonChoiceBox;
    @FXML
    private ChoiceBox<Director> directorChoiceBox;
    @FXML
    private ChoiceBox<Actor> protagonistChoiceBox;
    @FXML
    private Spinner<Integer> durationSpinner;
    @FXML
    private Spinner<Double> imdbRatingSpinner;


    // enables/disables everything except the series and season ChoiceBoxes
    private void setRestDisabled(boolean disabled) {
        directorChoiceBox.setDisable(disabled);
        protagonistChoiceBox.setDisable(disabled);
        durationSpinner.setDisable(disabled);
        imdbRatingSpinner.setDisable(disabled);
    }
    // when the user selects a series enable the seasonChoiceBox
    private void onSeriesChoose(ActionEvent e) {
        seasonChoiceBox.getItems().clear();
        Series currSeries = seriesChoiceBox.getValue();
        // handle if this is empty
        List<Season> seriesSeasons = seriesService.getSeasonsBySeriesId(currSeries.getId());
        if (seriesSeasons.isEmpty() ) {
            // warn user series has no seasons yet
            return;
        } else {
            // if seasons exists add them and  enable the choiceBox
            seasonChoiceBox.getItems().addAll(seriesSeasons);
            seasonChoiceBox.setDisable(false);
        }
        seasonChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Season season) {
                return (season == null) ? "" : "Season " + season.getSeasonNumber();
            }

            @Override
            public Season fromString(String string) {
                // not really used for ChoiceBox, but must be implemented
                return null;
            }
        });
        setRestDisabled(true);
    }
    // when user chooses season enable the rest of the field
    private void onSeasonChoose(ActionEvent e) {
        setRestDisabled(false);
    }

    @FXML
    private void addEpisode(ActionEvent e) {
        Director director = directorChoiceBox.getValue();
        Actor protagonist = protagonistChoiceBox.getValue();
        String protagonistStr = protagonist.getFirstName() + " " + protagonist.getLastName();
        String directorStr = director.getFirstName() + " " + director.getLastName();

        // make sure every field is not null
        if (Validator.isNull(seriesChoiceBox) || Validator.isNull(seasonChoiceBox) || Validator.isNull(directorChoiceBox) || Validator.isNull(protagonistChoiceBox)
                || Validator.isNull(durationSpinner) || Validator.isNull(imdbRatingSpinner)) {
            screenManager.showAlert("Warning", "Every field is required", Alert.AlertType.WARNING);
            return;
        }

        try {
            seriesService.addEpisode(seasonChoiceBox.getValue().getId(), durationSpinner.getValue(),
                    directorStr, imdbRatingSpinner.getValue(), protagonistStr);
            screenManager.showAlert("Info", "Episode Added Successfully", Alert.AlertType.INFORMATION);
        } catch (InvalidEpisodeDataException ex) {
            System.err.println(ex.getMessage());
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        seriesChoiceBox.getItems().addAll(seriesService.getAllSeries());
        directorChoiceBox.getItems().addAll(directorService.getAllDirectors());
        protagonistChoiceBox.getItems().addAll(actorService.getAllActor());

        // add values to the spinners
        SpinnerValueFactory<Double> imdbRatingValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 10.0, 5.0, 0.1);
        imdbRatingSpinner.setValueFactory(imdbRatingValueFactory);
        SpinnerValueFactory<Integer> durationValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0, 3);
        durationSpinner.setValueFactory(durationValueFactory);

        // The following code is so that the ChoiceBox displays what is specified instead of using the toString method each Class
        seriesChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Series series) {
                return (series == null) ? "" : series.getTitle();
            }

            @Override
            public Series fromString(String string) {
                return null;
            }
        });
        directorChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Director director) {
                return (director == null) ? "" : director.getFirstName() + " " + director.getLastName();
            }

            @Override
            public Director fromString(String string) {
                return null;
            }
        });
        protagonistChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Actor actor) {
                return (actor == null) ? "" : actor.getFirstName() + " " + actor.getLastName();
            }

            @Override
            public Actor fromString(String string) {
                return null;
            }
        });

        seriesChoiceBox.setOnAction(this::onSeriesChoose);
        seasonChoiceBox.setOnAction(this::onSeasonChoose);
    }
}
