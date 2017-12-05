package cs.group11.models.artworks;

import cs.group11.helpers.InvalidDataException;
import cs.group11.interfaces.Validatable;
import cs.group11.models.Artwork;
import javafx.scene.image.Image;

public class Painting extends Artwork implements Validatable {

    private double width;
    private double height;

    public Painting(int id, String title, String description, String imagePath, String artist, int creationYear, double width, double height) {
        super(id, title, description, imagePath, artist, creationYear);

        this.width = width;
        this.height = height;

        this.validate();
    }

    public Painting(String title, String description, String imagePath, String artist, int creationYear, double width, double height) {
        super(title, description, imagePath, artist, creationYear);

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
    public String toCsv() {
        StringBuilder builder = new StringBuilder();

        builder.append("painting").append(",");
        builder.append(this.getId()).append(",");
        builder.append(this.getName()).append(",");
        builder.append(this.getDescription()).append(",");
        builder.append(this.getImagePath()).append(",");
        builder.append(this.getArtist()).append(",");
        builder.append(this.getCreationYear()).append(",");
        builder.append(this.getWidth()).append(",");
        builder.append(this.getHeight()).append(",");

        return builder.toString();
    }

    @Override
    public void validate() throws InvalidDataException {
        super.validate();
        // TODO: Validate width and height
    }
}
