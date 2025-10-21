package main.java.myApp.service;

public class InvalidSeriesDataException extends RuntimeException {
    public InvalidSeriesDataException(String message) {
        super(message);
    }
}
