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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.splitmate.model.*;
import com.splitmate.model.Currency;
import com.splitmate.service.*;

public class friendOverviewController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }

}
