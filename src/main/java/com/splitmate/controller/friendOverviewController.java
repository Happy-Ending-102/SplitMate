package com.splitmate.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.splitmate.model.*;
import com.splitmate.model.Currency;
import com.splitmate.service.*;

public class FriendOverviewController implements Initializable {

        @FXML
        private Button addExpenseButton;
    
        @FXML
        private TextField amountToPayTextField;
    
        @FXML
        private VBox commonGroupsVBox;
    
        @FXML
        private ComboBox<?> currencySelectionComboBox;
    
        @FXML
        private Label currentStatusLabel;
    
        @FXML
        private Label friendIBANLabel;
    
        @FXML
        private Label friendIDLabel;
    
        @FXML
        private Label friendNameLabel;
    
        @FXML
        private Tab infoTab;
    
        @FXML
        private Button paymentConfirmationButton;
    
        @FXML
        private Tab paymentTab;
    
        @FXML
        private VBox paymentVBox;
    
        @FXML
        private TabPane tabPane;
    
        @FXML
        private VBox transactionHistoryVBox;
    
        @FXML
        private Button transactionSettingsButton;

        @Autowired private UserService userService;
        @Autowired private GroupService groupService;
        @Autowired private PaymentService paymentService;
        @Autowired private SessionService sessionService;
    
        @FXML
        void addExpense(ActionEvent event) {
            // TO DO add Expense Screen
        }
    
        // @FXML
        // void confirmPayment(ActionEvent event) {
        //     try {
        //         double amount = Double.parseDouble(amountToPayTextField.getText());
        //         Currency currency = currencySelectionComboBox.getValue();
        
        //         paymentService.sendPayment(sessionService.getCurrentUser(), friend, amount, currency);
        
        //         updateCurrentStatus();
        //         loadTransactionHistory();
        //         amountToPayTextField.clear();
        //     } catch (NumberFormatException e) {
        //         // Optional: Show an error dialog
        //     }
        // }
    
        @FXML
        void showTransactionSettings(ActionEvent event) {
            // TO DO transaction settings screen 
        }

     

        public void initializeFriendData(User friend) {
            this.friend = friend;
            friendNameLabel.setText(friend.getName());
            friendIDLabel.setText("ID: " + friend.getId());
            friendIBANLabel.setText("IBAN: " + friend.getIban());
        
            loadCommonGroups();
            updateCurrentStatus();
            loadTransactionHistory();
        }

        // private void loadCommonGroups() {
        //     User currentUser = sessionService.getCurrentUser();
        //     List<Group> commonGroups = groupService.getCommonGroups(currentUser, friend);
        
        //     commonGroupsVBox.getChildren().clear();
        
        //     for (Group group : commonGroups) {
        //         Label label = new Label(group.getName() + " — Last updated: " + group.getLastUpdated());
        //         label.setStyle("-fx-padding: 5 0 5 10; -fx-font-size: 14px;");
        //         commonGroupsVBox.getChildren().add(label);
        //     }
        // }

        // private void updateCurrentStatus() {
        //     String status = paymentService.getCurrentPaymentStatus(sessionService.getCurrentUser(), friend);
        //     currentStatusLabel.setText(status);
        // }

        // private void loadTransactionHistory() {
        //     List<String> history = paymentService.getTransactionHistory(sessionService.getCurrentUser(), friend);
        //     transactionHistoryVBox.getChildren().clear();
        
        //     for (String entry : history) {
        //         Label label = new Label(entry);
        //         label.setStyle("-fx-padding: 5 10 5 10; -fx-font-size: 13px;");
        //         transactionHistoryVBox.getChildren().add(label);
        //     }
        // }

        
        // @FXML
        // void confirmPayment(ActionEvent event) {
        //     try {
        //         double amount = Double.parseDouble(amountToPayTextField.getText());
        //         Currency currency = currencySelectionComboBox.getValue();

        //         paymentService.sendPayment(sessionService.getCurrentUser(), friend, amount, currency);

        //         updateCurrentStatus();
        //         loadTransactionHistory();
        //         amountToPayTextField.clear();
        //     } catch (NumberFormatException e) {
        //         // Optional: Show an error dialog
        //     }
        // }
    
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currencySelectionComboBox.setItems(FXCollections.observableArrayList(Currency.values()));
    }

}
