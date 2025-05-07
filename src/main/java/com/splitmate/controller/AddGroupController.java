package com.splitmate.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class addGroupController implements Initializable{

    @FXML
    private Button addImageButton;

    @FXML
    private ComboBox<?> currencyComboBox;

    @FXML
    private ComboBox<?> currencyConversionComboBox;

    @FXML
    private TextField friendName;

    @FXML
    private ScrollPane friendsScrollPane;

    @FXML
    private ImageView groupImage;

    @FXML
    private TextField groupName;

    @FXML
    private Button groupSaveButton;

    @FXML
    void addGroupImage(ActionEvent event) {

    }

    @FXML
    void saveGroup(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }

}
