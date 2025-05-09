package com.splitmate.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.splitmate.model.Currency;
import com.splitmate.model.Friendship;
import com.splitmate.model.Group;
import com.splitmate.model.Payment;
import com.splitmate.model.User;
import com.splitmate.service.*;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FriendOverviewController implements Initializable {


    @FXML
    private Button addExpenseButton;

    @FXML
    private TextField amountToPayTextField;

    @FXML
    private VBox commonGroupsVBox;

    @FXML
    private ComboBox<Currency> currencySelectionComboBox;

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

    @FXML
    void addExpense(ActionEvent event) {

    }

    @FXML
    void confirmPayment(ActionEvent event) {
    //     try {
    //         BigDecimal amount = new BigDecimal(amountToPayTextField.getText());
    //         Currency currency = currencySelectionComboBox.getValue();
    //         User currentUser = sessionService.getCurrentUser();

    //         paymentService.sendPayment(currentUser, friend, amount, currency);

    //         updateCurrentStatus();
    //         loadTransactionHistory();
    //         amountToPayTextField.clear();

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    }

    @FXML
    void showTransactionSettings(ActionEvent event) {

    }

    private User friend;

    @Autowired private SessionService sessionService;
    @Autowired private UserService userService;
    @Autowired private GroupService groupService;
    // @Autowired private PaymentService paymentService;
    @Autowired private FriendshipService friendshipService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currencySelectionComboBox.setItems(FXCollections.observableArrayList(Currency.values()));
    }

    public void initializeFriendData(User friend) {
        this.friend = friend;

        friendNameLabel.setText(friend.getName());
        friendIDLabel.setText("ID: " + friend.getId());
        friendIBANLabel.setText("IBAN: " + (friend.getIban() != null ? friend.getIban() : "Not provided"));

        loadCommonGroups();
        // updateCurrentStatus();
        // loadTransactionHistory();
    }

    private void loadCommonGroups() {
        User currentUser = sessionService.getCurrentUser();
        List<Group> commonGroups = groupService.getMutualGroups(currentUser.getId(), friend.getId());

        commonGroupsVBox.getChildren().clear();

        for (Group group : commonGroups) {
            Label label = new Label(group.getName() + " — Last updated: "); //+ group.getLastUpdated());
            label.setStyle("-fx-padding: 5 0 5 10; -fx-font-size: 14px;");
            commonGroupsVBox.getChildren().add(label);
        }
    }

    // private void updateCurrentStatus() {
    //     String status = paymentService.getCurrentPaymentStatus(sessionService.getCurrentUser(), friend);
    //     currentStatusLabel.setText(status);
    // }

    // private void loadTransactionHistory() {
    //     User currentUser = sessionService.getCurrentUser();
        // Friendship friendship = friendshipService.getFriendshipBetween(currentUser.getId(), friend.getId());

    //     transactionHistoryVBox.getChildren().clear();

    //     if (friendship == null) {
    //         Label error = new Label("Friendship not found.");
    //         error.setStyle("-fx-padding: 10; -fx-text-fill: red;");
    //         transactionHistoryVBox.getChildren().add(error);
    //         return;
    //     }

    //     List<Payment> history = friendshipService.sortByDateDesc(friendship);

    //     if (history == null || history.isEmpty()) {
    //         Label noData = new Label("No transactions yet.");
    //         noData.setStyle("-fx-padding: 10; -fx-text-fill: gray;");
    //         transactionHistoryVBox.getChildren().add(noData);
    //         return;
    //     }

    //     for (Payment p : history) {
    //         String payer = p.getFrom().equals(currentUser) ? "You" : friend.getName();
    //         String text = payer + " paid ₺" + p.getAmount() + " on " + p.getPaymentDate().toLocalDate();

    //         Label label = new Label(text);
    //         label.setStyle("-fx-padding: 5 10 5 10; -fx-font-size: 13px;");
    //         transactionHistoryVBox.getChildren().add(label);
    //     }
    // }


    
}
