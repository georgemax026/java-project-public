package main.java.myApp.controllers.directors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import main.java.myApp.ScreenManager;
import main.java.myApp.model.Gender;
import main.java.myApp.model.Media;
import main.java.myApp.model.Series;
import main.java.myApp.service.*;
import main.java.myApp.util.Validator;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;


public class AddDirectorController implements Initializable {
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private ChoiceBox<Gender> genderChoiceBox;
    @FXML
    private ChoiceBox<Media> mediaChoiceBox;


    private final SeriesService seriesService;
    private final DirectorService directorService;
    private final MovieService movieService;
    private final ScreenManager screenManager;


    public AddDirectorController(DirectorService directorService, SeriesService seriesService, MovieService movieService, ScreenManager screenManager) {
        this.directorService = directorService;
        this.seriesService = seriesService;
        this.movieService = movieService;
        this.screenManager = screenManager;
    }

    private final ArrayList<Media> bestOfList = new ArrayList<>(3);


    @FXML
    private void addToMediaList(ActionEvent e) {
        if (bestOfList.contains(mediaChoiceBox.getValue())) {
            return;
        } else {
            bestOfList.add(mediaChoiceBox.getValue());
        }
    }
    @FXML
    private void displayMediaList(ActionEvent e) {
        screenManager.showPopup("/main/resources/fxml/directors/bestOfList.fxml", "best of", (BestOfListController c) -> c.displayMedia(bestOfList));
    }
    @FXML
    private void addDirector(ActionEvent e) {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        LocalDate birthDate = birthDatePicker.getValue();
        Gender gender = genderChoiceBox.getValue();

        ArrayList<String> bestOfStr = new ArrayList<>(bestOfList.stream()
                .map(Media::getTitle)
                .toList());

        // validate user input
        if (Validator.isBlank(firstNameTextField) || Validator.isBlank(lastNameTextField) || Validator.isNull(birthDatePicker)
                || Validator.isNull(genderChoiceBox)) {
            screenManager.showAlert("Warning", "Every Field is mandatory", Alert.AlertType.WARNING);
            return;
        } else if (directorService.directorExists(lastName)) {
            screenManager.showAlert("Error", "director already exists", Alert.AlertType.ERROR);
            return;
        } else if (birthDate.getYear() > LocalDate.now().getYear() - 18) {
            screenManager.showAlert("Warning", "Director must be at least 18 years old", Alert.AlertType.WARNING);
            return;
        } else if (bestOfStr.isEmpty()) {
            // ask the user for confirmation to add the director without any knownFor in the list
            if (!screenManager.showConfirmation("Info", "Are you sure you do not want add any 'known for' movies/series? (cannot be added after)")) {
                return;
            }
        }
        try {
            directorService.addDirector(firstName, lastName, birthDate, gender, bestOfStr);
            screenManager.showAlert("Info", "Director added successfully", Alert.AlertType.INFORMATION);
        } catch (InvalidDirectorDataException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    private void clearBestOfList(ActionEvent e) {
        bestOfList.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderChoiceBox.getItems().addAll(Gender.values());
        ArrayList<Media> allContent = new ArrayList<>();
        allContent.addAll(movieService.getAllMovies());
        allContent.addAll(seriesService.getAllSeries());
        allContent.sort(Comparator.comparing(Media::getTitle));

        mediaChoiceBox.getItems().addAll(allContent);

        mediaChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Media content) {
                return (content == null) ? "" : content.getTitle();
            }

            @Override
            public Series fromString(String string) {
                return null;
            }
        });
    }
}
