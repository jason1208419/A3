package cs.group11.controllers;

import cs.group11.models.Auction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;

public class AuctionController {
    @FXML
    private ListView aucList;


    @FXML
    private Label filterLBL;
    @FXML
    private CheckBox paintBTN;
    @FXML
    private CheckBox sculptBTN;

    private ObservableList<Auction> currentAuctions;

    @FXML
    protected void initialize() {
        currentAuctions = FXCollections.observableArrayList();
        aucList.setItems(currentAuctions);

    }

    private class AuctionCell extends ListCell<Auction> {
        private Node node;
        private AuctionCellController controller;

        public AuctionCell() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/AuctionCell.fxml"));
                node = loader.load();
                controller = loader.getController();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        /* TO DO
        @Override
        protected void updateItem(Auction auction, boolean empty) {
            super.updateItem(auction, empty);

            if (empty) {
                setGraphic(null);
                setGraphic(null);
            } else {
                controller.se
            }
        }
        */

    }


}
