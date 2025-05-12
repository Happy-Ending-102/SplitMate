package com.splitmate.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.splitmate.model.Expense;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

@Controller
public class ExpenseDetailController implements Initializable {
    @FXML private TextField ownerField, amountField, descField, currencyField;
    @FXML private VBox contributorsList;
    @Autowired private MainController mainController;

    private Expense expense;

    public void setExpense(Expense expense) {
        this.expense = expense;
        ownerField.setText(expense.getOwner().getName());
        amountField.setText(String.format("%.2f", expense.getAmount()));
        descField.setText(expense.getDescription());
        currencyField.setText(expense.getCurrency().name());
        contributorsList.getChildren().clear();
        expense.getDivisionAmongUsers().forEach(u -> {
            Label l = new Label(u.getUser().getName() + "  –  " +
                String.format("%.2f", u.getPercentage()));
            contributorsList.getChildren().add(l);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // nothing here
    }

    @FXML
    private void onBack() {
       mainController.showGroupDetailsView();
    }
}
