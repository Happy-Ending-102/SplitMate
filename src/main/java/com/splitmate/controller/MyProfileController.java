package com.splitmate.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.splitmate.model.Frequency;
import com.splitmate.model.User;
import com.splitmate.service.SessionService;
import com.splitmate.service.UserService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

@Component
public class MyProfileController implements Initializable {

    @FXML private Button changePasswordButton;
    @FXML private ToggleGroup frequencyToggleGroup;
    @FXML private Button saveButton;
    @FXML private Text userIdText;
    @FXML private ImageView userImage;
    @FXML private TextField userNameField;
    @FXML private TextField ibanField;

    @Autowired private UserService userService;
    private final SessionService sessionService;
    private final MainController mainController;

    public MyProfileController(SessionService sessionService, MainController mainController) {
        this.sessionService = sessionService;
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User current = sessionService.getCurrentUser();

        if (current == null) {
            userIdText.setText("N/A");
            userNameField.setText("Guest");
            ibanField.setText("—");
            loadDefaultImage();
            return;
        }

        // Populate fields
        userIdText.setText("ID: " + current.getId());
        userNameField.setText(current.getName());
        if(current.getIban()!=null){
            ibanField.setText(current.getIban());
        }

        if (current.getFrequency() != null) {
        String freqKey = current.getFrequency().name();
        for (Toggle t : frequencyToggleGroup.getToggles()) {
            if (freqKey.equals(t.getUserData())) {
                frequencyToggleGroup.selectToggle(t);
                break;
            }
        }
    }

        // Load profile image: first try avatarBase64, else default icon
        String base64 = current.getAvatarBase64();
        if (base64 != null && !base64.isBlank()) {
            try {
                byte[] bytes = Base64.getDecoder().decode(base64);
                Image img = new Image(new ByteArrayInputStream(bytes));
                userImage.setImage(img);
                return;
            } catch (IllegalArgumentException ex) {
                // fallback to default image
            }
        }

        // If no valid avatarBase64 or decoding failed, load the default image
        loadDefaultImage();
    }

    /**
     * Attempts to load the default avatar from classpath. If not found,
     * logs an error and sets a placeholder.
     */
    private void loadDefaultImage() {
        URL res = Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("icons/default-avatar.png");
        if (res != null) {
            userImage.setImage(new Image(res.toExternalForm()));
        } else {
            System.err.println("default-avatar.png not found on classpath");
            userImage.setImage(new Image("https://via.placeholder.com/80"));
        }
    }

    @FXML
    private void onChangePassword(ActionEvent event) {
        mainController.showChangePasswordView();
    }

    @FXML
    private void onSave(ActionEvent event) {
        User currUser = sessionService.getCurrentUser();
        currUser.setName(userNameField.getText());
        currUser.setIban(ibanField.getText());
            Toggle selected = frequencyToggleGroup.getSelectedToggle();
        if (selected != null) {
            // assumes Frequency is an enum with DAILY, WEEKLY, MONTHLY
            String freqKey = selected.getUserData().toString();
            currUser.setFrequency(Frequency.valueOf(freqKey));
        }
        mainController.showProfileView();
        userService.updateUser(currUser);

    }
    @FXML
    private void onCopyID(){

    }
    @FXML
    private void onCopyIBAN(){

    }
    @FXML
    private void onChangePhoto(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(userImage.getScene().getWindow());

        if (selectedFile != null) {
            try {
                byte[] imageBytes = Files.readAllBytes(selectedFile.toPath());
                String base64 = Base64.getEncoder().encodeToString(imageBytes);

                // Update user model and DB
                User currentUser = sessionService.getCurrentUser();
                currentUser.setAvatarBase64(base64);
                userService.updateUser(currentUser);  // Persist to DB

                // Refresh ImageView
                userImage.setImage(new Image(new ByteArrayInputStream(imageBytes)));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
