package main.java.myApp.service;

public class InvalidActorDataException extends RuntimeException {
    public InvalidActorDataException(String message) {
        super(message);
    }
}
