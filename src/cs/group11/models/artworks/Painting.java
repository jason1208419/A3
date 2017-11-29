package cs.group11.models.artworks;

import cs.group11.interfaces.Validatable;
import cs.group11.models.Artwork;
import javafx.scene.image.Image;

public class Painting extends Artwork implements Validatable {

    private double width;
    private double height;

    public Painting(String title, String description, Image image, String artist, int creationYear, double width, double height) {
        super(title, description, image, artist, creationYear);

        this.width = width;
        this.height = height;

        this.validate();
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public void validate() throws IllegalArgumentException {
        super.validate();

        // TODO: Validate width and height
    }
}
