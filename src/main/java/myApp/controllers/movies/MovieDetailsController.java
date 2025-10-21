package main.java.myApp.controllers.movies;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.myApp.ScreenManager;
import main.java.myApp.controllers.actors.ActorDetailsController;
import main.java.myApp.controllers.directors.DirectorDetailsController;
import main.java.myApp.model.Actor;
import main.java.myApp.model.Director;
import main.java.myApp.model.Movie;
import main.java.myApp.service.*;
import main.java.myApp.util.Session;

import java.time.Year;

public class MovieDetailsController {
    private final MovieService movieService;
    private final DirectorService directorService;
    private final ActorService actorService;
    private final ScreenManager screenManager;

    public MovieDetailsController(MovieService movieService, DirectorService directorService, ActorService actorService, ScreenManager screenManager) {
        this.directorService = directorService;
        this.movieService = movieService;
        this.actorService = actorService;
        this.screenManager = screenManager;
    }


    @FXML
    private Label movieTitleLabel;
    @FXML
    private Label currentUserLabel;
    @FXML
    private FlowPane genreFlow;
    @FXML
    private ImageView posterImageView;
    @FXML
    private HBox metaHbox;
    @FXML
    private VBox infoVbox;

    // go back to the main.fxml when the back button's pressed
    @FXML
    private void onBack(ActionEvent actionEvent) {
        screenManager.showScreen("/main/resources/fxml/main.fxml");
    }

    // create Labels for all things so that they can be appended
    // create label to append to the genre flow thing
    private Label createGenreLabel(String text) {
        text = text.toLowerCase();
        Label genre = new Label(text);
        genre.setStyle("-fx-background-color: #222; -fx-text-fill: #ccc; -fx-padding: 6 10; -fx-background-radius: 16");
        return genre;
    }
    private Label createMetaLabel(Year releaseYear, int length) {
        Label label = new Label();
        int hours = length / 60;
        int minutes = length % 60;
        String lengthStr;
        if (hours > 0 && minutes > 0) {
            lengthStr =  hours + "h " + minutes + "m";
        } else if (hours > 0) {
            lengthStr = hours + "h";
        } else {
            lengthStr = minutes + "m";
        }
        label.setText(releaseYear + " · " + lengthStr);
        label.setStyle("-fx-text-fill:  #ccc; -fx-font-size: 14;");
        return label;
    }
    private Label createUserRatingLabel(Double rating, int ratingSize) {
        String ratingStr = String.format("%.1f", rating);
        Label label = new Label("User Rating: ★ " + ratingStr + "/10 (" + ratingSize + ")");
        label.setStyle("-fx-text-fill:  #ccc; -fx-font-size: 14;");
        return label;
    }
    private Label createImdbRatingLabel(Double rating) {
        String ratingStr = String.format("%.1f", rating);
        Label label = new Label("IMDb Rating: ★ " + ratingStr + "/10");
        label.setStyle("-fx-text-fill:  #f1c40f; -fx-font-size: 14; -fx-font-weight: bold");
        return label;
    }
    // current movie.
    private Node createYourRatingNode(Movie movie) {
        Label label = new Label();
        HBox hBox = new HBox();
        Hyperlink addRating = new Hyperlink();

        int currUserId = Session.getUser().getId();

        // this may be null thus I'm using Integer and not int
        Integer currUserRating = movie.getUserRating().get(currUserId);

        addRating.setOnAction((event) -> {
            addRating(movie);
        });
        addRating.setStyle("-fx-font-size: 14;");
        label.setStyle("-fx-text-fill:  #ccc; -fx-font-size: 14;");
        if  (currUserRating == null) {
            addRating.setText("Rate this movie!");
            return addRating;
        } else {
            label.setText("Your Rating: ★ " + currUserRating + "/10");
            addRating.setText("Change rating!");
        }

        hBox.getChildren().addAll(label,  addRating);
        return hBox;
    }

