package cs.group11.controllers;

import cs.group11.MegaDB;
import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
import cs.group11.interfaces.OnCancelClick;
import cs.group11.interfaces.OnSubmitClick;
import cs.group11.models.Address;
import cs.group11.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SignUpController {

    @FXML private ImageView avatarImg;

    @FXML private Button uploadButton;
    @FXML private Button drawAvatarButton;
    @FXML private Button selectAvatarButton;

    @FXML private TextField usernameInput;
    @FXML private TextField firstnameInput;
    @FXML private TextField lastnameInput;
    @FXML private TextField phoneNoInput;
    @FXML
    private TextArea addressInput;
    @FXML private TextField postcodeInput;

    @FXML private Button submitButton;
    @FXML private Button cancelButton;

    private static final FileChooser.ExtensionFilter IMAGE_FILE_EXTENTIONS = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.gif", "*.jpeg", "*.jpg");

    private OnCancelClick onCancelClick;
    private OnSubmitClick onSubmitClick;

    private String avatarPath = "";
    private final int UK_PHONE_MAX_LENGTH = 11;
    @FXML
    private Label error;

    @FXML
    public void initialize() {
        phoneNoInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phoneNoInput.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (phoneNoInput.getText().length() > UK_PHONE_MAX_LENGTH) {
                phoneNoInput.setText(phoneNoInput.getText().substring(0, UK_PHONE_MAX_LENGTH));
            }
        });
    }

    public void setOnCancelClick(OnCancelClick onCancelClick) {
        this.onCancelClick = onCancelClick;
    }

    public void setOnSubmitClick(OnSubmitClick onSubmitClick) {
        this.onSubmitClick = onSubmitClick;
    }

    public void uploadClick() {
        avatarPath = userSelectImage();

        try {
            Image image = new Image(avatarPath);
            this.avatarImg.setImage(image);
        } catch (NullPointerException e) {
            System.out.println("Path not specified");
        }
    }

    public void drawAvatarClick() throws IOException {
        Stage drawingStage = new Stage();

        OnSubmitClick onSave = (Object selectedAvatar) -> {
            try {
                avatarPath = (String) selectedAvatar;
                drawingStage.close();
                Image image = new Image(avatarPath);
                this.avatarImg.setImage(image);

            } catch (NullPointerException e) {
                System.out.println("Path not specified");
                avatarPath = "";
            }
        };

        DrawController drawController = new DrawController();
        drawController.setOnSave(onSave);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/drawer.fxml"));
        loader.setController(drawController);
        Parent root = loader.load();
        drawingStage.setTitle("Drawing tool");
        drawingStage.setScene(new Scene(root));
        drawingStage.show();
    }

    public void cancelClick() {
        onCancelClick.cancel(null);
    }

    public void submitClick() {
        String username = usernameInput.getText().toLowerCase();
        String firstname = firstnameInput.getText();
        String lastname = lastnameInput.getText();
        String phoneNo = phoneNoInput.getText();
        String postcode = postcodeInput.getText();

        List<String> addressLines = new ArrayList<>();
        Collections.addAll(addressLines, addressInput.getText().split("\\n"));
        String[] lines = new String[addressLines.size()];
        lines = addressLines.toArray(lines);

        User user = null;

        try {
            Address address = new Address(lines, postcode);
            user = new User(username, firstname, lastname, phoneNo, address, avatarPath);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("User signup error");
            alert.setHeaderText("There was a problem with your signup details.");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
            return;
        }

        try {
            user.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MegaDB.login(user.getUsername());

        clearAll();
        onSubmitClick.submit(user);
    }

    private void clearAll() {
        usernameInput.clear();
        firstnameInput.clear();
        lastnameInput.clear();
        phoneNoInput.clear();
        postcodeInput.clear();
        addressInput.clear();
        avatarImg.setImage(null);
    }

    /**
     * Pop up a window to let the user choose one of the build in avatar as their own avatar
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    public void builtInAvatarClick() throws IOException {
        Stage stage = new Stage();

        OnSubmitClick onAvatarSubmit = avatarPath -> {
            this.avatarPath = (String) avatarPath;

            stage.close();
            Image image = new Image(this.avatarPath);
            this.avatarImg.setImage(image);
        };

        BuildInAvatarController buildInAvatarController = new BuildInAvatarController();
        buildInAvatarController.setOnAvatarSubmit(onAvatarSubmit);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/buildInAvatar.fxml"));
        fxmlLoader.setController(buildInAvatarController);

        Parent root1 = fxmlLoader.load();
        stage.setTitle("Build In Avatar");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    private String userSelectImage() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(IMAGE_FILE_EXTENTIONS);
        chooser.setTitle("Select Image");
        File in = chooser.showOpenDialog(avatarImg.getScene().getWindow());

        //return path if a file chosen
        if (Validator.isFileValid(in)) {
            return in.toURI().toString();
        }
        return null;
    }
}
