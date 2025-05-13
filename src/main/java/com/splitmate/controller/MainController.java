package com.splitmate.controller;

import java.io.IOException;
import java.net.URL;

import org.springframework.stereotype.Component;

import com.splitmate.config.SpringContext;
import com.splitmate.model.Expense;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class MainController {

    private Stage primaryStage;
    private Scene mainScene; // keep scene to reuse it

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;

        // Initial dummy scene
        this.mainScene = new Scene(new javafx.scene.layout.StackPane(), 800, 600);

        // Fullscreen settings
        primaryStage.setScene(mainScene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.show();

        
    }

    private void loadView(String fxmlPath, String title) {
        try {
            URL resource = Thread.currentThread().getContextClassLoader().getResource(fxmlPath);
            if (resource == null) {
                throw new RuntimeException("FXML file not found at path: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(resource);
            loader.setControllerFactory(SpringContext.get()::getBean);
            Parent root = loader.load();

            // Reuse the same scene — just set the root
            mainScene.setRoot(root);
            primaryStage.setTitle(title);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLoginView() {
        loadView("fxml/login.fxml", "Login - SplitMate");
    }

    public void showSignUpView() {
        loadView("fxml/signup.fxml", "Sign Up - SplitMate");
    }
    
    public void showGroupsView() { 
        loadView("fxml/groupsList.fxml",  "Groups - SplitMate"); 
    }

    public void showFriendsView() { 
        loadView("fxml/friendList.fxml",  "Groups - SplitMate"); 
    }

    public void showAddGroupView(){
        loadView("fxml/addGroup.fxml",  "Add Group - SplitMate"); 
    }

    public void showNotificationsView(){
        loadView("fxml/NotificationsPage.fxml",  "Notifications - SplitMate"); 
    }

    public void showGroupDetailsView(){
        loadView("fxml/groupOverview.fxml",  "Group Details - SplitMate");
    }

    public void showProfileView(){
        loadView("fxml/myProfile.fxml",  "Groups - SplitMate"); 
    }

    public void showGroupSettingsView(){
        loadView("fxml/groupSettings.fxml",  "Group Settings - SplitMate");
    }

    public void showChangePasswordView(){
        loadView("fxml/changePassword.fxml",  "Group Settings - SplitMate");
    }

    public void showHistoryView(){
        loadView("fxml/history.fxml",  "History - SplitMate");
    }

  
    public void showFriendOverview() {
        loadView("fxml/friendOverwiev.fxml", "Friend Overview - SplitMate");
    }
    
    public void showPaymentsView(){
        loadView("fxml/payments.fxml", "Payments - SplitMate");
    }

    public void showNewGroupExpenseView(){
        loadView("fxml/newGroupExpense.fxml", "Payments - SplitMate");
    }

public void showExpenseDetailView(Expense expense) {
        try {
            // locate the FXML
            URL resource = Thread.currentThread()
                                .getContextClassLoader()
                                .getResource("fxml/expenseDetail.fxml");
            if (resource == null) {
                throw new RuntimeException(
                  "Cannot find fxml/expenseDetail.fxml on classpath"
                );
            }

            // set up loader with Spring
            FXMLLoader loader = new FXMLLoader(resource);
            loader.setControllerFactory(SpringContext.get()::getBean);

            // load the view graph
            Parent detailRoot = loader.load();

            // grab the controller and pass the expense
            ExpenseDetailController ctrl = loader.getController();
            ctrl.setExpense(expense);

            // swap into your main scene
            mainScene.setRoot(detailRoot);
            primaryStage.setTitle("Expense Details - SplitMate");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
