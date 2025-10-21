package main.java.myApp.service;

import main.java.myApp.model.Director;
import main.java.myApp.model.Gender;
import main.java.myApp.repository.DirectorRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DirectorService {
    private final DirectorRepository directorRepository;

    /**
     * Constructs a DirectorService with a given repository.
     * @param directorRepository The data access layer for director.
     */
    public DirectorService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    /**
     * Retrieves a Director by their unique ID.
     * @param id The ID of the Director to find.
     * @return The found Director object.
     * @throws DirectorNotFoundException if director with the given ID exists.
     */
    public Director getDirectorById(int id) throws DirectorNotFoundException {
        return directorRepository.findById(id)
                .orElseThrow(() -> new DirectorNotFoundException("Director with ID: " + id + " Not found"));
    }

    /**
     * Retrieves all Directors from the repository.
     * @return A {@link List<Director>} of all Directors.
     */
    public List<Director> getAllDirectors() {
        return directorRepository.findAll();
    }

    /**
     * Searches for Directors with the given lastname.
     * @param lastname the last name of the director.
     * @return the Director if found.
     * @throws DirectorNotFoundException if director was not found.
     */
    public Director getDirectorByName(String lastname) throws DirectorNotFoundException {
        return directorRepository.findByName(lastname).orElseThrow(() -> new DirectorNotFoundException("Director with name: " + lastname + "was not found"));
    }


    /**
     * creates a new Director and adds it to the repository
     * @param firstName the first name for the director
     * @param lastName the last name of the director
     * @param birthDate the date of birth of the director
     * @param gender the gender of the director
     * @param bestOf a {@link List<String>} of the directors best work (what he's known for)
     * @return an instance of the newly created Director
     * @throws InvalidDirectorDataException if a director has the same lastname, director has blank first or lastname, or is under 18 y/o
     * @throws DirectorAlreadyExistsException if a director already exists.
     */
    public Director addDirector(String firstName, String lastName, LocalDate birthDate,
                                Gender gender, ArrayList<String> bestOf) throws InvalidDirectorDataException, DirectorAlreadyExistsException {
        if (firstName.isBlank() || lastName.isBlank()) {
            throw new InvalidDirectorDataException("Director must have a first name and a last name");
        } else if (birthDate.getYear() > LocalDate.now().getYear() - 18) {
            throw new InvalidDirectorDataException("Director must be over 18 years of age");
        }

        List<Director> directors = directorRepository.findAll();
        Optional<Director> existingDirector = directors.stream()
                .filter(d -> d.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        if  (existingDirector.isPresent()) {
            // check if dir already exists
            throw new DirectorAlreadyExistsException("Director:  " + lastName + " already exists");
        } else {
            Director newDirector = new Director(firstName, lastName, birthDate, gender, bestOf);
            directorRepository.save(newDirector);
            return newDirector;
        }
    }

    /**
     * method to add a new movie/series to a directors bestOf
     * @param directorId ID of the director
     * @param title Title of movie/series bestOf to be added
     * @throws DirectorNotFoundException If director with specified id does not exist
     */
    public void addBestOf(int directorId, String title) throws DirectorNotFoundException {
        List<Director> allDirectors = directorRepository.findAll();
        // this is done this way instead of using findById
        // because object is a "reference" so bestOf changes in the allDirectors list
        Optional<Director> currDir = allDirectors.stream().filter(d -> d.getId() == directorId).findFirst();
        if (currDir.isPresent()) {
            currDir.get().addBestOf(title);
        } else {
            throw new DirectorNotFoundException("Director with ID: " + directorId + " was not found");
        }
        directorRepository.saveAll(allDirectors);
    }
    /**
     * checks if a director with the same lastname exists
     * @param lastname the last name of the director
     * @return true if a director with the same lastname exists or false if not
     */
    public boolean directorExists(String lastname) {
        return directorRepository.existsByLastName(lastname);
    }
}
