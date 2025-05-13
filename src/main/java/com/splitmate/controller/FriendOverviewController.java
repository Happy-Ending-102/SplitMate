package com.splitmate.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.splitmate.model.Currency;
import com.splitmate.model.FilterType;
import com.splitmate.model.Friendship;
import com.splitmate.model.Group;
import com.splitmate.model.Payment;
import com.splitmate.model.Transaction;
import com.splitmate.model.User;
import com.splitmate.service.*;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FriendOverviewController implements Initializable {

    @FXML private RadioButton bothRadiButton;

    @FXML private ComboBox<Currency> currencyComboBox;

    @FXML private TextField endDateTextField;

    @FXML private Button filterButton;

    @FXML private AnchorPane historyPane;

    @FXML private AnchorPane historySettingsPane;

    @FXML private TextField maximumAmountTextField;

    @FXML private TextField minimumAmountTextField;

    @FXML private ToggleGroup paymentRecievementToggleGroup;

    @FXML private RadioButton paymentsRadioButton;

    @FXML private RadioButton recievementsRadiButton;

    @FXML private TextField startDateTextField;

    @FXML private Button addExpenseButton;

    @FXML private TextField amountToPayTextField;

    @FXML private VBox commonGroupsVBox;

    @FXML private ComboBox<Currency> currencySelectionComboBox;

    @FXML private Label currentStatusLabel;

    @FXML private Label friendIBANLabel;

    @FXML private Label friendIDLabel;

    @FXML private Label friendNameLabel;

    @FXML private Tab infoTab;

    @FXML private Button paymentConfirmationButton;

    @FXML private Tab paymentTab;

    @FXML private VBox paymentVBox;

    @FXML private TabPane tabPane;

    @FXML private VBox transactionHistoryVBox;

    @FXML private Button transactionSettingsButton;

    @FXML private AnchorPane addExpensePopUp;

    @FXML private Button closeExpensePopUpButton;

    @FXML private TextField expenseAmountTextField;

    @FXML private ComboBox<Currency> expenseCurrencySelectionComboBox;

    @FXML private TextField expenseDescriptionTextField;

    @FXML private TextField expenseFriendDivisionTextField;

    @FXML private TextField expenseUserDivisionTextField;

    @FXML private Label friendNamePopUpLabel;

    @FXML private Button saveExpenseButton;


    private User friend;
    private List<Transaction> history;
    private List<Transaction> masterHistory;

    @Autowired private SessionService sessionService;
    @Autowired private UserService userService;
    @Autowired private GroupService groupService;
    @Autowired private PaymentService paymentService;
    @Autowired private FriendshipService friendshipService;
   

    private final MainController mainController;

    public FriendOverviewController(MainController mainController) {
        this.mainController = mainController;
    }   


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currencySelectionComboBox.setItems(FXCollections.observableArrayList(Currency.values()));
        expenseCurrencySelectionComboBox.setItems(FXCollections.observableArrayList(Currency.values()));
        currencyComboBox.setItems(FXCollections.observableArrayList(Currency.values()));
        currencyComboBox.setValue(Currency.TRY);

        friend = sessionService.getCurrentFriend();
        if (friend == null) {
            String id = sessionService.getCurrentGroupId();
            friend = userService.getUser(id);
            sessionService.setCurrentFriend(friend);
        }

        saveExpenseButton.setOnAction(this::saveExpense);
        addExpenseButton.setOnAction(this::addExpense);
        closeExpensePopUpButton.setOnAction(this::closeExpensePopUp);

        friendNameLabel.setText(friend.getName());
        friendIDLabel.setText("ID: " + friend.getId());
        friendIBANLabel.setText("IBAN: " + (friend.getIban() != null ? friend.getIban() : "Not provided"));

        loadCommonGroups();
        // updateCurrentStatus();
        loadTransactionHistory();

    }

    private void loadCommonGroups() {
        User currentUser = sessionService.getCurrentUser();
        List<Group> commonGroups = groupService.getMutualGroups(currentUser.getId(), friend.getId());

        commonGroupsVBox.getChildren().clear();

        if (commonGroups.isEmpty()) {
            Label none = new Label("No common groups found.");
            none.setStyle("-fx-text-fill: gray; -fx-padding: 8;");
            commonGroupsVBox.getChildren().add(none);
            return;
        }

        for (Group group : commonGroups) {
            HBox card = new HBox(15);
            card.setAlignment(Pos.CENTER_LEFT);
            card.setStyle(
                "-fx-background-color:#e0e0e0;" +
                "-fx-background-radius:15;" +
                "-fx-padding:10 15;"
            );

            Label name = new Label(group.getName());
            name.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#333333;"
            );

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            card.getChildren().addAll(name, spacer);
            // cards are non-clickable by design
            commonGroupsVBox.getChildren().add(card);
        }
    }


    public List<Transaction> filterByType(Friendship f,
                                          String currentUserId,
                                          FilterType type){
        return friendshipService.filterByType(f, currentUserId, type);
    }

    public List<Transaction> filterByCurrency(List<Transaction> transactions, Currency currency){
        return friendshipService.filterByCurrency(transactions, currency);
    }

    public List<Transaction> filterByAmountRange(List<Transaction> transactions,
                                                 BigDecimal min,
                                                 BigDecimal max){
        return friendshipService.filterByAmountRange(transactions, min, max);
    }

    public List<Transaction> filterByDateRange(List<Transaction> transactions,
                                               LocalDate start,
                                               LocalDate end){
        return friendshipService.filterByDateRange(transactions, start, end);
    }

    // private void updateCurrentStatus() {
    //     String status = paymentService.getCurrentPaymentStatus(sessionService.getCurrentUser(), friend);
    //     currentStatusLabel.setText(status);
    // }

     private void loadTransactionHistory() {
         User currentUser = sessionService.getCurrentUser();
         Friendship friendship = friendshipService.getFriendshipBetween(currentUser.getId(), friend.getId());

         transactionHistoryVBox.getChildren().clear();

         if (friendship == null) {
             Label error = new Label("Friendship not found.");
             error.setStyle("-fx-padding: 10; -fx-text-fill: red;");
             transactionHistoryVBox.getChildren().add(error);
             this.masterHistory = new ArrayList<>();
            this.history       = new ArrayList<>();
             return;
         }

        List<Transaction> loaded;
        
        try{
            loaded = friendshipService.sortByDateDesc(friendship);
        }
        catch(NullPointerException e){
            // print the message
            System.out.println("No transactions found.");
            loaded = new ArrayList<>();
        }

        this.masterHistory = loaded;
        this.history       = new ArrayList<>(loaded);

        displayHistory(this.history);
    }

    private void displayHistory(List<Transaction> list) {
        transactionHistoryVBox.getChildren().clear();
        if (list.isEmpty()) {
            Label noData = new Label("No transactions yet.");
            noData.setStyle("-fx-padding: 10; -fx-text-fill: gray;");
            transactionHistoryVBox.getChildren().add(noData);
            return;
        }

        User me = sessionService.getCurrentUser();
        for (Transaction t : list) {
            String payer = t.getFrom().equals(me) ? "You" : friend.getName();
            String text  = String.format("%s paid %s %s on %s",
                                payer,
                                t.getCurrency(),
                                t.getAmount(),
                                t.getPaymentDate().toLocalDate());
        Label lbl = new Label(text);
        lbl.setStyle("-fx-padding: 5 10; -fx-font-size: 13px;");
        transactionHistoryVBox.getChildren().add(lbl);
        }
    }

    
    @FXML
    void addExpense(ActionEvent event) {
        friendNamePopUpLabel.setText(friend.getName());
        addExpensePopUp.setVisible(true);
        
    }

    @FXML
    void confirmPayment(ActionEvent event) {
        try {
            BigDecimal amount = new BigDecimal(amountToPayTextField.getText());
            Currency currency = currencySelectionComboBox.getValue();
            User currentUser = sessionService.getCurrentUser();
//public void initializeTransaction(String payerId, String receiverId, BigDecimal amount, Currency currency) {

            paymentService.initializeTransaction(
                currentUser.getId(),
                friend.getId(),
                amount,
                currency
            );
            
            amountToPayTextField.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void showTransactionSettings(ActionEvent event) {
        historyPane.setVisible(false);
        historySettingsPane.setVisible(true);
    }

    @FXML
    void showFilteredTransactionHistory(ActionEvent event) {
        historySettingsPane.setVisible(false);
        historyPane.setVisible(true);
        
        if(masterHistory == null){
            loadTransactionHistory();
            return;
        }

        List<Transaction> filtered = new ArrayList<>(masterHistory);

        Friendship f = friendshipService.getFriendshipBetween(
                        sessionService.getCurrentUserId(), 
                        friend.getId());

        if (paymentsRadioButton.isSelected()) {
            filtered = filterByType(f, sessionService.getCurrentUserId(), FilterType.PAYMENTS);
        } else if (recievementsRadiButton.isSelected()) {
            filtered = filterByType(f, sessionService.getCurrentUserId(), FilterType.RECEIVEMENTS);
        } else {
            filtered = filterByType(f, sessionService.getCurrentUserId(), FilterType.BOTH);
        }

        if (!startDateTextField.getText().isBlank() &&
            !endDateTextField  .getText().isBlank()) {
            LocalDate start = LocalDate.parse(startDateTextField.getText().trim());
            LocalDate end   = LocalDate.parse(endDateTextField.getText().trim());
            filtered = filterByDateRange(filtered, start, end);
        }

        if (!minimumAmountTextField.getText().isBlank() &&
            !maximumAmountTextField.getText().isBlank()) {
            BigDecimal min = new BigDecimal(minimumAmountTextField.getText().trim());
            BigDecimal max = new BigDecimal(maximumAmountTextField.getText().trim());
            filtered = filterByAmountRange(filtered, min, max);
        }

        Currency curr = currencyComboBox.getValue();
        if (curr != null) {
            filtered = filterByCurrency(filtered, curr);
        }

        displayHistory(filtered);

        this.history = filtered;
    }

    @FXML
    void closeExpensePopUp(ActionEvent event) {
        addExpensePopUp.setVisible(false);
    }

    
    @FXML
    private void saveExpense(ActionEvent event) {
        // Reset any previous error
        //errorLabel.setVisible(false);

        String amtText = expenseAmountTextField.getText().trim();
        String userExString = expenseUserDivisionTextField.getText().trim();
        String friendExString = expenseFriendDivisionTextField.getText().trim();
        Currency curr  = expenseCurrencySelectionComboBox.getValue();
        String desc    = expenseDescriptionTextField.getText().trim();

        // 1) Validation
        if (amtText.isEmpty() || curr == null || desc.isEmpty()) {
            //errorLabel.setText("Please fill in amount, currency, and description.");
           // errorLabel.setVisible(true);
            return;
        }

        BigDecimal amount;
        BigDecimal userAmount;
        BigDecimal friendAmount;
        try {
            amount = new BigDecimal(amtText);
            userAmount = new BigDecimal(userExString);
            friendAmount = new BigDecimal(friendExString);

        } catch (NumberFormatException ex) {
           // errorLabel.setText("Enter a valid number for amount.");
           // errorLabel.setVisible(true);
            return;
        }

        // 2) Initialize transaction & send notification
        String senderId   = sessionService.getCurrentUser().getId();
        String receiverId = friend.getId();

        friendshipService.addFriendshipExpense(senderId, receiverId, userAmount, friendAmount, curr);
        // 3) Close popup & clear fields
        addExpensePopUp.setVisible(false);
        expenseAmountTextField.clear();
        expenseDescriptionTextField.clear();
        expenseCurrencySelectionComboBox.getSelectionModel().clearSelection();
    }
    
}
