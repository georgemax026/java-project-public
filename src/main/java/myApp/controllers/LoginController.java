package main.java.myApp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import main.java.myApp.ScreenManager;
import main.java.myApp.model.User;
import main.java.myApp.service.UserService;
import main.java.myApp.util.Session;


public class LoginController {
    private final UserService userService;
    private final ScreenManager screenManager;

    public LoginController(UserService userService, ScreenManager screenManager) {
        this.userService = userService;
        this.screenManager = screenManager;
    }

    @FXML
    private TextField usernameTextField;

    @FXML
    private void login(ActionEvent actionEvent) {
        String username = usernameTextField.getText();
        User currUser;
        if (!userService.userExists(username)) {
            screenManager.showAlert("warning", "User not found", Alert.AlertType.WARNING);
            return;
        } else {
            currUser = userService.getUserByUsername(username);
        }
        Session.setUser(currUser);
        screenManager.showScreen("/main/resources/fxml/main.fxml");
    }

    @FXML
    private void onRegisterUser(ActionEvent actionEvent) {
        screenManager.showScreen("/main/resources/fxml/registerUser.fxml");
    }
}
