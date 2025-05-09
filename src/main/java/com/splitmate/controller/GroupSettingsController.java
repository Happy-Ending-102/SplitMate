package com.splitmate.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;

import com.splitmate.model.ConversionPolicy;
import com.splitmate.model.Currency;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

@Controller
public class GroupSettingsController implements Initializable {

    @FXML private Label backLabel;

    @FXML private Button addMemberButton;
    @FXML private Button deleteMemberButton;

    @FXML
    private ComboBox<Currency> currencyComboBox;
    @FXML
    private ComboBox<ConversionPolicy> currencyConversionComboBox;

    @FXML private Button freezeMemberButton;
    @FXML private Button unfreezeMemberButton;

    @FXML private Label currentBudgetLabel;
    @FXML private TextField budgetTextField;

    @FXML private Button saveButton;

    private final MainController mainController;

    public GroupSettingsController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // these will be 
        currencyComboBox.setItems(FXCollections.observableArrayList(Currency.values()));
        // TODO get currency of the group and write here currencyComboBox.setValue(value)
        currencyConversionComboBox.setItems(FXCollections.observableArrayList(ConversionPolicy.values()));
        // TODO get conversion policy of the group and write it currencyComboBox.setValue(value)

        // wire up all actions
        backLabel.setOnMouseClicked(this::onBack);
        addMemberButton.setOnAction(e -> onAddMember());
        deleteMemberButton.setOnAction(e -> onDeleteMember());
        freezeMemberButton.setOnAction(e -> onFreezeMember());
        unfreezeMemberButton.setOnAction(e -> onUnfreezeMember());
        saveButton.setOnAction(e -> onSave());
    }

    private void onBack(MouseEvent e) {
        mainController.showGroupDetailsView();
    }

    private void onAddMember() {
        System.out.println("Add group member clicked");
    }

    private void onDeleteMember() {
        System.out.println("Delete member clicked");
    }

    private void onFreezeMember() {
        System.out.println("Freeze member clicked");
    }

    private void onUnfreezeMember() {
        System.out.println("Unfreeze member clicked");
    }

    private void onSave() {
        
    }
}
