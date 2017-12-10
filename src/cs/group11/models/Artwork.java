package cs.group11.models;

import java.util.Calendar;
import java.util.List;

import cs.group11.MegaDB;
import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
import cs.group11.interfaces.Validatable;

/**
 * This class represent an artwork, storing any necessary information of an artwork
 */
public abstract class Artwork implements Validatable {

    private int id;
    private String name;
    private String description;
    private String imagePath;
    private String artist;
    private int creationYear;

    /**
     * To initialize an artwork
     *
     * @param name         The artwork title
     * @param description  the description of an artwork
     * @param imagePath    the path of the artwork image
     * @param artist       the artist of the artwork
     * @param creationYear the creation year of the artwork
     */
    public Artwork(String name, String description, String imagePath, String artist, int creationYear) {
        this.id = getNextId();

        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.artist = artist;
        this.creationYear = creationYear;
    }

    /**
     * To initialize an artwork
     * @param id the unique id of the artwork
     * @param name The artwork title
     * @param description the description of an artwork
     * @param imagePath the path of the artwork image
     * @param artist the artist of the artwork
     * @param creationYear the creation year of the artwork
     */
    public Artwork(int id, String name, String description, String imagePath, String artist, int creationYear) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.artist = artist;
        this.creationYear = creationYear;
    }

    /**
     * Assign an unique id for the artwork
     *
     * @return an unique id
     */
    private static int getNextId() {
        List<Artwork> artworks = MegaDB.getArtworks();

        //assign 0 as the artwork id if it's the first artwork
        if (artworks.size() == 0) {
            return 0;
        }

        Artwork lastArtwork = artworks.get(artworks.size() - 1);
        return lastArtwork.getId() + 1;
    }

    /**
     * Get the id of this artwork
     * @return the artwork id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the title of this artwork
     * @return the title
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description of this artwork
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the image path of this artwork
     * @return the image path
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Get the artist of this artwork
     * @return the artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Get the creation year of the artwork
     * @return the creation year
     */
    public int getCreationYear() {
        return creationYear;
    }

    /**
     * Convert artwork into a storable format
     * @return the converted format
     */
    public abstract String toCsv();

    @Override
    public void validate() throws InvalidDataException {
        if (Validator.isStringEmpty(name)) {
            throw new InvalidDataException("No name set for artwork!");
        }
        if (Validator.isStringEmpty(artist)) {
            throw new InvalidDataException("No artist set for artwork!");
        }
        if (Validator.isStringEmpty(imagePath)) {
            throw new InvalidDataException("No image specified for artwork!");
        }
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (!Validator.isIntValid(creationYear, currentYear, 0)) {
            throw new InvalidDataException(
                    "The specified creation year is either negative or beyond the current year.");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Artwork)) {
            return false;
        }
        Artwork other = (Artwork) obj;
        if (!artist.equals(other.artist)) {
            return false;
        }
        if (creationYear != other.creationYear) {
            return false;
        }
        if (!imagePath.equals(other.imagePath)) {
            return false;
        }
        if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
