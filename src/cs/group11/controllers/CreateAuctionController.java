package cs.group11.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;

import javafx.scene.layout.FlowPane;

import javafx.scene.image.ImageView;

import javafx.scene.control.RadioButton;

public class CreateAuctionController {
	@FXML
	private ImageView image;
	@FXML
	private RadioButton sculptureRadio;
	@FXML
	private RadioButton paintingRadio;
	@FXML
	private TextField title;
	@FXML
	private TextField auction;
	@FXML
	private TextField startPrice;
	@FXML
	private TextField width;
	@FXML
	private TextField length;
	@FXML
	private TextField creationDate;
	@FXML
	private FlowPane depthPane;
	@FXML
	private TextField depth;
	@FXML
	private FlowPane materialPane;
	@FXML
	private TextField material;
	@FXML
	private Button createButton;

	@FXML
	protected void initialize() {
		
	}

}
