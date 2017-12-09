package cs.group11.controllers;

import cs.group11.drawing.tools.AbstractDrawingTool;
import cs.group11.drawing.tools.CircleBrush;
import cs.group11.drawing.tools.LineTool;
import cs.group11.interfaces.OnSubmitClick;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Filippos Pantekis
 * The FXML controller used for the Avatar Drawing pane.
 */
public class DrawController {

	/**
	 * Constant with the default tool size.
	 */
	private static final int DEFAULT_TOOL_SIZE = 10;
	
	
	/**
	 * A constant array with the vailable drawing tools used to
	 * dynamically generate the gui representation.
	 */
	private static final AbstractDrawingTool[] AVAILABLE_TOOLS = { new LineTool(DEFAULT_TOOL_SIZE),
			new CircleBrush(DEFAULT_TOOL_SIZE) };

	@FXML
	private Slider sizeSlider;
	@FXML
	private Label infoLabel;
	@FXML
	private Label toolName;
	@FXML
	private ColorPicker colorPick;
	@FXML
	private CheckBox filled;
	@FXML
	private Canvas canvas;
	@FXML
	private ToolBar tools;
	@FXML
	private Button clear;

	private GraphicsContext canvasGraphics;
	private ToggleGroup radioButtonGroup = new ToggleGroup();
	private AbstractDrawingTool selectedTool;
	private int CANVAS_WIDTH = 388;
	private int CANVAS_HEIGHT = 368;
	private OnSubmitClick onViewSubmit;

	/**
	 * Initialize the GUI
	 */
	@FXML
	protected void initialize() {
		//Action listeners for clear button
		clear.setOnAction((e) -> canvasGraphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()));


		//Initialize colorpick
		colorPick.setValue(Color.BLACK);
		colorPick.setOnAction((e) -> {
			selectedTool.setColor(colorPick.getValue());
			updateTool();
		});

		//Initialize toolbar
		initializeToolbar();

		radioButtonGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> value, Toggle oldValue, Toggle newValue) {
				selectTool(Integer.parseInt(((RadioButton) newValue).getId()));
			}
		});

		radioButtonGroup.selectToggle(radioButtonGroup.getToggles().get(0));// Select first tool by default
		selectTool(0);

		//Initialize Canvas
		canvasGraphics = canvas.getGraphicsContext2D();
		canvas.setOnMouseClicked((e) -> selectedTool.mouseClicked(canvasGraphics, (int) e.getX(), (int) e.getY()));
		canvas.setOnMouseDragged((e) -> selectedTool.mouseDragged(canvasGraphics, (int) e.getX(), (int) e.getY()));

		//Initialize 'Filled' checkbox
		filled.setOnAction((e) -> {
			selectedTool.setFilled(filled.isSelected());
			updateTool();
		});

		//Initialize Slider.
		sizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				selectedTool.setRelativeToolSize(newValue.intValue());
				updateTool();
			}
		});
	}

	/**
	 * A method used to generate GUI components to represent tools 
	 * and then populate the ToolBar with them
	 */
	private void initializeToolbar() {
		for (int i = 0; i < AVAILABLE_TOOLS.length; i++) {
			AbstractDrawingTool tool = AVAILABLE_TOOLS[i];
			RadioButton button = new RadioButton(tool.getName());
			button.setId(String.valueOf(i));// Sceen graph id = tool array index.
			button.setTooltip(new Tooltip(tool.getDescription()));
			radioButtonGroup.getToggles().add(button);
			tools.getItems().add(button);
		}
	}

	/**
	 * Update the selected tool with the user inputs.
	 */
	private void updateTool() {
		selectedTool.setColor(colorPick.getValue());
		selectedTool.toolStateChanged(true);
		selectedTool.setFilled(filled.isSelected());
		selectedTool.setRelativeToolSize((int) sizeSlider.getValue());
		canvas.setCursor(selectedTool.getCursor());
	}

	/**
	 * Select a specific tool.
	 * @param index the position of the tool in the {@link #AVAILABLE_TOOLS} array.
	 */
	private void selectTool(int index) {
		if (selectedTool != null)
			selectedTool.toolStateChanged(false);
		selectedTool = AVAILABLE_TOOLS[index];
		updateTool();
		toolName.setText(selectedTool.getName() + ":");
		infoLabel.setText(" " + selectedTool.getDescription());
	}

	public String saveImage() throws IOException {
		FileChooser fileChooser = new FileChooser();

		//Set extension filter
		FileChooser.ExtensionFilter extFilter =
				new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showSaveDialog(null);

		if (file != null) {
			WritableImage writableImage = new WritableImage(CANVAS_WIDTH, CANVAS_HEIGHT);
			canvas.snapshot(null, writableImage);
			RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
			ImageIO.write(renderedImage, "png", file);
			return file.toURI().toString();
		}
		return null;
	}

	public void setOnSave(OnSubmitClick onViewSubmit) {
		this.onViewSubmit = onViewSubmit;
	}

	@FXML
	public void saveClick() throws IOException {
		onViewSubmit.submit(saveImage());
	}

}
