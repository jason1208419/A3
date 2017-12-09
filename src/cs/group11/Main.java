package cs.group11;

import java.io.IOException;

import cs.group11.controllers.AuctionListController;
import cs.group11.controllers.CreateAuctionController;
import cs.group11.controllers.EditProfileController;
import cs.group11.controllers.ProfileController;
import cs.group11.controllers.SignInController;
import cs.group11.controllers.SignUpController;
import cs.group11.controllers.ViewAuctionController;
import cs.group11.interfaces.OnAuctionClick;
import cs.group11.interfaces.OnHeaderAction;
import cs.group11.interfaces.OnSubmitClick;
import cs.group11.interfaces.OnUserClick;
import cs.group11.models.Address;
import cs.group11.models.Auction;
import cs.group11.models.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application implements OnHeaderAction, OnAuctionClick, OnUserClick {
	private Stage primaryStage;

	private Scene signInScene;
	private Scene signUpScene;
	private Scene auctionListScene;
	private Scene createAuctionScene;
	private Scene editProfileScene;
	private Scene browseProfileScene;
	private Scene auctionScene;

	private CreateAuctionController createAuctionController;
	private ProfileController profileController;
	private ViewAuctionController viewAuctionController;
	private EditProfileController editProfileController;

	public static void main(String[] args) throws IOException {
		MegaDB.load();

		Address address = new Address(new String[] { "29 Flintstones Avenue", "Ding Dong Street", "UK" }, "PDT 0KL");
		User creator = new User("admin", "Nasir", "Al Jabbouri", "07481173742", address, "res/avatars/creeper.jpg");

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				MegaDB.save();//Save on shutdown
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}));

		launch(args);
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
		AuctionListController auctionListController = new AuctionListController();
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
		profileController.addTestBids();

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
	}

	@Override
	public void browseAuctionsClick() {
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
