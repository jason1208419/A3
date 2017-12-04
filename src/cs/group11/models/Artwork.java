package cs.group11.models;

import java.util.Calendar;

import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
import cs.group11.interfaces.Validatable;
import javafx.scene.image.Image;

public abstract class Artwork implements Validatable {

	private int id;
	private String name;
	private String description;
	private String imagePath;
	private String artist;
	private int creationYear;

	public Artwork() {
	}

	public Artwork(String name, String description, String imagePath, String artist, int creationYear) {
		this.id = 0;
		this.name = name;
		this.description = description;
		this.imagePath = imagePath;
		this.artist = artist;
		this.creationYear = creationYear;

		this.validate();
	}

	public Artwork(int id, String name, String description, String imagePath, String artist, int creationYear) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.imagePath = imagePath;
		this.artist = artist;
		this.creationYear = creationYear;

		this.validate();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getArtist() {
		return artist;
	}

	public int getCreationYear() {
		return creationYear;
	}

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
}
