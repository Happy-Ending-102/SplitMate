package com.splitmate;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.splitmate.config.SpringContext;
import com.splitmate.controller.MainController;

@SpringBootApplication
public class SplitMateApplication extends Application {

    private ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        // Start the Spring context
        springContext = SpringApplication.run(SplitMateApplication.class);

    }


    @Override
    public void start(Stage primaryStage) {
        try {
            MainController mainController = springContext.getBean(MainController.class);
            mainController.setPrimaryStage(primaryStage);
            mainController.showLoginView();  // SHOW LOGIN SCENE
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        springContext.close();
        Platform.exit();
    }
}
