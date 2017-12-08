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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
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

        //Handles event when user clicks on an auction
        ChangeListener<Auction> onAuctionClick = (observable, oldValue, newValue) -> {
            System.out.println("Clicked on the auction for " + newValue.getArtwork().getName());

            ViewAuctionController controller = new ViewAuctionController();
            controller.setAuction(newValue);

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

                return a.getArtwork() instanceof Sculpture;
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
