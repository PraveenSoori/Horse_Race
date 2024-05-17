package com.rapidrun.gui;

import com.rapidrun.model.Horse;
import com.rapidrun.model.HorseManager;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;

public class ViewHorsesController {
    @FXML private TableView<Horse> horseTable;
    @FXML private TableColumn<Horse, String> colId;
    @FXML private TableColumn<Horse, String> colName;
    @FXML private TableColumn<Horse, String> colJockey;
    @FXML private TableColumn<Horse, Integer> colAge;
    @FXML private TableColumn<Horse, String> colBreed;
    @FXML private TableColumn<Horse, String> colRecord;
    @FXML private TableColumn<Horse, String> colGroup;
    @FXML private TableColumn<Horse, String> colImage; // This column will hold the image view, not the path as a string

    public void initialize() {
        updateTableWithSortedHorses();
        horseTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colJockey.setCellValueFactory(new PropertyValueFactory<>("jockeyName"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colBreed.setCellValueFactory(new PropertyValueFactory<>("breed"));
        colRecord.setCellValueFactory(new PropertyValueFactory<>("raceRecord"));
        colGroup.setCellValueFactory(new PropertyValueFactory<>("group"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        colImage.setCellFactory(column -> new TableCell<Horse, String>() {
            private final ImageView imageView = new ImageView();
            { // initializer block
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
            }

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null || imagePath.isEmpty()) {
                    setGraphic(null);
                } else {
                    File imgFile = new File(imagePath);
                    if (imgFile.exists() && !imgFile.isDirectory()) {
                        Image img = new Image(imgFile.toURI().toString());
                        imageView.setImage(img);
                    } else {
                        imageView.setImage(null);
                    }
                    setGraphic(imageView);
                }
            }
        });


        horseTable.setItems(HorseManager.sortHorsesById());
        horseTable.refresh();


    }

    private void updateTableWithSortedHorses() {
        horseTable.setItems(HorseManager.sortHorsesById());
    }
}
