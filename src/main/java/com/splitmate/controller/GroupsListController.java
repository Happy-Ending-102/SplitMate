package com.splitmate.controller;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import com.splitmate.model.Group;
import com.splitmate.service.GroupService;
import com.splitmate.service.SessionService;
import com.splitmate.service.UserService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

@Component
public class GroupsListController implements Initializable {

    @FXML private VBox groupCardsContainer;

    private final UserService   userService;
    private final SessionService sessionService;
    private final MainController mainController;

    public GroupsListController(UserService userService,
                                SessionService ss,
                                MainController mc) {
        this.userService   = userService;
        this.sessionService = ss;
        this.mainController = mc;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String userId = sessionService.getCurrentUserId();
        List<Group> groups = userService.getGroupsOfUser(userId);
        if (groups == null) groups = Collections.emptyList();

        groupCardsContainer.getChildren().clear();
        groups.forEach(this::renderGroupCard);
    }

    @FXML
    private void onAddGroup(MouseEvent e) {
        mainController.showAddGroupView();
    }

    private void renderGroupCard(Group g) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
          "-fx-background-color:#e0e0e0;" +
          "-fx-background-radius:15;" +
          "-fx-padding:10 15;"
        );

        Region icon = new Region();
        icon.setPrefSize(40,40);
        icon.setStyle(
          "-fx-background-color:#3f51b5;" +
          "-fx-background-radius:20;"
        );

        Label name = new Label(g.getName());
        name.setStyle(
          "-fx-font-size:18px;" +
          "-fx-font-weight:bold;" +
          "-fx-text-fill:#333;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label updated = new Label("last update:");
        updated.setStyle(
          "-fx-font-size:12px;" +
          "-fx-text-fill:#555;"
        );

        card.getChildren().addAll(icon, name, spacer, updated);
        card.setOnMouseClicked(evt -> {
            if (evt.getClickCount() == 2) {
                mainController.showGroupDetailsView(g.getId());
            }
        });

        groupCardsContainer.getChildren().add(card);
    }
}
