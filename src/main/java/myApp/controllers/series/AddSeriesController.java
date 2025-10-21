package main.java.myApp.controllers.series;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import main.java.myApp.ScreenManager;
import main.java.myApp.model.Genre;
import main.java.myApp.service.InvalidSeriesDataException;
import main.java.myApp.service.SeriesAlreadyExistsException;
import main.java.myApp.service.SeriesService;
import main.java.myApp.util.ImageUtils;
import main.java.myApp.util.Validator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddSeriesController implements Initializable {
    private final SeriesService seriesService;
    private final ScreenManager screenManager;


    public AddSeriesController (SeriesService seriesService, ScreenManager screenManager) {
        this.seriesService = seriesService;
        this.screenManager = screenManager;
    }

    @FXML
    private TextField titleTextField;
    @FXML
    private ChoiceBox<Genre> genreChoiceBox;
    @FXML
    private Button uploadPictureButton;

    private final String SERIES_POSTER_PATH = "./src/main/resources/images/series/";
    private File selectedPosterFile;

    public void addSeries(ActionEvent event) {
        String title = titleTextField.getText();
        Genre genre = genreChoiceBox.getValue();

        if (Validator.isBlank(titleTextField) || Validator.isNull(genreChoiceBox)) {
            screenManager.showAlert("Warning", "Every Field is mandatory", Alert.AlertType.WARNING);
            return;
        } else if (seriesService.seriesExists(title)) {
            screenManager.showAlert("Error", "Series already exists", Alert.AlertType.WARNING);
            return;
        } else if (selectedPosterFile == null && !screenManager.showConfirmation("Warning", "Are you sure you want this movie to have no poster? (you can't add it after)")) {
            return;
        }
        try {
            int newSeriesId = seriesService.addSeries(title, genre).getId();
            if (selectedPosterFile != null) {
                try {
                    File outputFile = new File(SERIES_POSTER_PATH + "s" + newSeriesId + ".jpg");
                    ImageUtils.resizeAndSaveJpg(selectedPosterFile, outputFile, 600, 900, 0.7f);
                } catch (IOException ex) {
                    screenManager.showAlert("info", "Something went wrong while saving the image, series added without poster", Alert.AlertType.INFORMATION);
                }
            }
            screenManager.showAlert("info", "Series added Successfully!", Alert.AlertType.INFORMATION);
        } catch (InvalidSeriesDataException | SeriesAlreadyExistsException ex) {
            screenManager.showAlert("Error", "Something went wrong. Please ensure every field is correct.", Alert.AlertType.ERROR);
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
    }
}
