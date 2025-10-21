package main.java.myApp.controllers.series;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import main.java.myApp.ScreenManager;
import main.java.myApp.model.Series;
import main.java.myApp.service.InvalidSeriesDataException;
import main.java.myApp.service.SeasonAlreadyExistsException;
import main.java.myApp.service.SeriesService;
import main.java.myApp.util.Validator;

import javax.swing.*;
import java.net.URL;
import java.time.Year;
import java.util.ResourceBundle;

public class AddSeasonController implements Initializable {
    private final SeriesService seriesService;
    private final ScreenManager screenManager;


    public AddSeasonController(SeriesService seriesService, ScreenManager screenManager) {
        this.seriesService = seriesService;
        this.screenManager = screenManager;
    }

    @FXML
    private ChoiceBox<Series> seriesChoiceBox;
    @FXML
    private Spinner<Integer> releaseYearSpinner;
    @FXML
    private Label infoLabel;

    private int newSeasonNumber;

    private void displaySeasonNumberToAdd(ActionEvent e) {
        Series currSeries = seriesChoiceBox.getValue();
        if (seriesService.getSeasonsBySeriesId(currSeries.getId()).isEmpty()) {
            newSeasonNumber = 1;
        } else {
            newSeasonNumber = (seriesService.getSeasonsBySeriesId(currSeries.getId()).getLast().getSeasonNumber() + 1);
        }
        infoLabel.setText("Adding season " + newSeasonNumber + " of series: " + currSeries.getTitle());
    }

    @FXML
    private void addSeason(ActionEvent e) {
        Series series = seriesChoiceBox.getValue();
        Year releaseYear = Year.of(releaseYearSpinner.getValue());

        if (Validator.isNull(seriesChoiceBox) || Validator.isNull(releaseYearSpinner)) {
            screenManager.showAlert("Warning", "Every field is mandatory", Alert.AlertType.WARNING);
            return;
        }
        if (seriesService.getSeasonsBySeriesId(series.getId()).isEmpty()) {
            newSeasonNumber = 1;
        } else {
            newSeasonNumber = (seriesService.getSeasonsBySeriesId(series.getId()).getLast().getSeasonNumber() + 1);
        }

        if (Validator.isFutureDate(releaseYear)) {
            screenManager.showAlert("Warning", "release year can't be in the future", Alert.AlertType.WARNING);
            return;
        } else if (newSeasonNumber != 1) {
            // if season is not the first season make sure user can't add next season the was released before previous season
            if (seriesService.getSeasonsBySeriesId(series.getId()).getLast().getReleaseYear().isAfter(releaseYear)) {
                screenManager.showAlert("Warning", "release year of 'next' season can't be release year of 'last' season"  , Alert.AlertType.WARNING);
                return;
            }
        }
        try {
            seriesService.addSeason(series.getId(), newSeasonNumber , releaseYear);
            screenManager.showAlert("Info", "Season Added Successfully"  , Alert.AlertType.INFORMATION);
        } catch (InvalidSeriesDataException | SeasonAlreadyExistsException ex) {
            screenManager.showAlert("Error", "Something went wrong ensure all field are correct"  , Alert.AlertType.ERROR);
        }
        displaySeasonNumberToAdd(e);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> releaseYearSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2025, 1 );
        releaseYearSpinner.setValueFactory(releaseYearSpinnerValueFactory);
        seriesChoiceBox.getItems().addAll(seriesService.getAllSeries());

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
        seriesChoiceBox.setOnAction(this::displaySeasonNumberToAdd);
    }

}
