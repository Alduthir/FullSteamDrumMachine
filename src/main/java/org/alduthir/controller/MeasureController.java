package org.alduthir.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;
import org.alduthir.App;
import org.alduthir.measure.*;
import org.alduthir.song.Song;

import java.io.IOException;

public class MeasureController extends App {
    private final MeasureManageService measureManageService;
    private final BpmSpinnerService bpmSpinnerService;

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

    public MeasureController() {
        this.measureManageService = new MeasureManageService();
        this.bpmSpinnerService = new BpmSpinnerService();
    }

    public void initialize(Song song) {
        this.song = song;
        measureManageService.initializeMeasureList(song, measureList);
        bpmSpinnerService.initializeBpmSpinner(song, bpmSpinner);

        measureList.setCellFactory(new MeasureCellFactory());
        measureList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                try {
                    redirectToBeatEditor();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.consume();
            }
        });
        measureList.getSelectionModel().selectFirst();
        measureList.requestFocus();
    }

    public void redirectToBeatEditor() throws IOException {
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

    @FXML
    public void redirectToSongSelection() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/songScreen.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("Full Steam Drum Machine");
        stage.show();
    }

    public void playAction() {
        measureManageService.playSelectedMeasure(song, measureList);
    }

    public void addAction() {
        measureManageService.addMeasure(song, measureList);
    }

    public void deleteAction() {
        measureManageService.deleteMeasure(song, measureList);
    }

    public void reuseAction() {
        notYetImplemented();
    }

    public void saveSequence() {
        notYetImplemented();
    }
}