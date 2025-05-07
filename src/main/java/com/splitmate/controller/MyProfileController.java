package com.splitmate.controller;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import com.splitmate.model.User;
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

    public MyProfileController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User current = sessionService.getCurrentUser();
        if (current == null) {
            // No user in session; you might redirect to login
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
            : "—"  // or another user.getIban() if you have it
        );

        // Load profile image: first try avatarBase64, else default icon
        String base64 = current.getAvatarBase64();
        if (base64 != null && !base64.isBlank()) {
            try {
                byte[] bytes = Base64.getDecoder().decode(base64);
                Image img = new Image(new ByteArrayInputStream(bytes));
                userImage.setImage(img);
            } catch (IllegalArgumentException ex) {
                loadDefaultImage();
            }
        } else {
            loadDefaultImage();
        }
    }

    private void loadDefaultImage() {
        Image img = new Image(
            getClass().getResource("/icons/default-avatar.png")
                   .toExternalForm()
        );
        userImage.setImage(img);
    }

    @FXML
    private void onChangePassword(ActionEvent event) {
        // TODO: open change-password dialog
    }

    @FXML
    private void onSave(ActionEvent event) {
        // TODO: save profile updates
    }
}
