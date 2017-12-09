package cs.group11.models;

import java.util.Calendar;
import java.util.List;

import cs.group11.MegaDB;
import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
import cs.group11.interfaces.Validatable;

public abstract class Artwork implements Validatable {

	private int id;
	private String name;
	private String description;
	private String imagePath;
	private String artist;
	private int creationYear;

	public Artwork(String name, String description, String imagePath, String artist, int creationYear) {
		this.id = getNextId();

		this.name = name;
		this.description = description;
		this.imagePath = imagePath;
		this.artist = artist;
		this.creationYear = creationYear;
	}

	public Artwork() {
	}

	private static int getNextId() {
		List<Artwork> artworks = MegaDB.getArtworks();

		if (artworks.size() == 0) {
			return 0;
		}

		Artwork lastArtwork = artworks.get(artworks.size() - 1);
		return lastArtwork.getId() + 1;
	}

	public Artwork(int id, String name, String description, String imagePath, String artist, int creationYear) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.imagePath = imagePath;
		this.artist = artist;
		this.creationYear = creationYear;
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
