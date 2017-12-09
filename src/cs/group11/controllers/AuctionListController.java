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
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
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
    private CheckBox completedBtn;
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
        };


        //Handles event when a filter is clicked
        EventHandler<ActionEvent> onCheckboxClick = (ActionEvent event) -> {
            boolean paintSelected = paintBtn.isSelected();
            boolean sculptureSelected = sculptBtn.isSelected();

            boolean myAuctionSelected = myAuctionsBtn.isSelected();
            boolean completedAuctionsSelected = completedBtn.isSelected();

            if (event.getTarget() == myAuctionsBtn || event.getTarget() == completedBtn) {
                paintBtn.setSelected(false);
                sculptBtn.setSelected(false);
            }

            if (paintSelected || sculptureSelected) {
                myAuctionsBtn.setSelected(false);
                completedBtn.setSelected(false);
            }

            filteredAuctions.setPredicate((Auction a) -> {
                if (paintSelected == sculptureSelected && (!myAuctionSelected && !completedAuctionsSelected)) {
                    return true;
                }

                if (paintSelected) {
                    return a.getArtwork() instanceof Painting;
                }

                if (myAuctionSelected && completedAuctionsSelected) {
                    return a.getCreator().equals(user) && a.isCompleted();
                }

                if (myAuctionSelected) {
                    return a.getCreator().equals(user);
                }

                if (completedAuctionsSelected) {
                    return a.isCompleted();
                }



                return a.getArtwork() instanceof Sculpture;
            });
        };

		paintBtn.setOnAction(onCheckboxClick);
		sculptBtn.setOnAction(onCheckboxClick);
        myAuctionsBtn.setOnAction(onCheckboxClick);
        completedBtn.setOnAction(onCheckboxClick);


		filterAuc.getSelectionModel().selectedItemProperty().addListener(auctionClicked);
	}

	public void updateAuctionList() {
        currentAuctions = FXCollections.observableArrayList(MegaDB.getAuctions());
        filteredAuctions = new FilteredList<>(currentAuctions, s -> true);
        filterAuc.setItems(filteredAuctions);
    }

    public void setOnAuctionClick(OnAuctionClick onAuctionClick) {
        this.onAuctionClick = onAuctionClick;
    }
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


    public void createAuctionClick() throws IOException {
	    onHeaderAction.createAuctionsClick();
    }

    public void avatarClick() throws IOException {
        onHeaderAction.browseProfileClick();
    }

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
