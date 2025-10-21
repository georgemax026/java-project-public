package main.java.myApp.service;

public class ActorAlreadyExistsException extends RuntimeException {
    public ActorAlreadyExistsException(String message) {
        super(message);
    }
}
