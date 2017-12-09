package cs.group11.drawing.tools;

import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A tool to erase drawn elements
 * @author Filippos Pantekis
 */
public class EraserBrush extends AbstractDrawingBrush {

	/**
	 * Construct an eraser with a specified default size
	 * @param initialSize the default size.
	 */
	public EraserBrush(int initialSize) {
		super("Eraser", "Drag over previously drawn items to erase them.", initialSize);
	}

	@Override
	protected void handleDraw(GraphicsContext on, int x, int y) {
		on.clearRect(x, y, getRelativeToolSize(), getRelativeToolSize());
	}

	@Override
	public Cursor getCursor() {
		//Create a custom cursor, just the outline of a rectangle.
		Canvas c = new Canvas(getRelativeToolSize(), getRelativeToolSize());
		GraphicsContext gc = c.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		gc.strokeRect(0, 0, getRelativeToolSize(), getRelativeToolSize());
		return new ImageCursor(transparentCanvasSnapshot(c));
	}
	
}
