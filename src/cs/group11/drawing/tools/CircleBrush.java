package cs.group11.drawing.tools;

import javafx.scene.canvas.GraphicsContext;

/**
 * @author Filippos Pantekis
 * An {@link AbstractDrawingBrush} implementation drawing circles
 */
public class CircleBrush extends AbstractDrawingBrush {
	
	/**
	 * Initialize the brush with a specific initial size.
	 * @param initialSize
	 */
	public CircleBrush(int initialSize) {
		super("Circle Brush", "Click and drag your cursor to draw a trail of circles!", initialSize);
	}

	@Override
	/**
	 * Determines whether to draw with filled circles or squares with empty circles.
	 */
	public void handleDraw(GraphicsContext on, int x, int y) {
		if (isFilled()) {
			on.fillOval(x, y, getRelativeToolSize(), getRelativeToolSize());
		} else {
			on.strokeOval(x, y, getRelativeToolSize(), getRelativeToolSize());
		}
	}

}
