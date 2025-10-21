package main.java.myApp.service;

import main.java.myApp.model.Actor;
import main.java.myApp.model.Gender;
import main.java.myApp.model.Race;
import main.java.myApp.repository.ActorRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ActorService {

    private final ActorRepository actorRepository;

    /**
     * Constructs a ActorService with a given repository.
     * @param actorRepository The data access layer for actors.
     */
    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }
    /**
     * Retrieves an actor by their unique ID.
     * @param id The ID of the actor to find.
     * @return The found Actor object.
     * @throws ActorNotFoundException if no Actor with the given ID exists.
     */
    public Actor getActorById(int id) throws ActorNotFoundException {
        return actorRepository.findById(id)
                .orElseThrow(() -> new ActorNotFoundException("Actor with ID: " + id + " Not found"));
    }
    /**
     * Retrieves all Actors from the repository.
     * @return A {@link List<Actor>} of all Actors.
     */
    public List<Actor> getAllActor() {
        return actorRepository.findAll();
    }
    /**
     * Searches for Actor with the given email.
     * @param lastname The last name to search for actor.
     * @return the actor if found.
     * @throws ActorNotFoundException if Actor was not found.
     */
    public Actor getActorByName(String lastname) throws ActorNotFoundException {
        return actorRepository.findByName(lastname).orElseThrow(() -> new ActorNotFoundException("actor with name: " + lastname + "was not found"));
    }

    /**
     * creates a new actor and adds it to the repository
     * @param firstName the first name for the actor
     * @param lastName the last name of the actor
     * @param birthDate the date of birth of the actor
     * @param gender the gender of the actor
     * @param race the race of the actor
     * @return an instance of the new actor
     * @throws InvalidActorDataException if a user with the same email already exists
     * @throws ActorAlreadyExistsException if actor already exists
     */
    public Actor addActor(String firstName, String lastName, LocalDate birthDate,
                          Gender gender, Race race) throws InvalidActorDataException, ActorAlreadyExistsException {

        if (firstName.isBlank() || lastName.isBlank()) {
            throw new InvalidActorDataException("Actor must have first and last name");
        } else if (birthDate.getYear() > LocalDate.now().getYear()) {
            throw new InvalidActorDataException("Actor birth date can't be in the future");
        }
        // check if actor already exists
        List<Actor> actors = actorRepository.findAll();
        Optional<Actor> existingActor = actors.stream()
                .filter(a -> {
                    // check if actor already exists by comparing first and last name
                    return (a.getLastName().equalsIgnoreCase(lastName));
                })
                .findFirst();
        if (existingActor.isPresent()) {
            throw new ActorAlreadyExistsException("Actor:  " + lastName + " already exists");
        } else {
            Actor newActor = new Actor(firstName, lastName, birthDate, gender, race);
            actorRepository.save(newActor);
            return newActor;
        }
    }
    /**
     * checks if an actor with the same lastname exists
     * @param lastname the last name of the actor
     * @return true if an actor with the same lastname exists or false if not
     */
    public boolean actorExists(String lastname) {
        return actorRepository.existsByLastName(lastname);
    }
}
