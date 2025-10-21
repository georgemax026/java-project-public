package main.java.myApp;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ScreenManager {

    private final Stage primaryStage;
    private final Function<Class<?>, Object> controllerFactory;

    public ScreenManager(Stage primaryStage, Function<Class<?>, Object> controllerFactory) {
        this.primaryStage = primaryStage;
        this.controllerFactory = controllerFactory;
        primaryStage.setMinWidth(1226);
        primaryStage.setMinHeight(900);
    }

     public void showPopup(String fxmlFileName, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            loader.setControllerFactory(controllerFactory::apply);
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.setTitle(title);
            popupStage.initOwner(primaryStage); // parent for modality
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(new Scene(root));
            popupStage.setResizable(false);
            popupStage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load popup: " + fxmlFileName, e);
        }
    }
    public <T> void showPopup(String fxmlFileName, String title,  Consumer<T> controllerInitializer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            loader.setControllerFactory(controllerFactory::apply); // same factory as main screens
            Parent root = loader.load();

            Stage popupStage = new Stage();

            T controller = loader.getController();
            controllerInitializer.accept(controller);

            popupStage.setTitle(title);
            popupStage.initOwner(primaryStage);
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(new Scene(root));
            popupStage.setResizable(false);
            popupStage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load popup: " + fxmlFileName, e);
        }
    }
    public void showScreen(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            loader.setControllerFactory(controllerFactory::apply);// inject services

            Parent root = loader.load();
            Scene scene = new Scene(root);

            // this is a kind of "fallback" if the scene is "smaller" than the stage it doesn't look white
            scene.setFill(Color.web("#1a1a1a"));
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load screen: " + fxmlFileName, e);
        }
    }
    public <T> void showScreen(String fxmlFile, Consumer<T> controllerInitializer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setControllerFactory(controllerFactory::apply);
            Parent root = loader.load();
            Scene scene = new Scene(root);

            T controller = loader.getController();
            controllerInitializer.accept(controller);

            // this is a kind of "fallback" if the scene is "smaller" than the stage it doesn't look white
            scene.setFill(Color.web("#1a1a1a"));
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.initOwner(primaryStage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(primaryStage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
