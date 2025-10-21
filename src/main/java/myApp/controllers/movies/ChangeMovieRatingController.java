package main.java.myApp.controllers.movies;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import main.java.myApp.model.Movie;
import main.java.myApp.service.MovieService;
import main.java.myApp.util.Session;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangeMovieRatingController implements Initializable {
    private Movie movie;

    private final MovieService movieService;

    public ChangeMovieRatingController(MovieService movieService) {
        this.movieService = movieService;
    }

    private @FXML Spinner<Integer> ratingSpinner;

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @FXML
    private void changeRating(ActionEvent event) {
        Integer userRating = ratingSpinner.getValue();
        movieService.rateMovie(movie.getId(), Session.getUser().getId(), userRating);
        Stage stage = (Stage) ratingSpinner.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // add values to spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
        ratingSpinner.setValueFactory(valueFactory);
    }
}
