package com.splitmate.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
        User currentUser = sessionService.getCurrentUser();
        Group currentGroup = sessionService.getCurrentGroup();
        currencyComboBox.setValue(currentGroup.getDefaultCurrency());
        // TODO get currency of the group and write here currencyComboBox.setValue(value)
        currencyConversionComboBox.setItems(FXCollections.observableArrayList(ConversionPolicy.values()));
        currencyConversionComboBox.setValue(currentGroup.getConversionPolicy());
        // TODO get conversion policy of the group and write it currencyComboBox.setValue(value)
        currentBudgetLabel.setText("Current Budget: " + currentGroup.getBudget() + " " + currentGroup.getDefaultCurrency());

        // wire up all actions
        backLabel.setOnMouseClicked(this::onBack);
        addMemberButton.setOnAction(e -> {populateAddMembersList(); 
        showOverlay(addMemberOverlay);} 
        );
        deleteMemberButton.setOnAction(e -> {populateDeleteMembersList(); 
        showOverlay(deleteMemberOverlay);} 
        );
        freezeMemberButton.setOnAction(e -> {populateFreezeMembersList(); 
        showOverlay(freezeMemberOverlay);}
        );
        unfreezeMemberButton.setOnAction(e -> {populateUnfreezeMembersList(); 
        showOverlay(unfreezeMemberOverlay);}
        );
        saveButton.setOnAction(e -> onSave());
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
        // 1) Grab current group from session
        Group currentGroup = sessionService.getCurrentGroup();

        // 2) Update currency & conversion policy
        Currency selectedCurrency = currencyComboBox.getValue();
        if (selectedCurrency != null) {
            currentGroup.setDefaultCurrency(selectedCurrency);
        }
        ConversionPolicy selectedPolicy = currencyConversionComboBox.getValue();
        if (selectedPolicy != null) {
            currentGroup.setConversionPolicy(selectedPolicy);
        }

        // 3) Read budget field; only change if non-empty
        String budgetInput = budgetTextField.getText().trim();
        if (!budgetInput.isEmpty()) {
            try {
                double newBudget = Double.parseDouble(budgetInput);
                currentGroup.setBudget(newBudget);
            } catch (NumberFormatException ex) {
                // You could show an error label here instead of throwing
                System.err.println("Invalid budget format: " + budgetInput);
                return;
            }
        }

        // 4) Persist changes (you'll need this method in GroupService)
        groupService.updateGroup(currentGroup);

        // 5) Refresh UI label
        currentBudgetLabel.setText(
            "Current Budget: " 
            + currentGroup.getBudget() 
            + " " 
            + currentGroup.getDefaultCurrency()
        );

        // 6) Navigate back to details
        mainController.showGroupDetailsView();
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

    private void populateAddMembersList() {
        addMembersListContainer.getChildren().clear();

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
                    // 1. Add user to the group
                    groupService.addUserToGroup(currentGroup.getId(), user.getId());

                    // 2. Refresh the in-memory group from DB
                    Group updatedGroup = groupService.getGroup(currentGroup.getId());
                    sessionService.setCurrentGroup(updatedGroup);

                    // 3. Refresh both lists
                    populateAddMembersList();
                    populateDeleteMembersList();
                }
            )
        );
    }

    private void populateDeleteMembersList() {
        deleteMembersListContainer.getChildren().clear();
        Group currentGroup = sessionService.getCurrentGroup();
        User currentUser = sessionService.getCurrentUser();
        List<User> usersOfGroup = currentGroup.getMembers();
        List<User> otherMembers = usersOfGroup.stream().filter(u -> !u.getId().equals(currentUser.getId())).collect(Collectors.toList());
        otherMembers.forEach(user ->
            createRow(
                user.getName(),
                deleteMembersListContainer,
                "Delete Member",
                "-fx-background-color:red;",
                evt -> {
                    Group g = sessionService.getCurrentGroup();
                    groupService.removeUserFromGroup(g.getId(), user.getId());

                    g.getMembers().removeIf(u -> u.getId().equals(user.getId()));

                    populateDeleteMembersList();
                    populateAddMembersList();
                }
            )
        );
    }

    private void populateFreezeMembersList() {
        freezeMembersListContainer.getChildren().clear();
        Group currentGroup = sessionService.getCurrentGroup();
        User currentUser = sessionService.getCurrentUser();

        List<User> usersOfGroup = currentGroup.getMembers();
        List<User> otherMembers = usersOfGroup.stream().filter(u -> !u.getId().equals(currentUser.getId())).collect(Collectors.toList());

        otherMembers.forEach(user ->
            createRow(
                user.getName(),
                freezeMembersListContainer,
                "Freeze",
                "-fx-background-color:blue;",
                evt -> {
                    Group g = sessionService.getCurrentGroup();
                    User u  = user;  // captured from your forEach

                    // 1) Persist the change
                    groupService.frozeUserInAGroup(g.getId(), u.getId());

                    // 2) Update in-memory lists
                    g.getMembers().removeIf(mem -> mem.getId().equals(u.getId()));
                    g.getFrozenMembers().add(u);

                    // 3) Rebuild both pop-ups from the fresh model
                    populateFreezeMembersList();
                    populateUnfreezeMembersList();
                }
            )
        );    
    }

    private void populateUnfreezeMembersList() {
        unfreezeMembersListContainer.getChildren().clear();
        Group currentGroup = sessionService.getCurrentGroup();
        User currentUser = sessionService.getCurrentUser();
        List<User> frozenUsers = currentGroup.getFrozenMembers();
                frozenUsers.forEach(user ->
            createRow(
                user.getName(),
                unfreezeMembersListContainer,
                "Unfreeze",
                "-fx-background-color:blue;",
                evt -> {
                    Group g = sessionService.getCurrentGroup();
                    User u  = user;

                    groupService.unfreezeUserInGroup(g.getId(), u.getId());

                    g.getFrozenMembers().removeIf(f -> f.getId().equals(u.getId()));
                    g.getMembers().add(u);

                    populateFreezeMembersList();
                    populateUnfreezeMembersList();
                }
            )
        );    
    }
}
