package main.java.myApp.service;

public class InvalidDirectorDataException extends RuntimeException {
    public InvalidDirectorDataException(String message) {
        super(message);
    }
}
