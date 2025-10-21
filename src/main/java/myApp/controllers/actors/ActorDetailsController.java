package main.java.myApp.controllers.actors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.java.myApp.model.Actor;
import main.java.myApp.model.Gender;

import java.time.format.DateTimeFormatter;

public class ActorDetailsController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label birthDateLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private Label raceLabel;


    public void displayDetails(Actor actor) {
        String birthDateStr = actor.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        if (actor.getGender() == Gender.FEMALE) {
            genderLabel.setText("Female");
        } else {
            genderLabel.setText("Male");
        }
        birthDateLabel.setText(birthDateStr);
        nameLabel.setText(actor.getFirstName() + " " + actor.getLastName());
        raceLabel.setText(actor.getRace().toString());
    }
}
