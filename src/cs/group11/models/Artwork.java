package cs.group11.models;

import java.util.Calendar;

import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
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
		if (Validator.isStringEmpty(name)) {
			throw new InvalidDataException("No name set for artwork!");
		}
		if (Validator.isStringEmpty(artist)) {
			throw new InvalidDataException("No artist set for artwork!");
		}
		if (Validator.isNull(image)) {
			throw new InvalidDataException("No image specified for artwork!");
		}
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		if (!Validator.isIntValid(creationYear, currentYear, 0)) {
			throw new InvalidDataException(
					"The specified creation year is either negative or beyond the current year.");
		}
	}
}
