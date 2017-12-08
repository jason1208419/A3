package cs.group11.controllers;

import java.io.IOException;
import java.util.List;

import cs.group11.Main;
import cs.group11.MegaDB;
import cs.group11.interfaces.OnHeaderAction;
import cs.group11.models.Auction;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
import cs.group11.models.artworks.Sculpture;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Handles fxml file that displays list of ongoing auctions.
 *
 * @Author Thomas Collett
 */
public class AuctionListController {
	@FXML
	private ImageView avatar1;
	@FXML
	private Label username1;

	@FXML
	private CheckBox paintBtn;
	@FXML
	private CheckBox sculptBtn;
	@FXML
	private ListView<Auction> filterAuc;

	@FXML
	private VBox rootBox;

	private ObservableList<Auction> currentAuctions;
	private FilteredList<Auction> filteredAuctions;

	private OnHeaderAction headerAction;
	private User user;

	@FXML
	/**
	 * Binds components and fills list with details about ongoing auctions.
	 */
	protected void initialize() {
		this.user = Main.getCurrentUser();
		Image avatarImage = new Image(user.getAvatarPath());
		this.avatar1.setImage(avatarImage);
		this.username1.setText(user.getUsername());

		currentAuctions = FXCollections.observableArrayList();
		filteredAuctions = new FilteredList<>(currentAuctions, s -> true);
		filterAuc.setItems(filteredAuctions);
		filterAuc.setCellFactory(param -> new AuctionCell());

		// Handles event when user clicks on an auction
		ChangeListener<Auction> onAuctionClick = (observable, oldValue, newValue) -> {
			System.out.println("Clicked on the auction for " + newValue.getArtwork().getName());

			ViewAuctionController controller = new ViewAuctionController();
			controller.setAuction(newValue);
			controller.setHeaderAction(new OnHeaderAction() {

				@Override
				public void logoutClick() {
					VBox box;
					try {
						box = FXMLLoader.load(getClass().getResource("../views/signIn.fxml"));
						box.prefHeightProperty().bind(rootBox.heightProperty());
						rootBox.getChildren().setAll(box);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void createAuctionsClick() {
					CreateAuctionV2Controller controller = new CreateAuctionV2Controller();

					FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/createAuctionV2.fxml"));
					loader.setController(controller);
					VBox box = null;
					try {
						box = loader.load();
					} catch (IOException e) {
						e.printStackTrace();
					}

					box.prefHeightProperty().bind(rootBox.heightProperty());

					rootBox.getChildren().setAll(box);

				}

				@Override
				public void browseProfileClick() {
					ProfileController profileCon = new ProfileController();
					profileCon.setLoginedUser(Main.getCurrentUser());
					profileCon.setViewingUser(Main.getCurrentUser());

					FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/profile.fxml"));
					loader.setController(profileCon);
					profileCon.addTestBids();

					VBox box = null;
					try {
						box = loader.load();
					} catch (IOException e) {
						e.printStackTrace();
					}

					box.prefHeightProperty().bind(rootBox.heightProperty());

					rootBox.getChildren().setAll(box);

				}

				@Override
				public void browseAuctionsClick() {
					AuctionListController controller = new AuctionListController();

					FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/auctionList.fxml"));
					loader.setController(controller);
					VBox box = null;
					try {
						box = loader.load();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					box.prefHeightProperty().bind(rootBox.heightProperty());

					rootBox.getChildren().setAll(box);
				}
			});

			FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/viewAuction.fxml"));
			loader.setController(controller);
			VBox box = null;
			try {
				box = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (box != null) {
				box.prefHeightProperty().bind(rootBox.heightProperty());
			}
			rootBox.getChildren().setAll(box);
		};

		// Handles event when a filter is clicked
		EventHandler<ActionEvent> onCheckboxClick = (ActionEvent event) -> {
			boolean paintSelected = paintBtn.isSelected();
			boolean sculptureSelected = sculptBtn.isSelected();

			filteredAuctions.setPredicate((Auction a) -> {
				if (paintSelected == sculptureSelected) {
					return true;
				}

				if (paintSelected) {
					return a.getArtwork() instanceof Painting;
				}

				return a.getArtwork() instanceof Sculpture;
			});
		};

		paintBtn.setOnAction(onCheckboxClick);
		sculptBtn.setOnAction(onCheckboxClick);

		filterAuc.getSelectionModel().selectedItemProperty().addListener(onAuctionClick);

		List<Auction> aucList = MegaDB.getAuctions();
		for (Auction auction : aucList) {
			currentAuctions.add(auction);
		}
	}

	public void setHeaderAction(OnHeaderAction headerAction) {
		this.headerAction = headerAction;
	}

	/**
	 * Switches to the individual auction screen.
	 *
	 * @param auction The individual auction to be viewed.
	 */
	private void switchScreen(Auction auction) {
		try {
			ViewAuctionController controller = new ViewAuctionController();
			controller.setUser(this.user);
			controller.setAuction(auction);

			FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/viewAuction.fxml"));
			loader.setController(controller);
			VBox box = loader.load();

			box.prefHeightProperty().bind(rootBox.heightProperty());
			box.prefWidthProperty().bind(rootBox.widthProperty());
			rootBox.getChildren().setAll(box);
		} catch (IOException e) {
			System.out.println("Failed to load fxml file");
		}
	}

	public void createAuctionClick() throws IOException {
		CreateAuctionV2Controller controller = new CreateAuctionV2Controller();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/createAuctionV2.fxml"));
		loader.setController(controller);
		VBox box = loader.load();

		box.prefHeightProperty().bind(rootBox.heightProperty());

		rootBox.getChildren().setAll(box);
	}

	public void avatarClick() throws IOException {
		ProfileController profileCon = new ProfileController();
		profileCon.setLoginedUser(this.user);
		profileCon.setViewingUser(this.user);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/profile.fxml"));
		loader.setController(profileCon);
		profileCon.addTestBids();

		VBox box = loader.load();

		box.prefHeightProperty().bind(rootBox.heightProperty());

		rootBox.getChildren().setAll(box);
	}

	public void logoutClick() throws IOException {
		VBox box = FXMLLoader.load(getClass().getResource("../views/signIn.fxml"));
		box.prefHeightProperty().bind(rootBox.heightProperty());
		rootBox.getChildren().setAll(box);
	}

	/**
	 * A custom ListCell to store auction data.
	 */
	private class AuctionCell extends ListCell<Auction> {
		private Node node;
		private AuctionCellController controller;

		private AuctionCell() {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/auctionCell.fxml"));
				node = loader.load();
				controller = loader.getController();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		/**
		 * Fills the cell with data about an auction.
		 */
		protected void updateItem(Auction auction, boolean empty) {
			super.updateItem(auction, empty);

			if (empty) {
				setGraphic(null);
			} else {
				controller.viewAuctionInfo(auction);
				setGraphic(node);
			}
		}
	}
}
