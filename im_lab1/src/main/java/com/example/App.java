package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(@SuppressWarnings("exports") Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml")); 
        Scene scene = new Scene(root);
        stage.setScene(scene);
         
        stage.setTitle("Flight in the atmosphere");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}