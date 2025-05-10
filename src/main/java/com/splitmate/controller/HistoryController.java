package com.splitmate.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

@Component
public class HistoryController  implements Initializable {

    @FXML private VBox historyVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("HistoryController loaded.");
    }

    
    
}
