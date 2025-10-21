package main.java.myApp.service;

public class SeriesAlreadyExistsException extends RuntimeException {
    public SeriesAlreadyExistsException(String message) {
        super(message);
    }
}
