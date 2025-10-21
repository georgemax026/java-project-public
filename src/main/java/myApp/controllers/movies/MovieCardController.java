package main.java.myApp.controllers.movies;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.java.myApp.model.Movie;

public class MovieCardController {
    @FXML
    private ImageView poster;
    @FXML
    private Label title;
    @FXML
    private Label imdbRating;
    @FXML
    private Label userRating;

    public void setData(Movie movie) {
        String posterPath = "/main/resources/images/movies/m" + movie.getId() + ".jpg";
        try {
            poster.setImage(new Image(
                    getClass().getResourceAsStream(posterPath),
                    160, 240,
                    true,
                    true
            ));

            poster.setCache(false);
        } catch (Exception e) {
            poster.setImage(null);
        }
        title.setText(movie.getTitle());
        imdbRating.setText("IMDb: " + movie.getImdbRating());
        String ratingStr = String.format("%.1f", movie.getUserRatingAverage());
        userRating.setText("User Rating: " + ratingStr);
    }
}
