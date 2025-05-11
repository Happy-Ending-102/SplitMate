package com.splitmate.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import javafx.fxml.Initializable;

import javafx.fxml.FXML;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO 
        System.out.println("Payment screen opened.");
    }
    
}
