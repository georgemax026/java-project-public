package main.java.myApp.controllers.series;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import main.java.myApp.model.Series;
import main.java.myApp.service.SeriesService;
import main.java.myApp.util.Session;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangeSeriesRatingController implements Initializable {
    private Series series;

    private final SeriesService seriesService;

    public ChangeSeriesRatingController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @FXML
    private Spinner<Integer> ratingSpinner;

    public void setSeries(Series series) {
        this.series = series;
    }

    @FXML
    private void changeRating(ActionEvent event) {
        Integer userRating = ratingSpinner.getValue();
        seriesService.rateSeries(series.getId(), Session.getUser().getId(), userRating);
        Stage stage = (Stage) ratingSpinner.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
        ratingSpinner.setValueFactory(valueFactory);
    }
}
