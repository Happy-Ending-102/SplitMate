package com.splitmate.controller;

import java.io.IOException;
import java.net.URL;

import org.springframework.stereotype.Component;

import com.splitmate.config.SpringContext;

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

    public void showLoginView() {
        loadView("fxml/login.fxml", "Login - SplitMate");
    }

    public void showSignUpView() {
        loadView("fxml/signup.fxml", "Sign Up - SplitMate");
    }
    
    public void showGroupsView() { 
        loadView("fxml/groupsList.fxml",  "Groups - SplitMate"); 
    }


    public void showAddGroupView(){

    }

    public void showGroupDetailsView(String ID){

    }

    public void showProfileView(){
        loadView("fxml/myProfile.fxml",  "Groups - SplitMate"); 
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

    public void showChangePasswordView() {
        loadView("fxml/changePassword.fxml",  "Groups - SplitMate"); 
    }
}
