package main.java.myApp.repository;

import main.java.myApp.model.Actor;
import main.java.myApp.model.Gender;
import main.java.myApp.model.Race;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TextFileActorRepository extends TextFileRepository<Actor> implements ActorRepository {


    public TextFileActorRepository(String filePath) {
        super(filePath);
        this.nextId = calculateNextId();
    }

    /**
     * parses a single line from the db
     * @param line the line of the txt file to be parsed
     * @return returns the db line as an Actor
     */
    @Override
    protected Actor parseLine(String line) {
        String[] parts = line.split(",", 6);
        if (parts.length < 6) {
            throw new IllegalArgumentException("Invalid actor data format: " + line);
        }
        int id = Integer.parseInt(parts[0]);
        String[] dateStrings = parts[3].split("-");
        int[] dateParts = {Integer.parseInt(dateStrings[0]), Integer.parseInt(dateStrings[1]), Integer.parseInt(dateStrings[2])};
        LocalDate birthDate = LocalDate.of(dateParts[0], dateParts[1], dateParts[2]);
        return new Actor(id, parts[1], parts[2], birthDate, Gender.fromDbString(parts[4]), Race.fromDbString(parts[5]));
    }

    @Override
    public List<Actor> findAll() {
        return readAllLines();
    }

    @Override
    public Optional<Actor> findById(int id) {
        return findAll().stream().filter(actor -> actor.getId() == id).findFirst();
    }

    @Override
    public Optional<Actor> findByName(String lastName) {
        return findAll()
                .stream()
                .filter(actor -> actor.getLastName().equalsIgnoreCase(lastName)).findAny();
    }

    @Override
    public void save(Actor actor) {
        List<Actor> actors = findAll();
        actor.setId(nextId++);
        actors.add(actor);
        writeAllLines(actors);
    }

    @Override
    public void saveAll(List<Actor> actors) {
        writeAllLines(actors);
    }


    @Override
    public void deleteById(int id) {
        List<Actor> actors = findAll();
        List<Actor> updatedActors = actors.stream()
                .filter(actor -> actor.getId() != id)
                .toList();
        if (updatedActors.size() < actors.size()) {
            writeAllLines(updatedActors);
        } else {
            System.out.println("Actor with ID " + id + " not found. No deletion performed.");
        }
    }
    @Override
    public boolean existsByLastName(String lastName) {
        return findAll().stream().anyMatch(actor -> actor.getLastName().equalsIgnoreCase(lastName));
    }
}
