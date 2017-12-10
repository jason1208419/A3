package cs.group11.drawing.tools;

import javafx.scene.canvas.GraphicsContext;

/**
 * @author Filippos Pantekis
 * An {@link AbstractDrawingBrush} implementation drawing squares
 */
public class SquareBrush extends AbstractDrawingBrush {
	
	/**
	 * Initialize the brush with a specific initial size.
	 * @param initialSize
	 */
	public SquareBrush(int initialSize) {
		super("Square Brush", "Click and drag your cursor to draw a trail of squares!", initialSize);
	}

	@Override
	/**
	 * Determines whether to draw with filled squares or squares with empty centres.
	 */
	public void handleDraw(GraphicsContext on, int x, int y) {
		if (isFilled()) {
			on.fillRect(x, y, getRelativeToolSize(), getRelativeToolSize());
		} else {
			on.strokeRect(x, y, getRelativeToolSize(), getRelativeToolSize());
		}
	}

}
