package com.gauravghati;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("To Do List :");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
    }
    
    // comment for example

    public static void main(String[] args) {
        launch(args);
    }
}
