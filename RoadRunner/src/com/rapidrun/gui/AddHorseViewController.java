package com.rapidrun.gui;

import com.rapidrun.model.Horse;
import com.rapidrun.model.HorseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;

import java.io.File;
public class AddHorseViewController {

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField jockeyNameField;
    @FXML private TextField ageField;
    @FXML private TextField breedField;
    @FXML private TextField raceRecordField;
    @FXML private ComboBox<String> groupComboBox;
    @FXML private TextField imagePathField;

    public void initialize() {
        groupComboBox.setItems(FXCollections.observableArrayList("A", "B", "C", "D"));
    }


    @FXML
    protected void handleAddHorse(ActionEvent event) {
        // Validation for the ID field: it should be numeric and unique
        String id = idField.getText();
        if (!id.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Invalid ID", "ID must be a numeric value.");
            return;
        }

        if (!HorseManager.isIdUnique(id)) {
            showAlert(Alert.AlertType.ERROR, "Duplicate ID", "The ID already exists. Please use a unique ID.");
            return;
        }


        String name = nameField.getText();
        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Data", "Please enter a name.");
            return;
        }

        String jockeyName = jockeyNameField.getText(); // Jockey name could be optional depending on your rules
        String breed = breedField.getText();
        if (breed.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Data", "Please enter a breed.");
            return;
        }

        String raceRecord = raceRecordField.getText();
        if (!raceRecord.matches("\\d+/\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Race Record", "Race record must be in the format 'wins/total'.");
            return;
        }

        String group = groupComboBox.getValue();
        if (group == null) {
            showAlert(Alert.AlertType.ERROR, "Missing Data", "Please select a group.");
            return;
        }

        String imagePath = imagePathField.getText();
        if (imagePath.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Data", "Please select an image.");
            return;
        }

        try {
            int age = Integer.parseInt(ageField.getText());
            if (age <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Age", "Age must be a positive number.");
                return;
            }

            // Construct the horse object and add it
            Horse newHorse = new Horse(id, name, jockeyName, age, breed, raceRecord, group, imagePath);
            HorseManager.addHorse(newHorse);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Horse added successfully!");
            clearForm();
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input for age. Please enter a valid number.");
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + ex.getMessage());
        }
    }


    @FXML
    protected void handleBrowseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imagePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    protected void handleCancel(ActionEvent event) {
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.close(); // Close the window
    }

    private void clearForm() {
        idField.clear();
        nameField.clear();
        jockeyNameField.clear();
        ageField.clear();
        breedField.clear();
        raceRecordField.clear();
        groupComboBox.getSelectionModel().clearSelection();
        imagePathField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
