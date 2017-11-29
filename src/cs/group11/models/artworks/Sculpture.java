package cs.group11.models.artworks;

import java.util.List;

import cs.group11.helpers.InvalidDataException;
import cs.group11.interfaces.Validatable;
import cs.group11.models.Artwork;
import javafx.scene.image.Image;

public class Sculpture extends Artwork implements Validatable {
	private double width;
	private double height;
	private double depth;
	private String material;

	private List<Image> photos;

	public Sculpture(String title, String description, Image image, String artist, int creationYear, double width,
			double height, double depth, String material, List<Image> photos) {
		super(title, description, image, artist, creationYear);

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

	public List<Image> getPhotos() {
		return photos;
	}

	@Override
	public void validate() throws InvalidDataException {
		super.validate();
		// TODO: Validate width, height, depth and material
	}
}
