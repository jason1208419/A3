package cs.group11.controllers;

import cs.group11.Main;
import cs.group11.models.Address;
import cs.group11.models.Auction;
import cs.group11.models.Bid;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles fxml file that displays list of ongoing auctions.
 *
 * @Author Thomas Collett
 */
public class AuctionListController {

    @FXML
    private CheckBox paintBtn;
    @FXML
    private CheckBox sculptBtn;
    @FXML
    private ListView<Auction> filterAuc;

    private ObservableList<Auction> currentAuctions;
    private FilteredList<Auction> filteredAuctions;


    @FXML
    /**
     * Binds components and fills list with details about ongoing auctions.
     */
    protected void initialize() {
        currentAuctions = FXCollections.observableArrayList();
        filteredAuctions = new FilteredList<>(currentAuctions, s -> true);
        filterAuc.setItems(filteredAuctions);
        filterAuc.setCellFactory(param -> new AuctionCell());

        //Handles event when user clicks on an auction
        ChangeListener<Auction> onAuctionClick = (observable, oldValue, newValue) -> {


            if (newValue == null) {
                return;
            }

            Auction auction = newValue;
            System.out.println("Clicked on the auction for " + auction.getArtwork().getName());

            
            switchScreen(auction);
        };


        //Handles event when a filter is clicked
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


                if (sculptureSelected) {
                    return a.getArtwork() instanceof Sculpture;
                }


                return false;


            });
        };

        paintBtn.setOnAction(onCheckboxClick);
        sculptBtn.setOnAction(onCheckboxClick);


        filterAuc.getSelectionModel().selectedItemProperty().addListener(onAuctionClick);

        addTestAuctions();
    }

    /**
     * Switches to the individual auction screen.
     *
     * @param auction The individual auction to be viewed.
     */
    private void switchScreen(Auction auction) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/viewAuction.fxml"));
            Parent root = loader.load();

            ViewAuctionController controller = loader.getController();
            controller.setAuction(auction);
            Scene viewAuc = new Scene(root, 600, 500);
            Stage primaryStage = Main.getPrimaryStage();
            primaryStage.setScene(viewAuc);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds test data into the auction list.
     */
    private void addTestAuctions() {
        String[] adr = new String[2];
        adr[0] = "Testdata";
        adr[1] = "Also test data";
        Address testAdr = new Address(adr, "SA14LU");
        User test = new User("Test", "TestA", "TestB", "999", testAdr, "res/avatars/creeper.jpg");

        String imagePath = "https://www.moma.org/wp/moma_learning/wp-content/uploads/2012/07/Van-Gogh.-Starry-Night-469x376.jpg";
        Painting painting = new Painting("Starry Night", "TestDesc", imagePath, "Vincent Van Gogh", 1889, 200, 300);


        Auction testAuc = new Auction(test, 12, 11.00, painting);
        Bid testBid = new Bid(15.25, test, testAuc);
        this.currentAuctions.add(testAuc);


        String[] adr2 = new String[2];
        adr2[0] = "A";
        adr2[1] = "B";
        Address testAdr2 = new Address(adr2, "SA27QG");
        User test2 = new User("Test1", "TestA", "TestB", "911", testAdr2, "res/avatars/creeper.jpg");
        String imagePath2 = "res/avatars/creeper.jpg";
        ArrayList<String> imageLocations = new ArrayList<>();
        imageLocations.add(imagePath2);

        Sculpture s = new Sculpture("Creeper", "Sample text", imagePath2, "Fillipos",
                2017, 342, 201, 300, "Green stuff", imageLocations);

        Auction testAuc2 = new Auction(test2, 15, 12.25, s);
        Bid testBid2 = new Bid(12.76, test2, testAuc2);
        this.currentAuctions.add(testAuc2);
    }

    /**
     * A custom ListCell to store auction data.
     */
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
