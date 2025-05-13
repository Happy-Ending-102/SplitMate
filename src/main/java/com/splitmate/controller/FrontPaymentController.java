package com.splitmate.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.splitmate.model.Debt;
import com.splitmate.model.User;
import com.splitmate.service.PaymentService;
import com.splitmate.service.SessionService;

import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;



@Component
public class FrontPaymentController  implements Initializable {


    @FXML
    private HBox headerBox;

    @FXML
    private TabPane tabPane;

    @FXML
    private VBox toPayVBox;

    @FXML
    private VBox toRecieveVBox;

    @Autowired private SessionService sessionService;

    @Autowired private PaymentService paymentService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadUserDebts();
    }

    private void loadUserDebts() {
        User currentUser = sessionService.getCurrentUser();
        List<Debt> debts = paymentService.getUserDebts(currentUser.getId());

        toPayVBox.getChildren().clear();
        toRecieveVBox.getChildren().clear();

        for (Debt debt : debts) {
            User from = debt.getFrom();
            User to = debt.getTo();

            Label label = new Label();
            label.setStyle("-fx-padding: 10;-fx-background-radius: 15;-fx-background-color:rgb(231, 227, 231); -fx-font-size: 20px; -fx-font-weight: bold;");
            label.setText(from.getName() + " → " + to.getName() + ": " + String.format("%.2f", debt.getAmount()) + " " + "₺");
            label.getStyleClass().add("debt-label");

            if (from.getId().equals(currentUser.getId())) {
                label.setText(String.format("%.2f", debt.getAmount()) + " " + "₺" + " to " + to.getName() );
                toPayVBox.getChildren().add(label);
            } else if (to.getId().equals(currentUser.getId())) {
                label.setText(from.getName() + " owe you " + String.format("%.2f", debt.getAmount()) + " " + "₺");
                toRecieveVBox.getChildren().add(label);
            }
        }
    }
    
}
