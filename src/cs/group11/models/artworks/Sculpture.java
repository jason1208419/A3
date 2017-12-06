package cs.group11.models.artworks;

import java.util.List;

import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
import cs.group11.interfaces.Validatable;
import cs.group11.models.Artwork;
import javafx.scene.image.Image;

public class Sculpture extends Artwork implements Validatable {
	private double width;
	private double height;
	private double depth;
	private String material;

	private List<String> photos;

	public Sculpture(int id, String title, String description, String imagePath, String artist, int creationYear, double width,
					 double height, double depth, String material, List<String> photos) {
		super(id, title, description, imagePath, artist, creationYear);

		this.width = width;
		this.height = height;
		this.depth = depth;
		this.material = material;
		this.photos = photos;

		this.validate();
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
		builder.append(this.getName()).append(",");
		builder.append(this.getDescription()).append(",");
		builder.append(this.getImagePath()).append(",");
		builder.append(this.getArtist()).append(",");
		builder.append(this.getCreationYear()).append(",");
		builder.append(this.getWidth()).append(",");
		builder.append(this.getHeight()).append(",");
		builder.append(this.getDepth()).append(",");
		builder.append(this.getMaterial()).append(",");

		for (String photo : photos) {
			builder.append(photo).append(";");
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
}
