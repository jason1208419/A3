package cs.group11.models.artworks;

import java.util.List;

import cs.group11.FileHandler;
import cs.group11.MegaDB;
import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
import cs.group11.interfaces.Validatable;
import cs.group11.models.Artwork;

public class Sculpture extends Artwork implements Validatable {
	private double width;
	private double height;
	private double depth;
	private String material;

	private List<String> photos;

	public Sculpture(int id, String title, String description, String imagePath, String artist, int creationYear,
			double width, double height, double depth, String material, List<String> photos) {
		super(id, title, description, imagePath, artist, creationYear);

		this.width = width;
		this.height = height;
		this.depth = depth;
		this.material = material;
		this.photos = photos;

		this.validate();

		MegaDB.addArtwork(this);
	}

	public Sculpture(String title, String description, String imagePath, String artist, int creationYear, double width,
			double height, double depth, String material, List<String> photos) {
		super(title, description, imagePath, artist, creationYear);

		this.width = width;
		this.height = height;
		this.depth = depth;
		this.material = material;
		this.photos = photos;

		this.validate();

		MegaDB.addArtwork(this);
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getDepth() {
		return depth;
	}

	public String getMaterial() {
		return material;
	}

	public List<String> getPhotos() {
		return photos;
	}

	@Override
	public String toCsv() {
		StringBuilder builder = new StringBuilder();

		builder.append("sculpture").append(",");
		builder.append(this.getId()).append(",");
		builder.append(FileHandler.escape(this.getName())).append(",");
		builder.append(FileHandler.escape(this.getDescription())).append(",");
		builder.append(FileHandler.escape(this.getImagePath())).append(",");
		builder.append(FileHandler.escape(this.getArtist())).append(",");
		builder.append(this.getCreationYear()).append(",");
		builder.append(this.getWidth()).append(",");
		builder.append(this.getHeight()).append(",");
		builder.append(this.getDepth()).append(",");
		builder.append(FileHandler.escape(this.getMaterial())).append(",");

		if (photos.size() == 0) {
			builder.append("[]");
		} else {
			for (String photo : photos) {
				builder.append(FileHandler.escape(photo)).append(";");
			}
		}
		builder.append(",");

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
		if (Validator.isNegative(depth)) {
			throw new InvalidDataException("No depth set!");
		}
		if (Validator.isStringEmpty(material)) {
			throw new InvalidDataException("No material set!");
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Sculpture)) {
			return false;
		}
		Sculpture other = (Sculpture) obj;
		if ((int) depth != (int) other.depth) {
			return false;
		}
		if ((int) height != (int) other.height) {
			return false;
		}
		if (!material.equals(other.material)) {
			return false;
		}
		if (!photos.equals(other.photos)) {
			return false;
		}
		if ((int) width != (int) other.width) {
			return false;
		}
		return true;
	}

}
