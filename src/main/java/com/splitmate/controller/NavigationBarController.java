package com.splitmate.controller;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

@Component
public class NavigationBarController {

    private final MainController main;

    public NavigationBarController(MainController main) {
        this.main = main;
    }

    @FXML private void gotoGroups() {
        main.showGroupsView();
    }

    @FXML private void gotoFriends() {
        // main.showFriendsView();
    }

    @FXML private void gotoNotifications() {
        // main.showNotificationsView();
    }

    @FXML private void gotoPayments() {
        // main.showPaymentsView();
    }

    @FXML private void gotoHistory() {
        // main.showHistoryView();
        main.showProfileView();
    }

    @FXML private void gotoProfile(MouseEvent evt) {
        // main.showProfileView();
    }

    @FXML
    private void logout() {
        // Navigate back to the login screen
        main.showLoginView();
    }
}