    // this should be used for directors
    // if directors exist in db this will return a node that makes the director clickable to get more info
    private Node createDirectorNode(String directorFullName) {
        HBox hBox = new HBox();
        Hyperlink directorLink = new Hyperlink();
        Label label = new Label();
        label.setStyle("-fx-text-fill:  #ccc; -fx-font-size: 14;");
        String[] directorNames = directorFullName.split(" ");

        try {
            // this is here to ensure the use of the director's last name
            // and if a director is stored with only 1 name for some reason it doesn't throw OOB exception
            Director currDirector = directorService.getDirectorByName(directorNames[directorNames.length - 1]);
            directorLink.setText(directorFullName);
            label.setText("Director: ");

            directorLink.setOnAction(actionEvent -> {
                displayDirectorDetails(currDirector);
            });
            hBox.getChildren().addAll(label, directorLink);
            return hBox;
        } catch (DirectorNotFoundException ex) {
            label.setText("Director: " + directorFullName);
            hBox.getChildren().addAll(label);
            return hBox;
        }
    }

    // this should be used for actors
    // if actors exist in db this will return a node that makes the actor clickable to get more info
    private Node createActorNode(String actorFullName) {
        HBox hBox = new HBox();
        Hyperlink actorLink = new Hyperlink();
        Label label = new Label();
        label.setStyle("-fx-text-fill:  #ccc; -fx-font-size: 14;");
        String[] actorNames = actorFullName.split(" ");
        try {
            // this is here to ensure the use of the actor's last name
            // and if an actor is stored with only 1 name for some reason it doesn't throw OOB exception
            Actor currActor = actorService.getActorByName(actorNames[actorNames.length - 1]);
            actorLink.setText(actorFullName);

            actorLink.setOnAction(actionEvent -> {
                displayActorDetails(currActor);
            });

            label.setText("Starring: ");
            hBox.getChildren().addAll(label, actorLink);
            return hBox;
        } catch (ActorNotFoundException ex) {
            label.setText("Actor: " + actorFullName);
            hBox.getChildren().addAll(label);
            return hBox;
        }
    }
    // opens a new window to display known info about the actor
   private void displayActorDetails(Actor actor) {
       screenManager.showPopup("/main/resources/fxml/actors/actorDetails.fxml", "Actor Details", (ActorDetailsController c) -> c.displayDetails(actor));
   }
    // opens a new window to display known info about the director
   private void displayDirectorDetails(Director director) {
       screenManager.showPopup("/main/resources/fxml/directors/directorDetails.fxml", "Director Details", (DirectorDetailsController c) -> c.displayDetails(director));
   }
    // method to add new rating to a movie
    private void addRating(Movie movie) {
        screenManager.showPopup("/main/resources/fxml/movies/changeMovieRating.fxml", "add rating", (ChangeMovieRatingController c) -> c.setMovie(movie));
        refresh(movie);
    }
    // refresh
    private void refresh(Movie movie) {
        Movie currMovie = movieService.getMovieById(movie.getId());
        displayMovie(currMovie);
    }

    public void displayMovie(Movie movie) {
        movieTitleLabel.setText(movie.getTitle());
        infoVbox.getChildren().clear();

        // make nodes and add them to the VBOX
        Node actorNode = createActorNode(movie.getProtagonist());
        Node directorNode = createDirectorNode(movie.getDirector());
        Node yourRatingLabel = createYourRatingNode(movie);

        infoVbox.getChildren().addAll(actorNode, directorNode, yourRatingLabel);

        currentUserLabel.setText("User: " + Session.getUser().getUsername());

        // clear the metaBox in case of refresh or placeholder data
        metaHbox.getChildren().clear();
        Label toMeta = createMetaLabel(movie.getReleaseYear(), movie.getDuration());
        Label toUserRating = createUserRatingLabel(movie.getUserRatingAverage(), movie.getUserRating().size());
        Label toImdbRating = createImdbRatingLabel(movie.getImdbRating());
        metaHbox.getChildren().addAll(toMeta, toUserRating, toImdbRating);

        // clear the genreFlow in case of refresh or placeholder data
        genreFlow.getChildren().clear();
        Label genreLabel = createGenreLabel(movie.getGenre().toString());
        genreFlow.getChildren().add(genreLabel);

        String posterPath = "/main/resources/images/movies/m" + movie.getId() + ".jpg";

        try {
            posterImageView.setImage(new Image(getClass().getResourceAsStream(posterPath)));
            posterImageView.setCache(false);
        } catch (Exception e) {
            posterImageView.setImage(new Image(getClass().getResourceAsStream("/main/resources/images/movies/posterNotFound.jpg")));
        }
    }
}
