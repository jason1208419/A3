package cs.group11.models.artworks;

import cs.group11.FileHandler;
import cs.group11.MegaDB;
import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
import cs.group11.interfaces.Validatable;
import cs.group11.models.Artwork;

/**
 * A subclass of Artwork modeling a Painting
 */
public class Painting extends Artwork implements Validatable {

	/**
	 * Painting width
	 */
	private double width;
	/**
	 * Painting height
	 */
	private double height;

	/**
	 * Constructs a new Painting object.
	 * @param id the ID of this painting.
	 * @param title The title of this painting.
	 * @param description The description of this painting.
	 * @param imagePath The image path of this painting.
	 * @param artist the artist/creator of this painting.
	 * @param creationYear the year of creation.
	 * @param width the width.
	 * @param height and height.
	 */
	public Painting(int id, String title, String description, String imagePath, String artist, int creationYear,
			double width, double height) {
		super(id, title, description, imagePath, artist, creationYear);

		this.width = width;
		this.height = height;

		this.validate();

		MegaDB.addArtwork(this);
	}

	/**
	 * Constructs a Painting object.
	 * @param title The title of the painting.
	 * @param description The description of the painting.
	 * @param imagePath The image path of the painting.
	 * @param artist The artist of the painting.
	 * @param creationYear The year of creation of the painting.
	 * @param width The width of the painting.
	 * @param height The height of the painting.
	 */
	public Painting(String title, String description, String imagePath, String artist, int creationYear, double width,
			double height) {
		super(title, description, imagePath, artist, creationYear);

		this.width = width;
		this.height = height;

		this.validate();

		MegaDB.addArtwork(this);
	}

	/**
	 * Get the width of this painting 
	 * @return a decimal representing the width in cm.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Get the height of this painting 
	 * @return a decimal representing the height in cm.
	 */
	public double getHeight() {
		return height;
	}

	@Override
	public String toCsv() {
		StringBuilder builder = new StringBuilder();

		builder.append("painting").append(",");
		builder.append(this.getId()).append(",");
		builder.append(FileHandler.escape(this.getName())).append(",");
		builder.append(FileHandler.escape(this.getDescription())).append(",");
		builder.append(FileHandler.escape(this.getImagePath())).append(",");
		builder.append(FileHandler.escape(this.getArtist())).append(",");
		builder.append(this.getCreationYear()).append(",");
		builder.append(this.getWidth()).append(",");
		builder.append(this.getHeight()).append(",");

		return builder.toString();
	}

	@Override
	public void validate() throws InvalidDataException {
		super.validate();

		if (Validator.isNegative(width)) {
			throw new InvalidDataException("No Width set!");
		}
		if (Validator.isNegative(height)) {
			throw new InvalidDataException("No Height set!");
		}

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Painting other = (Painting) obj;
		if ((int) height != (int) other.height) {
			return false;
		}
		if ((int) width != (int) other.width) {
			return false;
		}
		return true;
	}

}
