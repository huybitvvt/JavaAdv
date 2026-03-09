package com.bookstore;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main Application Class - JavaFX Entry Point
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the login FXML
        Parent root = FXMLLoader.load(getClass().getResource("/com/bookstore/view/login.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/bookstore/css/style.css").toExternalForm());

        primaryStage.setTitle("Quản Lý Cửa Hàng Sách");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
