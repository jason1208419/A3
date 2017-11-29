package cs.group11.models;

import cs.group11.helpers.InvalidDataException;
import cs.group11.interfaces.Validatable;
import javafx.scene.image.Image;

public abstract class Artwork implements Validatable {

    private String name;
    private String description;
    private Image image;
    private String artist;
    private int creationYear;

    public Artwork(String name, String description, Image image, String artist, int creationYear) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.artist = artist;
        this.creationYear = creationYear;

        this.validate();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Image getImage() {
        return image;
    }

    public String getArtist() {
        return artist;
    }

    public int getCreationYear() {
        return creationYear;
    }

    @Override
    public void validate() throws InvalidDataException {
        // TODO: Validate name, description, artist and creationYear
    }
}
