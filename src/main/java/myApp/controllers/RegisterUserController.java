package main.java.myApp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import main.java.myApp.ScreenManager;
import main.java.myApp.service.InvalidUserDataException;
import main.java.myApp.service.UserAlreadyExistsException;
import main.java.myApp.service.UserService;
import main.java.myApp.util.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterUserController {

    private final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private final UserService userService;
    private final ScreenManager screenManager;

    public RegisterUserController(UserService userService, ScreenManager screenManager) {
        this.userService = userService;
        this.screenManager = screenManager;
    }

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;

    // if email format is valid return true;
    private boolean validateEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    // method for when the register button is pressed
   @FXML
   private void registerUser(ActionEvent actionEvent) {
       String username = usernameTextField.getText().trim();
       String email = emailTextField.getText().trim();
       String firstName = firstNameTextField.getText();
       String lastName = lastNameTextField.getText();
       // validate input
       if (Validator.isBlank(usernameTextField) || Validator.isBlank(firstNameTextField)
               || Validator.isBlank(emailTextField) || Validator.isBlank(lastNameTextField)) {
           screenManager.showAlert("Warning", "every field is required", Alert.AlertType.WARNING);
           return;
       } else if (userService.userExists(username)) {
           screenManager.showAlert("Warning", "Username already in use. Please choose a different username", Alert.AlertType.WARNING);
           return;
       } else if (!validateEmail(email)) {
           screenManager.showAlert("Warning", "Invalid email format. Make sure you email is correct", Alert.AlertType.WARNING);
           return;
       }
       try {
           userService.addUser(firstName, lastName, username, email);
       } catch (UserAlreadyExistsException | InvalidUserDataException ex) {
           screenManager.showAlert("Error", "Something went wrong, make sure every field is properly filled", Alert.AlertType.ERROR);
           return;
       }
       screenManager.showAlert("Welcome", "Congratulation you successfully registered", Alert.AlertType.INFORMATION);
       screenManager.showScreen("/main/resources/fxml/login.fxml");
   }
   @FXML
    private void onBack(ActionEvent actionEvent) {
        screenManager.showScreen("/main/resources/fxml/login.fxml");
   }
}
