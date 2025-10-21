package main.java.myApp.service;

public class SeasonAlreadyExistsException extends RuntimeException {
    public SeasonAlreadyExistsException(String message) {
        super(message);
    }
}
