package cs.group11.drawing.tools;

import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * @author Filippos Pantekis
 * AAn abstract class to model a drawing tool.
 *
 */
public abstract class AbstractDrawingTool {

	/**
	 * a textual description of the tool.
	 */
	private final String description;
	/**
	 * The name of the tool
	 */
	private final String name;
	/**
	 * The color of the tool
	 */
	private Color toolColor;
	/**
	 * indicates wether or not to draw a filled shape.
	 */
	private boolean filled;
	/**
	 * The tool size
	 */
	private int relativeToolSize;

	/**
	 * Constructs an {@link AbstractDrawingTool}
	 * @param name The name of the tool.
	 * @param description A short tectual description of this tool.
	 * @param initialSize The initial size of this tool
	 */
	public AbstractDrawingTool(String name, String description, int initialSize) {
		this.name = name;
		this.description = description;
		this.relativeToolSize = initialSize;
	}

	/**
	 * Get the name of this tool
	 * @return the name of the tool
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the description of the tool.
	 * @return the description of the tool as a String.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get the color of the tool.
	 * @return a Color object which is the current tol color.
	 */
	public Color getColor() {
		return toolColor;
	}

	/**
	 * Make the too draw a filled or unfilled version of it's shape.
	 * Note: Some shapes may not support this feature.
	 * @param filled if true, the drawn shape is filled, unfilled otherwise
	 */
	public void setFilled(boolean filled) {
		this.filled = filled;
	}

	/**
	 * Check if the shape is filled.
	 * @return true if the sape is filled false otherwise  
	 */
	public boolean isFilled() {
		return filled;
	}

	/**
	 * Set the color of the shape.
	 * @param fillColor The new color of the shape.
	 */
	public void setColor(Color fillColor) {
		this.toolColor = fillColor;
	}

	/**
	 * Get the relative size of this tool.
	 * @return the size of this tool.
	 */
	public int getRelativeToolSize() {
		return relativeToolSize;
	}

	/**
	 * Set the size of this tool
	 * @param relativeToolSize the new size
	 */
	public void setRelativeToolSize(int relativeToolSize) {
		this.relativeToolSize = relativeToolSize;
	}

	/**
	 * Produces a snapshot of a Canvas with transparent background.
	 * @param canvas the canvas to capture
	 * @return an Image snapshot.
	 */
	protected Image transparentCanvasSnapshot(Canvas canvas) {
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		return canvas.snapshot(params, null);
	}

	/**
	 * Get a Cursor instance that represents this tool.
	 * @return A Cursor provided by the tool.
	 */
	public abstract Cursor getCursor();

	/**
	 * When the mouse is clicked, this method is called to inform the subclass.
	 * @param on The GraphicsContext on which to draw the shape
	 * @param x Point X of the mouse
	 * @param y Point Y of the mouse 
	 */
	public abstract void mouseClicked(GraphicsContext on, int x, int y);

	/**
	 * When the mouse is clicked, this method is called to inform the subclass.
	 * @param on The GraphicsContext on which to draw the shape
	 * @param x Point X of the mouse
	 * @param y Point Y of the mouse 
	 */
	public abstract void mouseDragged(GraphicsContext on, int x, int y);

	/**
	 * Invoked to infrom the subclass about a change of tool in the user interface
	 * allowing it to dispose of previously stored data or initialize others.
	 * @param isSelected True of this tool was selected false if it was unselected.
	 */
	public abstract void toolStateChanged(boolean isSelected);// Called when the tool is selected or unselected

}
