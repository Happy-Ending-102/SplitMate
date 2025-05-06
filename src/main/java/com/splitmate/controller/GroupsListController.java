package com.splitmate.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.splitmate.config.SpringContext;
import com.splitmate.model.Group;
import com.splitmate.service.GroupService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;

import org.springframework.stereotype.Component;

@Component
public class GroupsListController implements Initializable {

    @FXML private BorderPane navContainer;
    @FXML private TableView<Group> groupsTable;
    @FXML private TableColumn<Group, String> nameCol;
    @FXML private TableColumn<Group, String> currencyCol;
    @FXML private TableColumn<Group, Integer> membersCol;
    @FXML private Button addGroupButton;

    private final GroupService groupService;
    private final MainController mainController;

    public GroupsListController(GroupService groupService, MainController mainController) {
        this.groupService = groupService;
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/navigationBar.fxml"));
            loader.setControllerFactory(SpringContext.get()::getBean);
            Parent nav = loader.load();
            navContainer.setCenter(nav);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set up table etc.
    }

    @FXML
    private void onAddGroup() {
        mainController.showAddGroupView();
    }

    @FXML private VBox groupCardsContainer;

    private void renderGroupCard(Group group) {
        HBox card = new HBox(15);
        card.setStyle("-fx-background-color:#e0e0e0; -fx-background-radius:15; -fx-padding:10 15;");
        card.setAlignment(Pos.CENTER_LEFT);

        StackPane icon = new StackPane();
        icon.setPrefSize(40, 40);
        icon.setStyle("-fx-background-color:#3f51b5; -fx-background-radius:20;");
        // Optionally add ImageView

        Label name = new Label(group.getName());
        name.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-text-fill:#333;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label updated = new Label("last update:");
        updated.setStyle("-fx-font-size:12px; -fx-text-fill:#555;");

        card.getChildren().addAll(icon, name, spacer, updated);
        groupCardsContainer.getChildren().add(card);
    }


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
