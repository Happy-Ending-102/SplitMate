package com.splitmate.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.splitmate.model.Currency;
import com.splitmate.model.Frequency;
import com.splitmate.model.Group;
import com.splitmate.model.OneTimeExpense;
import com.splitmate.model.Partition;
import com.splitmate.model.RecurringExpense;
import com.splitmate.model.User;
import com.splitmate.repository.PartitionRepository;
import com.splitmate.service.ExpenseService;
import com.splitmate.service.GroupService;
import com.splitmate.service.SessionService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

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
    private ComboBox<User> groupMembersComboBox;
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

    private User expenseOwner;


    @Autowired private SessionService sessionService;
    @Autowired private GroupService   groupService;
    @Autowired private ExpenseService expenseService;
    @Autowired private PartitionRepository partitionRepository;

    private final MainController mainController;
    private BigDecimal semihAmount;

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

                groupMembersComboBox.setItems(FXCollections.observableArrayList(currentGroup.getMembers()));
                groupMembersComboBox.setConverter(new StringConverter<>() {
                    @Override
                    public String toString(User user) {
                        return (user != null) ? user.getName() : "";
                    }

                    @Override
                    public User fromString(String string) {
                        // Not needed
                        return null;
                    }
                });
            }
        }
    }


    @FXML
    void addExpense(ActionEvent event) {

        expenseOwner = groupMembersComboBox.getValue();

        if (expenseOwner == null) {
            showError("Please select an expense owner from the dropdown");
            return;
        }

// 1) Grab the group and the raw inputs
        String groupId = sessionService.getCurrentGroupId();
        if (groupId == null) {
            showError("No group selected");
            return;
        }

        String amtText = expenseAmountTextField.getText().trim();
        BigDecimal amount;
        try {
            amount = new BigDecimal(amtText);
            semihAmount = amount;
        } catch (NumberFormatException e) {
            showError("Amount must be a valid number");
            return;
        }

        Currency currency = currencyComboBox.getValue();
        if (currency == null) {
            showError("Please pick a currency");
            return;
        }

        String description = expenseDescriptionTextField.getText().trim();

        // 2) Build the right Expense subtype
        com.splitmate.model.Expense e;
        if (oneTimeExpenseRadioButton.isSelected()) {
            OneTimeExpense ote = new OneTimeExpense();
            ote.setDate(LocalDate.now());
            e = ote;
        } else {
            RecurringExpense re = new RecurringExpense();
            // map your freq toggles → Frequency enum
            if (weeklyFrequencyRadiButton.isSelected()) {
                re.setFrequency(Frequency.WEEKLY);
            } else if (monthlyFrequencyRadiButton.isSelected()) {
                re.setFrequency(Frequency.MONTHLY);
            } else {
                showError("Please choose a frequency");
                return;
            }
            e = re;
        }

        // 3) Finish wiring the common fields
        e.setAmount(amount);
        e.setCurrency(currency);
        e.setDescription(description);
        e.setOwner(expenseOwner);
        e.setDivisionAmongUsers(partitionList);

        // 4) Call your service to save it
        try {
            expenseService.addExpense(groupId, e);
        } catch (Exception ex) {
            showError("Failed to save expense: " + ex.getMessage());
            return;
        }

        // 5) Return to the group details screen
        mainController.showGroupDetailsView();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
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

                            String amtText = expenseAmountTextField.getText().trim();
                            BigDecimal amount;
                            amount = new BigDecimal(amtText);
                            semihAmount = amount;

                            Partition p = new Partition(matchedUser, amount, -5, semihAmount); // percentage left as negative
                            partitionRepository.save(p);
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
                            String amtText = expenseAmountTextField.getText().trim();
                            BigDecimal amount;
                            amount = new BigDecimal(amtText);
                            semihAmount = amount;

                            Partition p = new Partition(matchedUser, -5, percentage, semihAmount); // amount left as negative
                            partitionRepository.save(p);
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
