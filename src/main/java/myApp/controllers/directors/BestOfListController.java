package main.java.myApp.controllers.directors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import main.java.myApp.model.Media;

import java.util.ArrayList;

public class BestOfListController {
    @FXML
    private VBox infoVbox;

    private Label createLabel(String title) {
        Label label = new Label(title);
        label.setStyle("-fx-text-fill: white; -fx-padding: 6; -fx-font-size: 14");
        return label;
    }
    public void displayMedia(ArrayList<Media> mediaList) {
        mediaList.forEach(media -> {
            infoVbox.getChildren().add(createLabel(media.getTitle()));
        });
    }
}
