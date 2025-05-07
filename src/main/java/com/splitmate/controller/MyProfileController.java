package com.splitmate.controller;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import com.splitmate.model.Group;
import com.splitmate.model.User;
import com.splitmate.service.GroupService;
import com.splitmate.service.SessionService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

@Component
public class MyProfileController implements Initializable {

    @FXML private Button changePasswordButton;
    @FXML private ToggleGroup frequencyToggleGroup;
    @FXML private Button saveButton;
    @FXML private Text userIdText;
    @FXML private ImageView userImage;
    @FXML private Text userNameText;
    @FXML private Text ibanText;

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
            userNameText.setText("Guest");
            ibanText.setText("—");
            loadDefaultImage();
            return;
        }

        // Populate fields
        userIdText.setText(current.getId());
        userNameText.setText(current.getName());
        ibanText.setText(current.getProfileImageUrl() != null
            ? current.getProfileImageUrl()
            : "—"
        );

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
        // TODO: save profile updates
    }
}
