//package com.rapidrun.gui;
//
//import com.rapidrun.model.Horse;
//import com.rapidrun.model.HorseManager;
//import javafx.fxml.FXML;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//
//public class SelectedHorsesViewController {
//
//    @FXML
//    private TableView<Horse> tableView;
//    @FXML
//    private TableColumn<Horse, String> colGroup;
//    @FXML
//    private TableColumn<Horse, String> colId;
//    @FXML
//    private TableColumn<Horse, String> colName;
//
//    @FXML
//    public void initialize() {
//        // Initialize the TableView with data from HorseManager
//        updateTableView();
//    }
//
//    public void updateTableView() {
//        tableView.getItems().setAll(HorseManager.getSelectedRaceHorses().values());
//    }
//}





package com.rapidrun.gui;

import com.rapidrun.model.Horse;
import com.rapidrun.model.HorseManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import java.io.File;

public class SelectedHorsesViewController {

    @FXML
    private TableView<Horse> tableView;
    @FXML
    private TableColumn<Horse, String> colImage;

    @FXML
    public void initialize() {
        setupImageColumn();
        tableView.setItems(FXCollections.observableArrayList(HorseManager.getSelectedRaceHorses().values()));
    }
    private void setupImageColumn() {
        colImage.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Horse, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Horse, String> param) {
                return param.getValue().imagePathProperty();
            }
        });

        colImage.setCellFactory(column -> new TableCell<Horse, String>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(50); // Set the image view size
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
                        Image img = new Image(imgFile.toURI().toString(), true);
                        imageView.setImage(img);
                    } else {
                        // Set a default or placeholder image if the file doesn't exist
                        imageView.setImage(new Image("/path/to/placeholder/image.png", true));
                    }
                    setGraphic(imageView);
                }
            }
        });
    }
}
