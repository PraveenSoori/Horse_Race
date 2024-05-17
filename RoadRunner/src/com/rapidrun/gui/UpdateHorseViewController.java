package com.rapidrun.gui;

import com.rapidrun.model.Horse;
import com.rapidrun.model.HorseManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class UpdateHorseViewController {

    @FXML
    TextField idField; // Non-editable
    @FXML
    TextField nameField;
    @FXML
    TextField jockeyNameField;
    @FXML
    TextField ageField;
    @FXML
    TextField breedField;
    @FXML
    TextField raceRecordField;
    @FXML
    ComboBox<String> groupComboBox;
    @FXML
    TextField imagePathField;

    private Horse horse;

    @FXML
    public void initialize() {
        groupComboBox.setItems(FXCollections.observableArrayList("A", "B", "C", "D"));
    }

    public void setHorse(Horse horse) {
        this.horse = horse;
        idField.setText(horse.getId());
        nameField.setText(horse.getName());
        jockeyNameField.setText(horse.getJockeyName());
        ageField.setText(String.valueOf(horse.getAge()));
        breedField.setText(horse.getBreed());
        raceRecordField.setText(horse.getRaceRecord());
        groupComboBox.setValue(horse.getGroup());
        imagePathField.setText(horse.getImagePath());
    }


    @FXML
    private void handleUpdateHorse() {
        try {
            // Ensure no fields are empty
            if (nameField.getText().isEmpty() || breedField.getText().isEmpty() || raceRecordField.getText().isEmpty() ||
                    groupComboBox.getValue() == null || imagePathField.getText().isEmpty()) {
                showAlert("Error", "All fields are required. Please fill out all fields.");
                return;
            }

            // Validate the age field for a valid integer and sensible age range
            int age = Integer.parseInt(ageField.getText());
            if (age <= 0 ) {
                showAlert("Error", "Invalid age. Age must be a positive number.");
                return;
            }


            // Update horse details
            horse.setName(nameField.getText());
            horse.setJockeyName(jockeyNameField.getText());
            horse.setAge(age);
            horse.setBreed(breedField.getText());
            horse.setRaceRecord(raceRecordField.getText());
            horse.setGroup(groupComboBox.getValue());
            horse.setImagePath(imagePathField.getText());

            // Update the horse in the manager
            HorseManager.updateHorse(horse);
            showAlert("Success", "The horse details have been updated successfully.");

            // Close the dialog
            closeStage();
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number for age.");
        } catch (Exception e) {
            showAlert("Error", "An error occurred while updating the horse: " + e.getMessage());
        }
    }




    @FXML
    private void handleBrowseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePathField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleCancel() {
        if (confirmCancel()) {
            closeStage();
        }
    }

    private boolean confirmCancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel the update?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Cancel Update");
        return alert.showAndWait().filter(response -> response == ButtonType.YES).isPresent();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void closeStage() {
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.close();
    }
}
