package com.splitmate.controller;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.splitmate.model.Expense;
import com.splitmate.model.Group;
import com.splitmate.model.User;
import com.splitmate.service.GroupService;
import com.splitmate.service.SessionService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

@Controller
public class GroupOverviewController implements Initializable {

    @FXML private Label backToGroupsLabel;

    @FXML private ImageView groupIcon;
    @FXML private Label groupName;
    @FXML private Button settingsButton;
    @Autowired private SessionService sessionService;
    @Autowired private GroupService   groupService;

    @FXML private TabPane   tabPane;

    @FXML private ScrollPane membersScroll;
    @FXML private HBox       membersContainer;

    @FXML private ScrollPane regularExpensesScroll;
    @FXML private VBox       regularExpensesContainer;

    @FXML private ScrollPane expensesScroll;
    @FXML private VBox       expenseItemsContainer;

    private final MainController mainController;

    public GroupOverviewController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void onBack(MouseEvent e) {
        sessionService.clearGroup();
        mainController.showGroupsView();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backToGroupsLabel.setOnMouseClicked(this::onBack);
        // 1) Load the Group
        Group group = sessionService.getCurrentGroup();
        if (group == null) {
            String id = sessionService.getCurrentGroupId();
            group = groupService.getGroup(id);
            sessionService.setCurrentGroup(group);
        }

        // 2) Title + Avatar
        groupName.setText(group.getName());
        String b64 = group.getAvatarBase64();
        if (b64 != null && !b64.isEmpty()) {
            byte[] img = Base64.getDecoder().decode(b64);
            groupIcon.setImage(new Image(new ByteArrayInputStream(img)));
        }

        // 3) Members list
        membersContainer.getChildren().clear();
        for (User u : group.getMembers()) {
            VBox card = new VBox(4);
            ImageView iv = new ImageView();
            iv.setFitWidth(40);
            iv.setFitHeight(40);
            if (u.getAvatarBase64() != null) {
                byte[] ui = Base64.getDecoder().decode(u.getAvatarBase64());
                iv.setImage(new Image(new ByteArrayInputStream(ui)));
            }
            Label name = new Label(u.getName());
            card.getChildren().addAll(iv, name);
            membersContainer.getChildren().add(card);
        }

        // 4) Regular expenses
        regularExpensesContainer.getChildren().clear();
        for (Expense e : group.getExpenses()) {
            HBox row = new HBox(6);
            Label desc = new Label(e.getDescription() + ":");
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            Label amt = new Label(String.format("%.2f %s",
                            e.getAmount(), group.getDefaultCurrency()));
            row.getChildren().addAll(desc, spacer, amt);
            regularExpensesContainer.getChildren().add(row);
        }

        // 5) One-off expenses (uses your helper)
        expenseItemsContainer.getChildren().clear();
        for (Expense e : group.getExpenses()) {
            addExpenseItem(e);
        }
    }

    @FXML
    private void onSettingsAction(ActionEvent event) {
        mainController.showGroupSettingsView();
    }
    @FXML
    private void onAddExpenseAction(ActionEvent event) {
        mainController.showNewGroupExpenseView();
    }

    private void addExpenseItem(Expense expense) {
        HBox card = new HBox(10);
        card.setStyle(
          "-fx-background-color: #f0f0f0; " +
          "-fx-background-radius: 8px; " +
          "-fx-padding: 10px;"
        );
        Label descLabel = new Label(expense.getDescription());
        descLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label dateLabel = new Label("Date: " + expense.getDate());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(descLabel, spacer, dateLabel);

        // wire the click
        card.setOnMouseClicked(evt -> mainController.showExpenseDetailView(expense));

        expenseItemsContainer.getChildren().add(card);
    }
    
}
