package cs.group11.controllers;

import java.io.IOException;
import java.util.List;

import cs.group11.MegaDB;
import cs.group11.interfaces.OnAuctionClick;
import cs.group11.interfaces.OnHeaderAction;
import cs.group11.models.Auction;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
import cs.group11.models.artworks.Sculpture;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private CheckBox myAuctionsBtn;

    @FXML
    private ListView<Auction> filterAuc;

    @FXML
    private VBox rootBox;

    private ObservableList<Auction> currentAuctions;
    private FilteredList<Auction> filteredAuctions;

    private User user;
    private OnAuctionClick onAuctionClick;
    private OnHeaderAction onHeaderAction;

    @FXML
    /**
     * Binds components and fills list with details about ongoing auctions.
     */
    protected void initialize() {
        this.user = MegaDB.getLoggedInUser();
        Image avatarImage = new Image(user.getAvatarPath());
        this.avatar1.setImage(avatarImage);
        this.username1.setText(user.getUsername());

        currentAuctions = FXCollections.observableArrayList();

        filteredAuctions = new FilteredList<>(currentAuctions, s -> true);
        filterAuc.setItems(filteredAuctions);
        filterAuc.setCellFactory(param -> new AuctionCell());

        //Handles event when user clicks on an auction
        ChangeListener<Auction> auctionClicked = (observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }

            onAuctionClick.clicked(newValue);

            //Prevents unassigned values being passed between ChangeListeners and EventHandlers
            Platform.runLater(() -> {
                filterAuc.getSelectionModel().clearSelection();
            });
        };


        //Handles event when a filter is clicked
        EventHandler<ActionEvent> onCheckboxClick = (ActionEvent event) -> {
            refilterAuctions(event);
        };

        paintBtn.setOnAction(onCheckboxClick);
        sculptBtn.setOnAction(onCheckboxClick);
        myAuctionsBtn.setOnAction(onCheckboxClick);


        filterAuc.getSelectionModel().selectedItemProperty().addListener(auctionClicked);

        refilterAuctions(null);
    }

    private void refilterAuctions(Event event) {
        boolean paintSelected = paintBtn.isSelected();
        boolean sculptureSelected = sculptBtn.isSelected();

        boolean myAuctionSelected = myAuctionsBtn.isSelected();

        if (event != null && event.getTarget() == myAuctionsBtn) {
            paintBtn.setSelected(false);
            sculptBtn.setSelected(false);
        }

        if (paintSelected) {
            myAuctionsBtn.setSelected(false);
        }

        filteredAuctions.setPredicate((Auction a) -> {
            if (paintSelected == sculptureSelected && !myAuctionSelected) {
                return !a.isCompleted();
            }

            if (paintSelected) {
                return a.getArtwork() instanceof Painting && !a.isCompleted();
            }

            if (myAuctionSelected) {
                return a.getCreator().equals(user);
            }

            return a.getArtwork() instanceof Sculpture && !a.isCompleted();
        });
    }

    /**
     * Adds list of filtered auctions into GUI
     */
	public void updateAuctionList() {
        currentAuctions = FXCollections.observableArrayList(MegaDB.getAuctions());
        filteredAuctions = new FilteredList<>(currentAuctions, s -> true);
        filterAuc.setItems(filteredAuctions);
        refilterAuctions(null);
    }

    /**
     * Sets actions to be performed when an auction is clicked
     * @param onAuctionClick A collection of actions to perform when an auction is clicked.
     */
    public void setOnAuctionClick(OnAuctionClick onAuctionClick) {
        this.onAuctionClick = onAuctionClick;
    }

    /**
     * Sets actions to be performed when something in the header is clicked
     * @param onHeaderAction A collection of actions to perform when something in the header is clicked.
     */
    public void setOnHeaderAction(OnHeaderAction onHeaderAction) {
        this.onHeaderAction = onHeaderAction;
    }

    /**
	 * Switches to the individual auction screen.
	 *
	 * @param auction The individual auction to be viewed.
	 */
	private void switchScreen(Auction auction) {
	    onAuctionClick.clicked(auction);
	}


    /**
     * Switches to create auction screen
     * @throws IOException Thrown if fxml file fails to load
     */
    public void createAuctionClick() throws IOException {
	    onHeaderAction.createAuctionsClick();
    }

    /**
     * Switches to screen showing a user's profile
     * @throws IOException Thrown if fxml file fails to load
     */
    public void avatarClick() throws IOException {
        onHeaderAction.browseProfileClick();
    }

    /**
     * Logs a user out and switches them to the login screen
     * @throws IOException Thrown if fxml file fails to load
     */
    public void logoutClick() throws IOException {
        onHeaderAction.logoutClick();
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
