package com.splitmate.controller;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.splitmate.model.User;
import com.splitmate.service.FriendshipService;
import com.splitmate.service.SessionService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;

@Component
public class FrontFriendListController implements Initializable {

    @FXML private VBox friendCardsContainer;
    @FXML private VBox addFriendPopup;
    @FXML private TextField friendIdField;

    @Autowired private SessionService sessionService;
    @Autowired private NotificationController notificationService;
    @Autowired private FriendshipService friendshipService;
    @Autowired private MainController mainController;

    @Override
    public void initialize(URL loc, ResourceBundle res) {
        loadFriends();
    }

    private void loadFriends() {
        User current = sessionService.getCurrentUser();
        List<User> friends = friendshipService.getFriends(current.getId());
        friendCardsContainer.getChildren().clear();
        for (User friend : friends) {
            renderFriendCard(friend);
        }
    }

    @FXML
    private void onAddFriend(MouseEvent evt) {
        friendIdField.clear();
        addFriendPopup.setVisible(true);
    }

    @FXML
    private void onCloseAddFriend(MouseEvent evt) {
        addFriendPopup.setVisible(false);
    }

    @FXML
    private void onSendFriendRequest(ActionEvent evt) {
        System.out.println("Sending friend request");
        String idText = friendIdField.getText().trim();
        User current = sessionService.getCurrentUser();
        if (!idText.isEmpty()) {
            System.out.println("Sending friend request to " + idText);
            friendshipService.sendFriendRequest(current.getId(), idText);
        }
        addFriendPopup.setVisible(false);
    }

    private void renderFriendCard(User friend) {
        HBox card = new HBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color:#e0e0e0; -fx-background-radius:8;");
        card.setOnMouseClicked(event -> {
        mainController.showFriendOverview(friend);  // ✅ this line triggers navigation
    });


        ImageView avatar = new ImageView();
        avatar.setFitWidth(40);
        avatar.setFitHeight(40);
        Circle clip = new Circle(20, 20, 20);
        avatar.setClip(clip);
        String b64 = friend.getAvatarBase64();
        if (b64 != null && !b64.isEmpty()) {
            byte[] data = Base64.getDecoder().decode(b64);
            avatar.setImage(new Image(new ByteArrayInputStream(data)));
        } else {
            avatar.setImage(new Image(
                getClass().getResourceAsStream("/icons/default-avatar.png")
            ));
        }

        Label name = new Label(friend.getName() );
        name.setStyle("-fx-font-size:16px; -fx-text-fill:#333333;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        card.getChildren().addAll(avatar, name, spacer);
        friendCardsContainer.getChildren().add(card);
    }
}