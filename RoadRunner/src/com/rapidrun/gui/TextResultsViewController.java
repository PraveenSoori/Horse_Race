package com.rapidrun.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class TextResultsViewController {
    @FXML
    private TextArea resultsTextArea;

    public void setRaceResults(String results) {
        resultsTextArea.setText(results);
    }
}
