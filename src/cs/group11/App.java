package cs.group11;

import cs.group11.controllers.*;
import cs.group11.interfaces.OnAuctionClick;
import cs.group11.interfaces.OnHeaderAction;
import cs.group11.interfaces.OnSubmitClick;
import cs.group11.interfaces.OnUserClick;
import cs.group11.models.Address;
import cs.group11.models.Auction;
import cs.group11.models.Bid;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class App extends Application implements OnHeaderAction, OnAuctionClick, OnUserClick {
    private Stage primaryStage;

    private Scene signInScene;
    private Scene signUpScene;
    private Scene auctionListScene;
    private Scene createAuctionScene;
    private Scene editProfileScene;
    private Scene browseProfileScene;
    private Scene auctionScene;

    private CreateAuctionController createAuctionController;
    private AuctionListController auctionListController;
    private ProfileController profileController;
    private ViewAuctionController viewAuctionController;
    private EditProfileController editProfileController;

    public static void addTestData() {
        addTestUser();
        addTestAuction();
        addTestBids();
    }

    private static void addTestUser() {
        if (MegaDB.getUsers().size() == 0) {
            Address creatorAddress = new Address(new String[]{"29 Flintstones Avenue", "Ding Dong Street", "UK"}, "PDT 0KL");
            User creator = new User(0, new Date(), "admin", "Nasir", "Al Jabbouri", "07481173742", creatorAddress, "res/avatars/creeper.jpg");

            Address bidderAddress = new Address(new String[]{"29 Flintstones Avenue", "Ding Dong Street", "UK"}, "PDT 0KL");
            User bidder = new User(1, new Date(), "bidder", "Not Nasir", "Not Al Jabbouri", "072481844193", bidderAddress, "/res/avatars/creeper.jpg");
        }
    }

    private static void addTestAuction() {
        User creator = MegaDB.getUsers().get(0);

        if (MegaDB.getAuctions().size() == 0) {
            String description = "The Starry Night is an oil on canvas by the Dutch post-impressionist painter Vincent van Gogh. Painted in June 1889, " +
                    "it depicts the view from the east-facing window of his asylum room at Saint-Rémy-de-Provence, just before sunrise, with the addition " +
                    "of an idealized village";

            String artworkImagePath = "https://www.moma.org/wp/moma_learning/wp-content/uploads/2012/07/Van-Gogh.-Starry-Night-469x376.jpg";
            Painting painting = new Painting("Starry Night", description, artworkImagePath, "Vincent Van Gogh", 1889, 200, 300);
            Auction auction = new Auction(creator, 7, 10.00, painting);
        }
    }

    private static void addTestBids() {
        User bidder = MegaDB.getUsers().get(1);
        Auction auction = MegaDB.getAuctions().get(0);

        if (auction.getBids().size() == 0) {
            Bid bid = new Bid(auction.getReservePrice() + 2, bidder, auction);
        }
    }

    private void setupAllPages() throws IOException {
        setupAuctionPage();
        setupAuctionListPage();
        setupCreateAuctionPage();
        setupBrowseProfilePage();
        setupEditProfilePage();
        setupCreateAuctionPage();
    }

    private void setupSigninPage() throws IOException {
        SignInController signInController = new SignInController();

        signInController.setOnSubmitClick((Object o) -> {
            try {
                setupAllPages();
                auctionListController.updateAuctionList();
                primaryStage.setScene(auctionListScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        signInController.setOnSignupClick((Object o) -> {
            primaryStage.setScene(signUpScene);
        });

        FXMLLoader signInLoader = new FXMLLoader(getClass().getResource("views/signIn.fxml"));
        signInLoader.setController(signInController);

        signInScene = new Scene(signInLoader.load(), 600, 500);
    }

    private void setupSignupPage() throws IOException {
        SignUpController signUpController = new SignUpController();

        signUpController.setOnCancelClick((Object o) -> {
            primaryStage.setScene(signInScene);
        });

        signUpController.setOnSubmitClick(new OnSubmitClick() {
            @Override
            public void submit(Object o) {
                try {
                    setupAllPages();
                    auctionListController.updateAuctionList();
                    primaryStage.setScene(auctionListScene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        FXMLLoader signUpLoader = new FXMLLoader(getClass().getResource("views/signUp.fxml"));
        signUpLoader.setController(signUpController);

        signUpScene = new Scene(signUpLoader.load(), 600, 500);
    }

    private void setupAuctionListPage() throws IOException {
        auctionListController = new AuctionListController();
        auctionListController.setOnAuctionClick(this);
        auctionListController.setOnHeaderAction(this);

        FXMLLoader auctionListLoader = new FXMLLoader(getClass().getResource("views/auctionList.fxml"));
        auctionListLoader.setController(auctionListController);

        auctionListScene = new Scene(auctionListLoader.load(), 600, 500);
    }

    private void setupCreateAuctionPage() throws IOException {
        createAuctionController = new CreateAuctionController();
        createAuctionController.setOnAuctionClick(this);
        createAuctionController.setOnHeaderAction(this);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/createAuctionV2.fxml"));
        loader.setController(createAuctionController);

        createAuctionScene = new Scene(loader.load(), 600, 500);
    }

    private void setupBrowseProfilePage() throws IOException {
        profileController = new ProfileController();

        profileController.setOnAuctionClick(this);
        profileController.setOnUserClick(this);
        profileController.setOnHeaderAction(this);

        profileController.setOnEditProfileClick((Object o) -> {
            editProfileController.setViewingUser((User) o);
            primaryStage.setScene(editProfileScene);
        });

        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/profile.fxml"));
        loader.setController(profileController);

        browseProfileScene = new Scene(loader.load(), 600, 500);
    }

    private void setupEditProfilePage() throws IOException {
        editProfileController = new EditProfileController();

        editProfileController.setOnCancelClick((Object o) -> {
            profileController.setViewingUser((User) o);
            primaryStage.setScene(browseProfileScene);
        });

        editProfileController.setOnSubmitClick((Object o) -> {
            profileController.setViewingUser((User) o);
            primaryStage.setScene(browseProfileScene);
        });

        editProfileController.setOnAuctionClick(this);
        editProfileController.setOnUserClick(this);
        editProfileController.setOnHeaderAction(this);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/editProfile.fxml"));
        loader.setController(editProfileController);

        editProfileScene = new Scene(loader.load(), 600, 500);
    }

    private void setupAuctionPage() throws IOException {
        viewAuctionController = new ViewAuctionController();
        viewAuctionController.setOnHeaderAction(this);
        viewAuctionController.setOnUserClick(this);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/viewAuction.fxml"));
        loader.setController(viewAuctionController);

        auctionScene = new Scene(loader.load(), 600, 500);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupSigninPage();
        setupSignupPage();

        primaryStage.setTitle("Artatawe");
        primaryStage.setScene(signInScene);
        primaryStage.show();

        this.primaryStage = primaryStage;
    }

    @Override
    public void browseAuctionsClick() {
        auctionListController.updateAuctionList();
        primaryStage.setScene(auctionListScene);
    }

    @Override
    public void createAuctionsClick() {
        primaryStage.setScene(createAuctionScene);
    }

    @Override
    public void browseProfileClick() {
        profileController.setViewingUser(MegaDB.getLoggedInUser());
        primaryStage.setScene(browseProfileScene);
    }

    @Override
    public void logoutClick() {
        MegaDB.logout();
        primaryStage.setScene(signInScene);
    }

    @Override
    public void clicked(Auction auction) {
        viewAuctionController.setAuction(auction);
        primaryStage.setScene(auctionScene);
    }

    @Override
    public void clicked(User user) {
        profileController.setViewingUser(user);
        primaryStage.setScene(browseProfileScene);
    }
}
