package com.splitmate.controller;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import com.splitmate.service.SessionService;

@Component
public class NavigationBarController {

    private final MainController main;
    private final SessionService sessionService;

    public NavigationBarController(MainController main,
                                   SessionService sessionService) {
        this.main = main;
        this.sessionService = sessionService;
    }

    @FXML
    private void gotoGroups() {
        main.showGroupsView();
    }

    @FXML
    private void gotoFriends() {
        // main.showFriendsView();
    }

    @FXML
    private void gotoNotifications() {
        // main.showNotificationsView();
    }

    @FXML
    private void gotoPayments() {
        // main.showPaymentsView();
    }

    @FXML
    private void gotoHistory() {
        // main.showHistoryView();
        main.showProfileView();
    }

    @FXML
    private void gotoProfile(MouseEvent evt) {
        //main.showProfileView();
    }

    @FXML
    private void logout() {
        // Clear the current user session and return to login
        sessionService.clear();
        main.showLoginView();
    }
}
