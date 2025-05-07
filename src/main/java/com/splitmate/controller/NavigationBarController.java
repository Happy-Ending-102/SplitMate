package com.splitmate.controller;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import com.splitmate.model.User;
import com.splitmate.service.SessionService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

@Component
public class NavigationBarController implements Initializable {

    private final MainController main;
    private final SessionService sessionService;

    @FXML private ImageView profileImageView;
    @FXML private ImageView groupsIcon;
    @FXML private ImageView friendsIcon;
    @FXML private ImageView notificationsIcon;
    @FXML private ImageView paymentsIcon;
    @FXML private ImageView historyIcon;

    public NavigationBarController(MainController main,
                                   SessionService sessionService) {
        this.main = main;
        this.sessionService = sessionService;
    }

    @Override
    public void initialize(URL loc, ResourceBundle res) {
        // Load user avatar
        User u = sessionService.getCurrentUser();
        if (u != null && u.getAvatarBase64() != null) {
            try {
                byte[] data = Base64.getDecoder().decode(u.getAvatarBase64());
                profileImageView.setImage(new Image(new ByteArrayInputStream(data)));
            } catch (IllegalArgumentException ex) {
                setDefaultAvatar();
            }
        } else {
            setDefaultAvatar();
        }
    }

    private void setDefaultAvatar() {
        var res = Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("icons/default-avatar.png");
        if (res != null) {
            profileImageView.setImage(new Image(res.toExternalForm()));
        }
    }

    @FXML private void gotoGroups()        { main.showGroupsView(); }
    @FXML private void gotoFriends()       { /* TODO */ }
    @FXML private void gotoNotifications() { /* TODO */ }
    @FXML private void gotoPayments()      { /* TODO */ }
    @FXML private void gotoHistory()       { /* TODO */ }
    @FXML private void gotoProfile(MouseEvent e) { main.showProfileView(); }

    @FXML
    private void logout() {
        sessionService.clear();
        main.showLoginView();
    }
}
