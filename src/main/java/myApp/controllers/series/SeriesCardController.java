package main.java.myApp.controllers.series;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.java.myApp.model.Series;


public class SeriesCardController {
    @FXML
    private ImageView poster;
    @FXML
    private Label title;
    @FXML
    private Label rating;

    public void setData(Series series) {
        String posterPath = "/main/resources/images/series/s" + series.getId() + ".jpg";
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
        title.setText(series.getTitle());
        if (series.getUserRating().isEmpty()) {
            rating.setText("N/A");
        } else {
            String ratingStr = String.format("%.1f", series.getUserRatingAverage());
            rating.setText("User Rating: " + ratingStr);
        }
    }

}
