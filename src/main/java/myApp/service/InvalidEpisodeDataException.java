package main.java.myApp.service;

public class InvalidEpisodeDataException extends RuntimeException {
    public InvalidEpisodeDataException(String message) {
        super(message);
    }
}
