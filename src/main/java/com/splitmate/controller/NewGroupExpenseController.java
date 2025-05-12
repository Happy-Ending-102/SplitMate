package com.splitmate.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.splitmate.model.Currency;
import com.splitmate.model.Group;
import com.splitmate.model.Partition;
import com.splitmate.model.User;
import com.splitmate.service.GroupService;
import com.splitmate.service.SessionService;

import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

@Component
public class NewGroupExpenseController implements Initializable {
    @FXML
    private Button addExpenseButton;

    @FXML
    private ComboBox<Currency> currencyComboBox;

    @FXML
    private TextField expenseAmountTextField;

    @FXML
    private TextField expenseDescriptionTextField;

    @FXML
    private ToggleGroup expenseFrequencyToggleGroup;

    @FXML
    private TextField expenseOwnerTextField;

    @FXML
    private ToggleGroup expenseTypePreferenceToggleGroup;

    @FXML
    private VBox friendsVBox;

    @FXML
    private RadioButton monthlyFrequencyRadiButton;

    @FXML
    private Button newExpenseCloseButton;

    @FXML
    private RadioButton oneTimeExpenseRadioButton;

    @FXML
    private RadioButton regularExpenseRadioButton;

    @FXML
    private Button setFixedAmountButton;

    @FXML
    private Button setPercantageButton;

    @FXML
    private RadioButton weeklyFrequencyRadiButton;

    @FXML
    private Button closeSetFixedAmountPopUpButton;

    @FXML
    private Button closeSetPercentagePopUpButton;

    @FXML
    private Button fixedAmountShareDoneButton;

    @FXML
    private Button percentageShareDoneButton;

    @FXML
    private AnchorPane setFixedAmountAnchorPane;

    @FXML
    private AnchorPane setPercantageAnchorPane;


    @FXML
    private VBox userFixedAmountShareVBox;

    @FXML
    private VBox userPercentageShareVBox;
    
    @FXML
    private Button addFriendsDoneButton;

    private List<User> selectedUsers;

    private List<Partition> partitionList;


    @Autowired private SessionService sessionService;
    @Autowired private GroupService   groupService;

    private final MainController mainController;

    public NewGroupExpenseController(MainController mainController) {
        this.mainController = mainController;
    }   

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedUsers = new ArrayList<>();
        partitionList = new ArrayList<>();
        setFixedAmountButton.setDisable(true);
        setPercantageButton.setDisable(true);
        addExpenseButton.setDisable(true);
        currencyComboBox.setItems(FXCollections.observableArrayList(Currency.values()));
         // Load current group and list members
        String groupId = sessionService.getCurrentGroupId();
        if (groupId != null) {
            Group currentGroup = groupService.getGroup(groupId);
            if (currentGroup != null) {
                for (User user : currentGroup.getMembers()) {
                    CheckBox checkBox = new CheckBox(user.getName());
                    checkBox.setUserData(user);  // So you can retrieve the User object later
                    friendsVBox.getChildren().add(checkBox);
                }
            }
        }
    }


    @FXML
    void addExpense(ActionEvent event) {
        // TO DO
        mainController.showGroupDetailsView();
    }

    @FXML
    void closeNewExpenseScreen(ActionEvent event) {
        mainController.showGroupDetailsView();
    }

    @FXML
    void setFixedAmount(ActionEvent event) {
        setPercantageButton.setDisable(true);
        userFixedAmountShareVBox.getChildren().clear();

        for (User user : selectedUsers) {
            HBox row = new HBox(10);
            Label label = new Label(user.getName());
            TextField field = new TextField();
            field.setPromptText("Amount");
            row.getChildren().addAll(label, field);
            userFixedAmountShareVBox.getChildren().add(row);
        }
        setFixedAmountAnchorPane.setVisible(true);
    }

    @FXML
    void setPercantage(ActionEvent event) {
        setFixedAmountButton.setDisable(true);
        userPercentageShareVBox.getChildren().clear();

        for (User user : selectedUsers) {
            HBox row = new HBox(10);
            Label label = new Label(user.getName());
            TextField field = new TextField();
            field.setPromptText("%");
            row.getChildren().addAll(label, field);
            userPercentageShareVBox.getChildren().add(row);
        }
        setPercantageAnchorPane.setVisible(true);
    }

    
    @FXML
    void getUsersToDivide(ActionEvent event) {
        selectedUsers.clear();
        userFixedAmountShareVBox.getChildren().clear();   // clear old entries
        userPercentageShareVBox.getChildren().clear();    // clear old entries

        for (Node node : friendsVBox.getChildren()) {
            if (node instanceof CheckBox cb && cb.isSelected()) {
                User user = (User) cb.getUserData();
                selectedUsers.add(user);
            }
        }

        if(selectedUsers.size() != 0){
            setFixedAmountButton.setDisable(false);
            setPercantageButton.setDisable(false);
        }
    }

    @FXML
    void closeSetFixedAmount(ActionEvent event) {
        setFixedAmountAnchorPane.setVisible(false);
    }

    @FXML
    void closeSetPercantage(ActionEvent event) {
        setPercantageAnchorPane.setVisible(false);
    }

    @FXML
    void fixedAmountShareDone(ActionEvent event) {
        partitionList.clear();

        for (Node node : userFixedAmountShareVBox.getChildren()) {
            if (node instanceof HBox row) {
                Label label = (Label) row.getChildren().get(0);
                TextField textField = (TextField) row.getChildren().get(1);
                String userName = label.getText();
                String text = textField.getText();

                if (text != null && !text.isBlank()) {
                    try {
                        double amount = Double.parseDouble(text);
                        User matchedUser = selectedUsers.stream()
                            .filter(u -> u.getName().equals(userName))
                            .findFirst()
                            .orElse(null);

                        if (matchedUser != null) {
                            Partition p = new Partition(matchedUser, amount, 0); // percentage left as 0
                            partitionList.add(p);
                        }
                    } catch (NumberFormatException e) {
                        // Optionally show a warning popup
                        System.err.println("Invalid amount for " + userName);
                    }
                }
            }
        }

        setFixedAmountAnchorPane.setVisible(false);
        addExpenseButton.setDisable(false);
    }

    @FXML
    void percentageShareDone(ActionEvent event) {
        partitionList.clear();

        for (Node node : userPercentageShareVBox.getChildren()) {
            if (node instanceof HBox row) {
                Label label = (Label) row.getChildren().get(0);
                TextField textField = (TextField) row.getChildren().get(1);
                String userName = label.getText();
                String text = textField.getText();

                if (text != null && !text.isBlank()) {
                    try {
                        double percentage = Double.parseDouble(text);
                        User matchedUser = selectedUsers.stream()
                            .filter(u -> u.getName().equals(userName))
                            .findFirst()
                            .orElse(null);

                        if (matchedUser != null) {
                            Partition p = new Partition(matchedUser, 0, percentage); // amount left as 0
                            partitionList.add(p);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid percentage for " + userName);
                    }
                }
            }
        }

        setPercantageAnchorPane.setVisible(false);
        addExpenseButton.setDisable(false);
    }



}
