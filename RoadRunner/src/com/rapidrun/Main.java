package com.rapidrun;

import com.rapidrun.model.HorseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Loading the data from the file at the start of the program
        HorseManager.loadHorsesFromFile();

        // Load the FXML document for the main view
        Parent root = FXMLLoader.load(getClass().getResource("/com/rapidrun/gui/MainView.fxml"));

        // Create a scene with the loaded FXML document
        Scene scene = new Scene(root);

        // Set the title of the stage
        primaryStage.setTitle("RoadRunner Horse Racing Management");

        // Set the scene for the stage
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
