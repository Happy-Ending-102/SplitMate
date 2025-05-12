package com.splitmate.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.splitmate.model.Currency;
import com.splitmate.model.Group;
import com.splitmate.model.User;
import com.splitmate.service.GroupService;
import com.splitmate.service.SessionService;

import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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

    @Autowired private SessionService sessionService;
    @Autowired private GroupService   groupService;

    private final MainController mainController;

    public NewGroupExpenseController(MainController mainController) {
        this.mainController = mainController;
    }   

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
    }

    @FXML
    void closeNewExpenseScreen(ActionEvent event) {
        mainController.showGroupDetailsView();
    }

    @FXML
    void setFixedAmount(ActionEvent event) {
        // TO DO
    }

    @FXML
    void setPercantage(ActionEvent event) {
        // TO DO
    }



}
