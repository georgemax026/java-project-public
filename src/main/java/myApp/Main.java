package main.java.myApp;


import javafx.application.Application;
import javafx.stage.Stage;
import main.java.myApp.controllers.LoginController;
import main.java.myApp.controllers.MainController;
import main.java.myApp.controllers.RegisterUserController;
import main.java.myApp.controllers.SearchResultsController;
import main.java.myApp.controllers.actors.AddActorController;
import main.java.myApp.controllers.directors.AddDirectorController;
import main.java.myApp.controllers.movies.AddMovieController;
import main.java.myApp.controllers.movies.ChangeMovieRatingController;
import main.java.myApp.controllers.movies.MovieDetailsController;
import main.java.myApp.controllers.series.*;
import main.java.myApp.repository.*;
import main.java.myApp.service.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;


public class Main extends Application {

    private SearchService searchService;
    private SeriesService seriesService;
    private ActorService actorService;
    private DirectorService directorService;
    private UserService userService;
    private MovieService movieService;

    private final Map<Class<?>, Supplier<Object>> controllerFactoryMap = new HashMap<>();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // create controller factory for passing in the fxml
        Function<Class<?>, Object> controllerFactory = controllerClass -> {
            Supplier<Object> controllerSupplier = controllerFactoryMap.get(controllerClass);
            if (controllerSupplier != null) {
                return controllerSupplier.get(); // Execute the stored lambda to create the controller
            } else {
                try {
                    // Fallback for controllers with no special dependencies
                    return controllerClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create controller: " + controllerClass.getName(), e);
                }
            }
        };

        // create the main screen manager for the whole app
        ScreenManager manager = new ScreenManager(stage, controllerFactory);
        // add needed constructor values to the map for the controllers
        controllerFactoryMap.put(LoginController.class, () -> new LoginController(userService, manager));
        controllerFactoryMap.put(MainController.class, () -> new MainController(movieService, seriesService, searchService, manager));
        controllerFactoryMap.put(MovieDetailsController.class, () -> new MovieDetailsController(movieService, directorService, actorService, manager));
        controllerFactoryMap.put(SeriesDetailsController.class, () -> new SeriesDetailsController(seriesService, manager));
        controllerFactoryMap.put(AddSeriesController.class, () -> new AddSeriesController(seriesService, manager));
        controllerFactoryMap.put(AddSeasonController.class, () -> new AddSeasonController(seriesService, manager));
        controllerFactoryMap.put(AddEpisodeController.class, () -> new AddEpisodeController(seriesService, actorService, directorService, manager));
        controllerFactoryMap.put(AddActorController.class, () -> new AddActorController(actorService, manager));
        controllerFactoryMap.put(AddDirectorController.class, () -> new AddDirectorController(directorService, seriesService, movieService, manager));
        controllerFactoryMap.put(AddMovieController.class, () -> new AddMovieController(movieService, actorService, directorService, manager));
        controllerFactoryMap.put(ChangeMovieRatingController.class, () -> new ChangeMovieRatingController(movieService));
        controllerFactoryMap.put(ChangeSeriesRatingController.class, () -> new ChangeSeriesRatingController(seriesService));
        controllerFactoryMap.put(RegisterUserController.class, () -> new RegisterUserController(userService, manager));
        controllerFactoryMap.put(EpisodeDetailsController.class, () -> new EpisodeDetailsController(seriesService, searchService, manager));
        controllerFactoryMap.put(SearchResultsController.class, () -> new SearchResultsController(manager));
        // load the starting view
        manager.showScreen("/main/resources/fxml/login.fxml");
    }
    @Override
    public void init() {
        // make the repositories and the services on init()
        MovieRepository movieRepository = new TextFileMovieRepository("src/main/resources/data/movies.txt");
        DirectorRepository directorRepository = new TextFileDirectorRepository("src/main/resources/data/directors.txt");
        ActorRepository actorRepository = new TextFileActorRepository("src/main/resources/data/actors.txt");
        SeriesRepository seriesRepository = new TextFileSeriesRepository("src/main/resources/data/series.txt");
        SeasonRepository seasonRepository = new TextFileSeasonRepository("src/main/resources/data/seasons.txt");
        EpisodeRepository episodeRepository = new TextFileEpisodeRepository("src/main/resources/data/episodes.txt");
        UserRepository userRepository = new TextFileUserRepository("src/main/resources/data/users.txt");
        this.searchService = new SearchService(actorRepository, directorRepository, movieRepository, seriesRepository, seasonRepository, episodeRepository);
        this.seriesService = new SeriesService(seriesRepository, seasonRepository, episodeRepository);
        this.actorService = new ActorService(actorRepository);
        this.directorService = new DirectorService(directorRepository);
        this.userService = new UserService(userRepository);
        this.movieService = new MovieService(movieRepository);
    }
}