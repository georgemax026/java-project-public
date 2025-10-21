package main.java.myApp.service;

public class DirectorAlreadyExistsException extends RuntimeException {
    public DirectorAlreadyExistsException(String message) {
        super(message);
    }
}
