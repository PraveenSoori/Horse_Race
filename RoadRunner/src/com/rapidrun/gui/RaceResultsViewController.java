package com.rapidrun.gui;

import com.rapidrun.model.Horse;
import com.rapidrun.model.HorseManager;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.util.List;

public class RaceResultsViewController {

    @FXML
    private BarChart<String, Number> raceResultsChart;

    // Method to populate the chart with race data for the top three horses
    public void updateRaceResults() {
        raceResultsChart.getData().clear();
        List<Horse> topThreeHorses = HorseManager.getTopThreeHorses(); // Get only the top three horses

        topThreeHorses.forEach(horse -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(horse.getName() + " (" + horse.getRaceTime() + "s)");
            series.getData().add(new XYChart.Data<>("Finish Time", horse.getRaceTime()));
            raceResultsChart.getData().add(series);
        });
    }


    public void cleanupOnClose() {

    }
}
