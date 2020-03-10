package org.alduthir.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import org.alduthir.App;
import org.alduthir.measure.Measure;
import org.alduthir.measure.MeasureCellFactory;
import org.alduthir.song.Song;

import java.io.IOException;

public class MeasureController extends App {


    @FXML
    public JFXButton editButton;
    public JFXButton playButton;
    public JFXListView<Measure> measureList;
    public JFXButton addMeasureButton;
    public JFXButton reuseMeasureButton;
    public JFXButton deleteMeasureButton;
    public JFXButton backButton;
    public Spinner<Integer> bpmSpinner;

    private Song song;
    private ObservableList<Measure> measureCollection = FXCollections.observableArrayList();


    public void initialize(Song song) {
        this.song = song;
        initializeBpmSpinner();
        initializeMeasureList();
    }

    private void initializeMeasureList() {
        Measure test1 = new Measure("Intro");
        measureCollection.add(test1);

        Measure test2 = new Measure("Chorus");
        measureCollection.add(test2);

        measureList.getItems().addAll(measureCollection);
        measureList.setCellFactory(new MeasureCellFactory());
        measureList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                try {
                switchToBeatEditor();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.consume();
            }
        });
        measureList.getSelectionModel().selectFirst();
        measureList.requestFocus();
    }

    private void switchToBeatEditor() throws IOException {
        Measure selectedMeasure = measureList.getSelectionModel().getSelectedItem();
        if (selectedMeasure != null) {
            Stage stage = (Stage) measureList.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/beatScreen.fxml"));
            Parent root = loader.load();
            BeatController controller = loader.getController();
            controller.initialize(song, selectedMeasure);
            stage.setScene(new Scene(root, 800, 400));
            stage.setTitle(
                    String.format(
                            "Full Steam Drum Machine - %s - %s",
                            song.toString(),
                            selectedMeasure.toString()
                    )
            );
            stage.show();
        }
    }

    private void initializeBpmSpinner() {
        bpmSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(20, 250));
        bpmSpinner.getValueFactory().setValue(75);
        bpmSpinner.setOnScroll(e -> {
            double delta = e.getDeltaY();

            if (delta < 0) {
                bpmSpinner.decrement();
            } else if (delta > 0) {
                bpmSpinner.increment();
            }
            e.consume();
        });
    }

    @FXML
    public void switchToSongSelection() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/songScreen.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("Full Steam Drum Machine");
        stage.show();
    }
}