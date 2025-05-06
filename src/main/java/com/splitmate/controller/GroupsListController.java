package com.splitmate.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import com.splitmate.model.Group;
import com.splitmate.service.GroupService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

@Component
public class GroupsListController implements Initializable {

    @FXML private TableView<Group> groupsTable;
    @FXML private TableColumn<Group, String> nameCol;
    @FXML private TableColumn<Group, String> currencyCol;
    @FXML private TableColumn<Group, Integer> membersCol;
    @FXML private Button addGroupButton;

    private final GroupService groupService;
    private final MainController mainController;

    public GroupsListController(GroupService groupService,
                                MainController mainController) {
        this.groupService = groupService;
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // bind columns
        // nameCol.setCellValueFactory(cell -> 
        //     new SimpleStringProperty(cell.getValue().getName()));

        // // load data
        // groupsTable.setItems(
        //     FXCollections.observableList(groupService.getAllGroups())
        // );
    }

    /** Fired when user clicks the “Add Group” button */
    @FXML
    private void onAddGroup() {
        mainController.showAddGroupView();
    }

    /** Double-click on a row opens that group’s details */
    @FXML
    private void onGroupSelected(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            Group g = groupsTable.getSelectionModel().getSelectedItem();
            if (g != null) {
                mainController.showGroupDetailsView(g.getId());
            }
        }
    }
}