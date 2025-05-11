package com.splitmate.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.splitmate.model.ConversionPolicy;
import com.splitmate.model.Currency;
import com.splitmate.model.Group;
import com.splitmate.model.User;
import com.splitmate.service.GroupService;
import com.splitmate.service.SessionService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

@Controller
public class GroupSettingsController implements Initializable {

    @FXML private Label backLabel;

    @FXML private Button addMemberButton;
    @FXML private Button deleteMemberButton;

    @FXML
    private ComboBox<Currency> currencyComboBox;
    @FXML
    private ComboBox<ConversionPolicy> currencyConversionComboBox;

    @FXML private Button freezeMemberButton;
    @FXML private Button unfreezeMemberButton;

    @FXML private Label currentBudgetLabel;
    @FXML private TextField budgetTextField;

    @FXML private Button saveButton;

        // 4 overlay Panes
    @FXML private AnchorPane addMemberOverlay,
                             deleteMemberOverlay,
                             freezeMemberOverlay,
                             unfreezeMemberOverlay;

    // 4 containers inside them
    @FXML private VBox addMembersListContainer,
                      deleteMembersListContainer,
                      freezeMembersListContainer,
                      unfreezeMembersListContainer;

    private final MainController mainController;
    private final SessionService sessionService;
    @Autowired private GroupService groupService;


    public GroupSettingsController(MainController mainController, SessionService sessionService) {
        this.mainController = mainController;
        this.sessionService = sessionService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // these will be 
        currencyComboBox.setItems(FXCollections.observableArrayList(Currency.values()));
        // TODO get currency of the group and write here currencyComboBox.setValue(value)
        currencyConversionComboBox.setItems(FXCollections.observableArrayList(ConversionPolicy.values()));
        // TODO get conversion policy of the group and write it currencyComboBox.setValue(value)

        // wire up all actions
        backLabel.setOnMouseClicked(this::onBack);
        addMemberButton.setOnAction(e -> showOverlay(addMemberOverlay));
        deleteMemberButton.setOnAction(e -> showOverlay(deleteMemberOverlay));
        freezeMemberButton.setOnAction(e -> showOverlay(freezeMemberOverlay));
        unfreezeMemberButton.setOnAction(e -> showOverlay(unfreezeMemberOverlay));
        saveButton.setOnAction(e -> onSave());

        // populate each overlay list with sample data (replace with real Users)
        Group currentGroup = sessionService.getCurrentGroup();
        User currentUser = sessionService.getCurrentUser();
        List<User> invitableUsers = groupService.getPossiblMembersToAdd(currentGroup.getId(), currentUser.getId());
        invitableUsers.forEach(user ->
            createRow(
                user.getName(),
                addMembersListContainer,
                "Add Member",
                "-fx-background-color:limegreen;",
                evt -> {
                groupService.addUserToGroup(currentGroup.getId(), user.getId());
                }
            )
        );
        List<User> usersOfGroup = currentGroup.getMembers();
        usersOfGroup.forEach(user ->
            createRow(
                user.getName(),
                deleteMembersListContainer,
                "Delete Member",
                "-fx-background-color:red;",
                evt -> {
                groupService.removeUserFromGroup(currentGroup.getId(), user.getId());
                }
            )
        );
        usersOfGroup.forEach(user ->
            createRow(
                user.getName(),
                freezeMembersListContainer,
                "Freeze",
                "-fx-background-color:blue;",
                evt -> {
                groupService.frozeUserInAGroup(currentGroup.getId(), user.getId());
                }
            )
        );
        List<User> frozenUsers = currentGroup.getFrozenMembers();
                usersOfGroup.forEach(user ->
            createRow(
                user.getName(),
                unfreezeMembersListContainer,
                "Unfreeze",
                "-fx-background-color:blue;",
                evt -> {
                groupService.unfreezeUserInGroup(currentGroup.getId(), user.getId());
                }
            )
        );
    }

    private void hideAllOverlays() {
        addMemberOverlay.setVisible(false);
        deleteMemberOverlay.setVisible(false);
        freezeMemberOverlay.setVisible(false);
        unfreezeMemberOverlay.setVisible(false);
    }

        // bring this overlay to front & show it
    private void showOverlay(AnchorPane overlay) {
        hideAllOverlays();
        overlay.toFront();
        overlay.setVisible(true);
    }

    @FXML private void onCloseOverlay() {
        hideAllOverlays();
    }

    private void onBack(MouseEvent e) {
        mainController.showGroupDetailsView();
    }

    private void onSave() {
        
    }

    private void createRow(String name, VBox container, String btnText, String btnStyle,  EventHandler<ActionEvent> handler) {
        HBox row = new HBox(10);
        row.setStyle("-fx-background-color:white; -fx-padding:8px; -fx-background-radius:8px;");

        // avatar placeholder (swap in real images as needed):
        // ImageView avatar = new ImageView(
        //     new Image(getClass().getResourceAsStream("/images/avatar-placeholder.png"))
        // );
        // avatar.setFitWidth(32);
        // avatar.setFitHeight(32);

        Label nameLabel = new Label(name);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button actionBtn = new Button(btnText);
        actionBtn.setStyle(btnStyle + "; -fx-text-fill:white;");
        actionBtn.setOnAction(handler); 

        row.getChildren().setAll(nameLabel, spacer, actionBtn);
        container.getChildren().add(row);
    }
}
