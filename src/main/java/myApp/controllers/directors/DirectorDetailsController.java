package main.java.myApp.controllers.directors;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import main.java.myApp.model.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DirectorDetailsController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label birthDateLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private VBox knownForVbox;

    public void displayDetails(Director director) {
        String birthDateStr = director.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        if (director.getGender() == Gender.FEMALE) {
            genderLabel.setText("Female");
        } else {
            genderLabel.setText("Male");
        }

        birthDateLabel.setText(birthDateStr);
        nameLabel.setText(director.getFirstName() + " " + director.getLastName());

        ArrayList<String> bestOf = director.getBestOf();
        ArrayList<Node> bestOfNodes = new ArrayList<>();
        bestOf.forEach(title -> {
            Label bestOfLabel = new Label(title);
            bestOfLabel.setStyle("-fx-text-fill: white");
            bestOfNodes.add(bestOfLabel);
        });
        knownForVbox.getChildren().addAll(bestOfNodes);
    }

}
