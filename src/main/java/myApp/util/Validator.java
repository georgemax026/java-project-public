package main.java.myApp.util;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ChoiceBox;

import java.time.LocalDate;
import java.time.Year;

public class Validator {

    public static boolean isBlank(TextField field) {
        return field.getText() == null || field.getText().trim().isEmpty();
    }

    public static boolean isNull(DatePicker picker) {
        return picker.getValue() == null;
    }

    public static boolean isNull(ChoiceBox<?> choiceBox) {
        return choiceBox.getValue() == null;
    }

    public static boolean isFutureDate(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }
    public static boolean isFutureDate(Year year) {
        return year != null && year.isAfter(Year.now());
    }
    public static boolean isNull(Spinner<?> spinner) {
        return spinner.getValue() == null;
    }
}
