package cs.group11.drawing.tools;

import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * @author Filippos Pantekis
 * An abstract class to model a drawing brush tool.
 */
public abstract class AbstractDrawingBrush extends AbstractDrawingTool {

	/**
	 * Initialize an AbstractDrawingBrush
	 * @see {@link AbstractDrawingTool#AbstractDrawingTool(String, String, int)}
	 */
	public AbstractDrawingBrush(String name, String description, int initialSize) {
		super(name, description, initialSize);
	}

	@Override
	public void mouseClicked(GraphicsContext on, int x, int y) {
		draw(on, x, y);
	}

	@Override
	public void mouseDragged(GraphicsContext on, int x, int y) {
		draw(on, x, y);
	}

	@Override
	public void toolStateChanged(boolean isSelected) {
	}

	@Override
	public Cursor getCursor() {
		Canvas c = new Canvas(getRelativeToolSize(), getRelativeToolSize());
		GraphicsContext gc = c.getGraphicsContext2D();
		draw(gc, 0, 0);
		return new ImageCursor(transparentCanvasSnapshot(c));
	}

	/**
	 * Draw this brush at a perticular point of the graphics context
	 * @param on Tge graphics context to draw on.
	 * @param x Point X on the canvas.
	 * @param y Point Y on the canvas.
	 */
	protected void draw(GraphicsContext on, int x, int y) {
		on.setStroke(getColor());
		on.setFill(getColor());// Prepare the context before invoking handleDraw
								// Save the subclass a bit of time.
		handleDraw(on, x, y);
	}

	/**
	 * Let the subclass handle drawing.
	 * @param on The graphics context to draw on.
	 * @param x Point X on the canvas.
	 * @param y Point Y on the canvas.
	 */
	protected abstract void handleDraw(GraphicsContext on, int x, int y);

}
