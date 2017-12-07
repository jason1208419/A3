package cs.group11.drawing.tools;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * @author Filippos Pantekis
 * An {@link AbstractDrawingBrush} implementation drawing circles
 */
public class LineTool extends AbstractDrawingTool {

	/**
	 * The default thickness of this line.
	 * Used to restore after drawing in the GraphicsContext
	 */
	private static final int DEFAULT_LINE_THICKNESS = 1;
	/**
	 * The width and height of the oval drawn when clicking point1
	 * to inicate the first selection the user made.
	 */
	private static final int POINT_DIMENTIONS = 2;

	/**
	 * Accumulator variable used to keep track of point 1 untill
	 * point 2 is clicked by the user
	 */
	private Point2D firstSelection;

	/**
	 * Initialize the tool with a specific initial size.
	 * @param initialSize
	 */
	public LineTool(int initialSize) {
		super("Line Tool", "Click on point 1 and then point 2 to draw a straight line between them.", initialSize);
	}

	@Override
	public void mouseClicked(GraphicsContext on, int x, int y) {
		on.setStroke(getColor());
		if (firstSelection == null) {
			firstSelection = new Point2D(x, y);
			on.strokeOval(x, y, POINT_DIMENTIONS, POINT_DIMENTIONS);
		} else {
			int xFrom = (int) firstSelection.getX();
			int yFrom = (int) firstSelection.getY();
			on.clearRect(xFrom, yFrom, POINT_DIMENTIONS, POINT_DIMENTIONS);// Clear old point
			on.setLineWidth(getRelativeToolSize());
			on.strokeLine(xFrom, yFrom, x, y);
			on.setLineWidth(DEFAULT_LINE_THICKNESS);// Restore line thickness.
			firstSelection = null;// unselect first point after we drew the line.
		}
	}

	@Override
	public void mouseDragged(GraphicsContext on, int x, int y) {
	}// No drag support

	@Override
	public void toolStateChanged(boolean isSelected) {
		if (firstSelection != null) 
			firstSelection = null;// de-select the point when user changes or re-selects tool
	}

	@Override
	public Cursor getCursor() {// Create a custom cursor for the tool.
		Canvas c = new Canvas(POINT_DIMENTIONS, POINT_DIMENTIONS);
		GraphicsContext gc = c.getGraphicsContext2D();
		gc.strokeOval(0, 0, POINT_DIMENTIONS, POINT_DIMENTIONS);
		return new ImageCursor(transparentCanvasSnapshot(c));
	}

}
