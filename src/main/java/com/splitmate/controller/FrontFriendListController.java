package com.splitmate.controller;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.splitmate.model.Friendship;
import com.splitmate.model.User;
import com.splitmate.repository.FriendshipRepository;
import com.splitmate.service.SessionService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

@Component
public class FrontFriendListController implements Initializable {

    @FXML private VBox friendCardsContainer;

    @Autowired private SessionService sessionService;
    @Autowired private FriendshipRepository friendshipRepo;
    @Autowired private MainController mainController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Which user is logged in?
        User current = sessionService.getCurrentUser();

        // 2. Load all Friendship objects where current is either userA or userB
        List<Friendship> list = friendshipRepo.findByUserAOrUserB(current, current);

        // 3. For each friendship, figure out the "other" user
        for (Friendship f : list) {
            User friend = f.getUserA().equals(current)
                          ? f.getUserB()
                          : f.getUserA();
            addFriendCard(friend);
        }
    }

    // Called when the "+ Add new friend" link is clicked
    @FXML
    private void onAddFriend(ActionEvent evt) {
        mainController.showAddFriendView();
    }

    // Creates one HBox “card” for a given friend and adds it to the VBox
    private void addFriendCard(User friend) {
        HBox card = new HBox(8);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color:#eee; -fx-padding:8; -fx-background-radius:6;");

        ImageView avatar = new ImageView();
        avatar.setFitWidth(40); 
        avatar.setFitHeight(40);

        String b64 = friend.getAvatarBase64();
        if (b64 != null && !b64.isEmpty()) {
            // Decode the Base64 into a byte[]
            byte[] data = Base64.getDecoder().decode(b64);
            // Wrap it in a ByteArrayInputStream so JavaFX can read it
            avatar.setImage(new Image(new ByteArrayInputStream(data)));
        } else {
            avatar.setImage(new Image(
            getClass().getResourceAsStream("/icons/default-avatar.png")
            ));
        }


        // Their name
        Label name = new Label(friend.getName());
        name.setStyle("-fx-font-size:16px;");

        card.getChildren().addAll(avatar, name);

        // Finally, insert this card into the scroll list
        friendCardsContainer.getChildren().add(card);
    }
}
