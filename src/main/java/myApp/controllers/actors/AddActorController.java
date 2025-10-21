package main.java.myApp.controllers.actors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import main.java.myApp.ScreenManager;
import main.java.myApp.model.Gender;
import main.java.myApp.model.Race;
import main.java.myApp.service.ActorAlreadyExistsException;
import main.java.myApp.service.ActorService;
import main.java.myApp.service.InvalidActorDataException;
import main.java.myApp.util.Validator;

import java.net.URL;
import java.util.ResourceBundle;

public class AddActorController implements Initializable {
    private final ActorService actorService;
    private final ScreenManager screenManager;

    public AddActorController(ActorService actorService, ScreenManager screenManager) {
        this.actorService = actorService;
        this.screenManager = screenManager;
    }

    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private ChoiceBox<Gender> genderChoiceBox;
    @FXML
    private ChoiceBox<Race> raceChoiceBox;

    @FXML
    private void addActor(ActionEvent event) {
        // make sure user has filled every field
        if (Validator.isBlank(firstNameTextField) || Validator.isBlank(lastNameTextField) || Validator.isNull(genderChoiceBox)
                || Validator.isNull(raceChoiceBox) || Validator.isNull(birthDatePicker)) {
            screenManager.showAlert("Warning", "Every Field is mandatory", Alert.AlertType.WARNING);
            return;
        } else if (actorService.actorExists(lastNameTextField.getText())) {
            screenManager.showAlert("Error", "Actor already exists", Alert.AlertType.ERROR);
            return;
        } else if (Validator.isFutureDate(birthDatePicker.getValue())) {
            // make sure actor isn't a traveler from the future
            screenManager.showAlert("Warning", "birth date can't be in the future", Alert.AlertType.WARNING);
            return;
        }
        try {
            actorService.addActor(firstNameTextField.getText().trim(), lastNameTextField.getText().trim(), birthDatePicker.getValue()
                    ,genderChoiceBox.getValue(), raceChoiceBox.getValue());
            screenManager.showAlert("Info", "Actor added successfully", Alert.AlertType.INFORMATION);
        } catch (InvalidActorDataException | ActorAlreadyExistsException ex) {
            screenManager.showAlert("Error", "Something went wrong ensure all the field are correct", Alert.AlertType.ERROR);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // fill the choiceBoxes
        genderChoiceBox.getItems().addAll(Gender.values());
        raceChoiceBox.getItems().addAll(Race.values());
    }
}
