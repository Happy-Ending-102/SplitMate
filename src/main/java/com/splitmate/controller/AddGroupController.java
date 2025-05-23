package com.splitmate.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.splitmate.model.*;
import com.splitmate.model.Currency;
import com.splitmate.service.*;

@Component
public class AddGroupController implements Initializable {

    @FXML private Button addImageButton;
    @FXML private ComboBox<Currency> currencyComboBox;
    @FXML private ComboBox<ConversionPolicy> currencyConversionComboBox;
    @FXML private TextField friendName;
    @FXML private ScrollPane friendsScrollPane;
    @FXML private VBox friendsListVBox;
    @FXML private ImageView groupImage;
    @FXML private TextField groupName;
    @FXML private Button groupSaveButton;
    @FXML private HBox addedFriendsHBox;
    @FXML private Label saveErrorLabel; 

    @Autowired private GroupService groupService;
    @Autowired private UserService userService;
    @Autowired private SessionService sessionService;
    @Autowired private MainController mainController;  // for navigation

    

    private String avatarBase64 = "";
    private final List<User> invitedUsers = new ArrayList<>();
    private final Map<User, Button> friendButtons = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currencyComboBox.setItems(FXCollections.observableArrayList(Currency.values()));
        currencyConversionComboBox.setItems(FXCollections.observableArrayList(ConversionPolicy.values()));
        groupImage.setImage(new Image(getClass().getResourceAsStream("/icons/default-avatar.png")));

        // Hide error label initially
        saveErrorLabel.setVisible(false);

        loadRealFriends();
    }

    private void loadRealFriends() {
        String myId = sessionService.getCurrentUser().getId();
        List<User> friends = userService.getFriendsOfUser(myId);

        friendsListVBox.getChildren().clear();
        friendButtons.clear();

        for (User friend : friends) {
            Button friendButton = new Button(friend.getName());
            friendButton.setMaxWidth(Double.MAX_VALUE);
            friendButton.getStyleClass().add("friend-button");

            friendButton.setOnAction(e -> {
                addFriendToGroup(friend);
                friendsListVBox.getChildren().remove(friendButton); // remove from scroll list
            });

            friendsListVBox.getChildren().add(friendButton);
            friendButtons.put(friend, friendButton);
        }
    }

    private void addFriendToGroup(User friend) {
        if (invitedUsers.contains(friend)) return;

        invitedUsers.add(friend);

        Label nameLabel = new Label(friend.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Button removeButton = new Button("×");
        removeButton.setStyle("-fx-background-color: transparent; -fx-font-weight: bold;");

        HBox container = new HBox(5, nameLabel, removeButton);
        // container.getStyleClass().add("friend-chip");

        removeButton.setOnAction(e -> {
            invitedUsers.remove(friend);
            addedFriendsHBox.getChildren().remove(container);

            // Optional: re-add to scroll pane
            Button originalButton = friendButtons.get(friend);
            if (originalButton != null) {
                friendsListVBox.getChildren().add(originalButton);
            }
        });

        Region space = new Region();
        space.setMinWidth(10);

        addedFriendsHBox.getChildren().addAll(space, container);
    }

    @FXML
    void addGroupImage(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Group Image");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = chooser.showOpenDialog(addImageButton.getScene().getWindow());
        if (file != null) {
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] bytes = fis.readAllBytes();
                avatarBase64 = Base64.getEncoder().encodeToString(bytes);
                groupImage.setImage(new Image(new ByteArrayInputStream(bytes)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void saveGroup(ActionEvent event) {
        // Hide previous error
        saveErrorLabel.setVisible(false);

        // Trim and validate group name
        String name = groupName.getText().trim();
        if (name.isEmpty()) {
            showError("Group name is required.");
            return;
        }

        // Validate that at least one member has been added
        if (invitedUsers.isEmpty()) {
            showError("Please add at least one member.");
            return;
        }

        // Validate default currency selection
        if (currencyComboBox.getValue() == null) {
            showError("Please select a default currency.");
            return;
        }

        // Validate conversion policy selection
        if (currencyConversionComboBox.getValue() == null) {
            showError("Please select a conversion policy.");
            return;
        }

        String nameG = groupName.getText().trim();
        if (name.isEmpty()) return;

        Group g = new Group();
        g.setName(nameG);
        g.setAvatarBase64(avatarBase64);

        if (currencyComboBox.getValue() != null) {
            g.setDefaultCurrency(currencyComboBox.getValue());
        }

        if (currencyConversionComboBox.getValue() != null) {
            g.setConversionPolicy(currencyConversionComboBox.getValue());
        }
        groupService.createGroup(g);

        User current = sessionService.getCurrentUser();
        g.addMember(current);
        userService.updateUser(current);
        for(User user : invitedUsers) {
            g.addMember(user);
            userService.updateUser(user);
        }

        groupService.updateGroup(g);

        // Navigate to group list page after saving
        mainController.showGroupsView();
    }
    private void showError(String message) {
        saveErrorLabel.setText(message);
        saveErrorLabel.setVisible(true);
    }
}
