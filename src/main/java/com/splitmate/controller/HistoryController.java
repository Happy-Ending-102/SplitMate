package com.splitmate.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.splitmate.model.Payment;
import com.splitmate.model.Transaction;
import com.splitmate.model.User;
import com.splitmate.service.SessionService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

@Component
public class HistoryController  implements Initializable {

    @FXML private VBox historyVBox;

    @Autowired private SessionService sessionService;
    // @Autowired private PaymentService paymentService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User currentUser = sessionService.getCurrentUser();

        if (currentUser == null || currentUser.getHistory() == null || currentUser.getHistory().isEmpty()) {
            Label noHistory = new Label("No transaction history yet.");
            noHistory.setStyle("-fx-text-fill: gray; -fx-font-weight: bold; -fx-font-size: 20px;");
            historyVBox.getChildren().add(noHistory);
            return;
        }

        List<Transaction> history = currentUser.getHistory();

        for (Transaction transaction : history) {
            String payer = transaction.getFrom().getId().equals(currentUser.getId()) ? "You" : transaction.getFrom().getName();
            String receiver = transaction.getTo().getId().equals(currentUser.getId()) ? "You" : transaction.getTo().getName();

            String summary = String.format("%s paid %s %.2f %s on %s",
                    payer,
                    receiver,
                    transaction.getAmount(),
                    transaction.getCurrency(),
                    transaction.getPaymentDate().toLocalDate());

            Label entry = new Label(summary);
            entry.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 8; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-weight: bold; -fx-font-size: 20px;");
            historyVBox.getChildren().add(entry);
        }
    }
    
    
}
