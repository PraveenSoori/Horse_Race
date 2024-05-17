package com.rapidrun.gui;

import com.rapidrun.model.Horse;
import com.rapidrun.model.HorseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
public class MainViewController {

    boolean raceStarted = false;
    @FXML
    private Button addHorseButton;

    @FXML
    private Button deleteHorseButton;

    @FXML
    private Button updateHorseButton;

    @FXML
    private Button viewAllHorsesButton;

    @FXML
    private Button viewSelectedHorsesButton;

    @FXML
    private Button startRaceButton;

    @FXML
    private Button displayRaceResultsButton;


    @FXML
    public void initialize() {
        // Initially set the button for displaying results as disabled
        displayRaceResultsButton.setDisable(true);
        startRaceButton.setDisable(true);
    }


    @FXML
    private void handleAddHorse(ActionEvent event) {
        try {

            if (raceStarted) {
                showAlert("Operation Disabled", "You cannot add a horse during or after the race.");
                return;
            }
            // Load the FXML file for the AddHorseView
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/rapidrun/gui/AddHorseView.fxml"));
            Parent addHorseView = fxmlLoader.load();

            // Create a new stage for the popup window
            Stage addHorseStage = new Stage();
            addHorseStage.setTitle("Add Horse");
            addHorseStage.initModality(Modality.APPLICATION_MODAL);
            addHorseStage.setScene(new Scene(addHorseView));

            // Show the new stage and wait for it to be closed
            addHorseStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper error handling
        }
    }

    //========================================================================================================

    @FXML
    private void handleDeleteHorse() {
        if (raceStarted) {
            showAlert("Operation Disabled", "You cannot delete a horse during or after the race.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Horse");
        dialog.setHeaderText("Delete Horse Details");
        dialog.setContentText("Please enter the Horse ID:");

        dialog.showAndWait().ifPresent(horseId -> {
            if (!horseId.trim().isEmpty() && HorseManager.deleteHorse(horseId.trim())) {
                showAlert("Success", "Horse with ID " + horseId + " was deleted successfully.");
            } else {
                showAlert("Error", "No horse found with ID " + horseId + ", or the ID was empty.");
            }
        });
    }


    //=============================================================================================================

    @FXML
    private void handleUpdateHorseAction() {

        if (raceStarted) {
            showAlert("Operation Disabled", "You cannot update horse details during or after the race.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update Horse Details");
        dialog.setHeaderText("Please enter the ID of the horse to update:");
        dialog.showAndWait().ifPresent(id -> {
            Horse horse = HorseManager.getHorseById(id.trim());
            if (horse != null) {
                openUpdateHorseDialog(horse);
            } else {
                showAlert("Horse Not Found", "No horse found with ID " + id);
            }
        });
    }

    private void openUpdateHorseDialog(Horse horse) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/rapidrun/gui/UpdateHorseView.fxml"));
            Parent root = loader.load();

            UpdateHorseViewController controller = loader.getController();
            controller.setHorse(horse);

            Stage stage = new Stage();
            stage.setTitle("Update Horse Details");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the update dialog: " + e.getMessage());
        }
    }

    //====================================================================================================

    @FXML
    private void handleViewHorses(ActionEvent event) {
        try {
            // Load the FXML file for viewing horse details
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/rapidrun/gui/ViewHorses.fxml"));
            Parent viewHorsesView = fxmlLoader.load();

            // Create a new stage for the popup window
            Stage viewHorsesStage = new Stage();
            viewHorsesStage.setTitle("View Registered Horses");
            viewHorsesStage.initModality(Modality.APPLICATION_MODAL);
            viewHorsesStage.setScene(new Scene(viewHorsesView));

            // Show the new stage and wait for it to be closed
            viewHorsesStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //===================================================================================================
    public void handleViewSelectedHorses() {

        if (!HorseManager.hasMinimumSelectedHorsesPerGroup()) {
            showAlert("Cannot Start Race", "Every group must have at least one horse to start the race.");
            return;
        }

        startRaceButton.setDisable(false); // Enable button to display results
        HorseManager.selectTopHorsesRandomly();
        showSelectedHorsesView(); // Open the view with selected horses
    }


    public void showSelectedHorsesView() {

        if (!HorseManager.hasMinimumSelectedHorsesPerGroup()) {
            showAlert("Cannot Start Race", "Every group must have at least one horse to start the race.");
            return;
        }
        try {
            URL url = getClass().getResource("/com/rapidrun/gui/SelectedHorsesView.fxml");
            if (url == null) {
                throw new IOException("Cannot find FXML file.");
            }
            FXMLLoader loader = new FXMLLoader(url);
            VBox root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Selected Horses");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load the FXML file:");
            e.printStackTrace();
        }
    }



    //=====================================================================================================



    @FXML
    private void handleStartRace() {
        if (!HorseManager.hasMinimumSelectedHorsesPerGroup()) {
            showAlert("Cannot Start Race", "Every group must have at least one horse to start the race.");
            return;
        }
        try {
            HorseManager.simulateRaceForSelectedHorses(); // Simulate the race
            List<Horse> sortedHorses = HorseManager.getTopThreeHorses(); // Fetch the top three sorted horses
            displayTextualRaceResults(sortedHorses); // Display textual race results

            raceStarted = true; // Race starts
            disableButtons(true); // Disables certain buttons
            displayRaceResultsButton.setDisable(false); // Enable button to display results

        } catch (Exception e) {
            showAlert("Error during race simulation", e.getMessage());
        }
    }


    private void displayTextualRaceResults(List<Horse> sortedHorses) {
        StringBuilder results = new StringBuilder("Race Results:\n");
        String[] suffixes = {"st", "nd", "rd", "th"};

        for (int i = 0; i < sortedHorses.size(); i++) {
            Horse horse = sortedHorses.get(i);
            String suffix = (i < 3) ? suffixes[i] : suffixes[3]; // Assign the correct suffix

            // Formatted output for each horse
            results.append(i + 1).append(suffix).append(" place - ")
                    .append(String.format("%.2f", horse.getRaceTime())).append("s - ")
                    .append("ID: ").append(horse.getId()).append(" - ") // Display horse ID
                    .append(horse.getName()).append(" - ")
                    .append(horse.getBreed()).append("\n");
        }

        Alert resultsAlert = new Alert(Alert.AlertType.INFORMATION, results.toString(), ButtonType.OK);
        resultsAlert.setHeaderText("Initial Race Results");
        resultsAlert.showAndWait();
    }



    @FXML
    private void displayRaceResults() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/rapidrun/gui/RaceResultsView.fxml"));
            Parent root = loader.load();  // Load the view

            // Fetch the controller and call a method to update the view
            RaceResultsViewController controller = loader.getController();
            controller.updateRaceResults();

            Stage stage = new Stage();
            stage.setTitle("Race Results");
            stage.setScene(new Scene(root));
            stage.show();


            stage.setOnCloseRequest(event -> controller.cleanupOnClose());

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error displaying race results: " + e.getMessage());
            alert.showAndWait();
        }
    }

    //======================================================================================================



    private void disableButtons(boolean disable) {
        addHorseButton.setDisable(disable);
        deleteHorseButton.setDisable(disable);
        updateHorseButton.setDisable(disable);

    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
