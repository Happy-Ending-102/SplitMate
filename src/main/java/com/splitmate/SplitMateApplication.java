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

/**
 * Entry point that bootstraps Spring and then launches JavaFX.
 */
@SpringBootApplication
public class SplitMateApplication extends Application {

    private ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        // Launches the JavaFX runtime, which calls init() → start()
        launch(args);
    }

    @Override
    public void init() {
        // Start the Spring context
        springContext = SpringApplication.run(SplitMateApplication.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load your FXML via Spring-aware FXMLLoader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        loader.setControllerFactory(springContext::getBean);
        Parent root = loader.load();

        primaryStage.setTitle("SplitMate");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Clean shutdown of Spring and JavaFX
        springContext.close();
        Platform.exit();
    }
}
