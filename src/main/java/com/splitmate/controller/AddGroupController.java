package com.splitmate.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.splitmate.model.Group; // citeturn4file1
import com.splitmate.model.Currency;
import com.splitmate.model.ConversionPolicy;
import com.splitmate.model.User; // citeturn4file0
import com.splitmate.service.GroupService; // citeturn4file3
import com.splitmate.service.UserService;
import com.splitmate.service.SessionService;

@Component
public class AddGroupController implements Initializable {

    @FXML private Button addImageButton;
    @FXML private ComboBox<Currency> currencyComboBox;
    @FXML private ComboBox<ConversionPolicy> currencyConversionComboBox;
    @FXML private TextField friendName;
    @FXML private ScrollPane friendsScrollPane;
    @FXML private ImageView groupImage;
    @FXML private TextField groupName;
    @FXML private Button groupSaveButton;

    @Autowired private GroupService groupService;
    @Autowired private UserService userService;
    @Autowired private SessionService sessionService;

    private String avatarBase64 = "";
    private final List<User> invitedUsers = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate selection boxes
        currencyComboBox.setItems(FXCollections.observableArrayList(Currency.values()));
        currencyConversionComboBox.setItems(FXCollections.observableArrayList(ConversionPolicy.values()));
        // Placeholder image
        groupImage.setImage(new Image(getClass().getResourceAsStream("/icons/default-avatar.png")));
        /*URL url = getClass().getResource("/icons/default-avatar.png");
        if (url == null) {
            throw new IllegalStateException("Resource not found: /images/default-avatar.png");
        }
        groupImage.setImage(new Image(url.toExternalForm()));*/
        // Set up friends list (this should be a scrollable list of users)}
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
                // TODO: display an error dialog
            }
        }
    }

    @FXML
    void saveGroup(ActionEvent event) {
        String name = groupName.getText().trim();
        if (name.isEmpty()) {
            // TODO: alert "Group name cannot be empty"
            return;
        }

        Group g = new Group();
        g.setName(name);
        g.setAvatarBase64(avatarBase64); //TODO CHECK THIS
        groupService.createGroup(g);

        if (currencyComboBox.getValue() != null) {
            g.setDefaultCurrency(currencyComboBox.getValue());
        }
        if (currencyConversionComboBox.getValue() != null) {
            g.setConversionPolicy(currencyConversionComboBox.getValue());
        }

        // Always include the current user
        User current = sessionService.getCurrentUser();
        g.addMember(current);

        current.joinGroup(g);
        userService.updateUser(current);

        // TODO: invite selected users. the ui must change before.

        groupService.updateGroup(g);

    }
}
