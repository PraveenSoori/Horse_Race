package com.rapidrun.gui;

import com.rapidrun.model.Horse;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UpdateHorseViewControllerTest {

    private UpdateHorseViewController controller;
    private Horse horse;

    @BeforeAll
    void setUpClass() {
        // Ensures JavaFX is initialized (creates a JFXPanel which initializes JavaFX Platform)
        new JFXPanel();
    }

    @BeforeEach
    void setUp() {
        // Must run initialization of JavaFX controls on the JavaFX application thread
        Platform.runLater(() -> {
            controller = new UpdateHorseViewController();
            controller.idField = new TextField();
            controller.nameField = new TextField();
            controller.jockeyNameField = new TextField();
            controller.ageField = new TextField();
            controller.breedField = new TextField();
            controller.raceRecordField = new TextField();
            controller.groupComboBox = new ComboBox<>();
            controller.imagePathField = new TextField();

            horse = new Horse("1", "TestHorse", "TestJockey", 5, "TestBreed", "5/10", "A", "path/to/image.jpg");
            controller.setHorse(horse);
        });
    }

    @Test
    void testSetHorse() {
        Platform.runLater(() -> {
            assertEquals("1", controller.idField.getText(), "The ID field should contain the horse ID.");
            assertEquals("TestHorse", controller.nameField.getText(), "The name field should contain the horse name.");
            assertEquals("TestJockey", controller.jockeyNameField.getText(), "The jockey name field should contain the jockey's name.");
            assertEquals("5", controller.ageField.getText(), "The age field should contain the horse age.");
            assertEquals("TestBreed", controller.breedField.getText(), "The breed field should contain the horse breed.");
            assertEquals("5/10", controller.raceRecordField.getText(), "The race record field should contain the horse race record.");
            assertEquals("A", controller.groupComboBox.getValue(), "The group combo box should contain the horse group.");
            assertEquals("path/to/image.jpg", controller.imagePathField.getText(), "The image path field should contain the path to the horse image.");
        });
    }
}
