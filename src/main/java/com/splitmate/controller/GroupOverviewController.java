package com.splitmate.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
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

    @FXML private TabPane tabPane;
    @FXML private ScrollPane membersScroll;
    @FXML private HBox membersContainer;

    @FXML private ScrollPane regularExpensesScroll;
    @FXML private VBox regularExpensesContainer;

    @FXML private ScrollPane expensesScroll;
    @FXML private VBox expenseItemsContainer;
    @FXML private Button addExpenseButton;

    private final MainController mainController;

    public GroupOverviewController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO groupicon, groupname should be added
        //TODO membersContainer, regularExpencesContainer and expenseItemsContainer(for group expences page) should be filled here
        backToGroupsLabel.setOnMouseClicked(this::onBack);

    }

    private void onBack(MouseEvent e) {
        mainController.showGroupsView();
    }

    @FXML
    private void onSettingsAction(ActionEvent event) {
        mainController.showGroupSettingsView();
    }
    @FXML
    private void onAddExpenseAction(ActionEvent event) {
        // TODO: show your “new expense” dialog
        System.out.println("Opening add-expense dialog…");
    }

    private void addExpenseItem(String desc, String date) {
        HBox card = new HBox(10);
        card.setStyle(
          "-fx-background-color: #f0f0f0; " +
          "-fx-background-radius: 8px; " +
          "-fx-padding: 10px;"
        );
        Label descLabel = new Label(desc);
        descLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label dateLabel = new Label("Date: " + date);
    
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
    
        card.getChildren().addAll(descLabel, spacer, dateLabel);
        expenseItemsContainer.getChildren().add(card);
    }
    

}
