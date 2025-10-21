package main.java.myApp.controllers.movies;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import main.java.myApp.ScreenManager;
import main.java.myApp.model.Actor;
import main.java.myApp.model.Director;
import main.java.myApp.model.Genre;
import main.java.myApp.service.*;
import main.java.myApp.util.ImageUtils;
import main.java.myApp.util.Validator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Year;
import java.util.ResourceBundle;

public class AddMovieController implements Initializable {
    private final MovieService movieService;
    private final DirectorService directorService;
    private final ActorService actorService;
    private final ScreenManager screenManager;

    public AddMovieController(MovieService movieService, ActorService actorService, DirectorService directorService, ScreenManager screenManager) {
        this.movieService = movieService;
        this.directorService = directorService;
        this.actorService = actorService;
        this.screenManager = screenManager;
    }

    @FXML
    private TextField titleTextField;
    @FXML
    private ChoiceBox<Genre> genreChoiceBox;
    @FXML
    private ChoiceBox<Director> directorChoiceBox;
    @FXML
    private ChoiceBox<Actor> protagonistChoiceBox;
    @FXML
    private Spinner<Double> imdbRatingSpinner;
    @FXML
    private Spinner<Integer> releaseYearSpinner;
    @FXML
    private Spinner<Integer> movieDurationSpinner;
    @FXML
    private Button uploadPictureButton;

    private final String MOVIE_POSTER_PATH = "./src/main/resources/images/movies/";
    private File selectedPosterFile;


    // method for addMovie button onAction that adds movie
    @FXML
    private void addMovie(ActionEvent actionEvent) {
        // make sure every field except releaseYear exists
        if (Validator.isNull(directorChoiceBox) || Validator.isNull(genreChoiceBox) || Validator.isNull(imdbRatingSpinner) || Validator.isNull(releaseYearSpinner)
                || Validator.isNull(movieDurationSpinner) || Validator.isNull(protagonistChoiceBox) || Validator.isBlank(titleTextField)
                || movieDurationSpinner.getValue() == 0) {
            screenManager.showAlert("Warning", "Every Field except poster is mandatory", Alert.AlertType.WARNING);
            return;
        } else if (movieService.movieExists(titleTextField.getText())) {
            screenManager.showAlert("Error", "Movie already exists", Alert.AlertType.ERROR);
            return;
        } else if (selectedPosterFile == null && !screenManager.showConfirmation("Warning", "Are you sure you want this movie to have no poster? (you can't add it after)")) {
            return;
        }

        Year releaseYear = Year.of(releaseYearSpinner.getValue());
        Director movieDirector = directorChoiceBox.getValue();
        Actor protagonist = protagonistChoiceBox.getValue();
        String directorStr = movieDirector.getFirstName() +  " " + movieDirector.getLastName();
        String actorStr = protagonist.getFirstName() +  " " + protagonist.getLastName();
        // new movie is needed for adding it to the poster
        int newMovieId;
        try {
            // adding movie returns new movie and the id is needed so that the jpg can have to appropriate name
            newMovieId = movieService.addMovie(titleTextField.getText(), releaseYear, genreChoiceBox.getValue(), movieDurationSpinner.getValue(),
                    directorStr, imdbRatingSpinner.getValue(), actorStr).getId();
            // try adding poster if present
            if (selectedPosterFile != null) {
                try {
                    File outputFile = new File(MOVIE_POSTER_PATH + "m" + newMovieId + ".jpg");
                    ImageUtils.resizeAndSaveJpg(selectedPosterFile, outputFile, 600, 900, 0.7f);
                }  catch (IOException ex) {
                    screenManager.showAlert("info", "Something went wrong while saving the image, movie added without poster", Alert.AlertType.INFORMATION);
                }
            }
            screenManager.showAlert("info", "Movie added Successfully!", Alert.AlertType.INFORMATION);
        } catch (InvalidMovieDataException | MovieAlreadyExistsException ex) {
            screenManager.showAlert("Error", "Something went wrong. Please ensure every field is correct.", Alert.AlertType.ERROR);
            return;
        }
        if (imdbRatingSpinner.getValue() > 7.5) {
            directorService.addBestOf(movieDirector.getId(), titleTextField.getText());
        }
    }

    @FXML
    private void onUploadPoster() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Poster");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(uploadPictureButton.getScene().getWindow());
        if (file != null) {
            selectedPosterFile = file;
            uploadPictureButton.setText(file.getAbsolutePath());
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genreChoiceBox.getItems().addAll(Genre.values());
        protagonistChoiceBox.getItems().addAll(actorService.getAllActor());
        directorChoiceBox.getItems().addAll(directorService.getAllDirectors());

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
        // add values to spinners
        SpinnerValueFactory<Double> imdbRatingValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 10.0, 5.0, 0.1);
        imdbRatingSpinner.setValueFactory(imdbRatingValueFactory);
        SpinnerValueFactory<Integer> durationValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0, 3);
        movieDurationSpinner.setValueFactory(durationValueFactory);
        SpinnerValueFactory<Integer> movieValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, LocalDate.now().getYear(), 0, 1);
        releaseYearSpinner.setValueFactory(movieValueFactory);
    }
}
