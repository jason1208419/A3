package cs.group11.controllers;

import cs.group11.models.Address;
import cs.group11.models.Auction;
import cs.group11.models.Bid;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;

import java.io.IOException;

public class AuctionListController {
    @FXML
    private ListView<Auction> aucList;


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
        aucList.setCellFactory(param -> new AuctionCell());

        ChangeListener<Auction> onAuctionClick = (observable, oldValue, newValue) -> {
            Auction auction = newValue;
            System.out.println("Clicked on the auction for " + auction.getArtwork().getName());
            // TODO REDIRECT USER TO INDIVIDUAL AUCTION PAGE
        };

        paintBTN.setOnAction((event) -> {
            System.out.println("Painting filter clicked, isTicked = " + paintBTN.isSelected());
        });

        sculptBTN.setOnAction((event) -> {
            System.out.println("Sculpture filter clicked, isTicked = " + sculptBTN.isSelected());
        });
        //TODO IMPLEMENT FILTER

        aucList.getSelectionModel().selectedItemProperty().addListener(onAuctionClick);

        addTestAuctions();
    }

    private void addTestAuctions() {
        String[] adr = new String[2];
        adr[0] = "Testdata";
        adr[1] = "Also test data";
        Address testAdr = new Address(adr,"SA14LU");
        User test = new User("Test","TestA", "TestB","999",testAdr,"res/avatars/creeper.jpg");

        Image artworkImage = new Image("https://www.moma.org/wp/moma_learning/wp-content/uploads/2012/07/Van-Gogh.-Starry-Night-469x376.jpg");
        Painting painting = new Painting("Starry Night", "TestDesc", artworkImage, "Vincent Van Gogh", 1889, 200, 300);


        Auction testAuc = new Auction(test,12,11.00, painting);
        Bid testBid = new Bid (15.25, test, testAuc);
        this.currentAuctions.add(testAuc);
    }


    private class AuctionCell extends ListCell<Auction> {
        private Node node;
        private AuctionCellController controller;

        public AuctionCell() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/auctionCell.fxml"));
                node = loader.load();
                controller = loader.getController();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void updateItem(Auction auction, boolean empty) {
            super.updateItem(auction, empty);

            if (empty) {
                setGraphic(null);
            }

            else {
                controller.viewAuctionInfo(auction);
                setGraphic(node);

            }

        }


    }


}
