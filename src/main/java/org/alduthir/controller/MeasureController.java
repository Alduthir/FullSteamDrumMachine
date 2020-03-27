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
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.alduthir.App;
import org.alduthir.measure.Measure;
import org.alduthir.measure.MeasureCellFactory;
import org.alduthir.measure.MeasureRepository;
import org.alduthir.song.Song;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

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
    private MeasureRepository repository;

    public void initialize(Song song) {
        this.song = song;

        if (repository == null) {
            try {
                repository = new MeasureRepository();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            initializeMeasureList();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        initializeBpmSpinner();
    }

    public void switchToBeatEditor() throws IOException {
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
    public void switchToSongSelection() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/songScreen.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("Full Steam Drum Machine");
        stage.show();
    }

    public void playMeasure() {
        notYetImplemented();
    }

    public void addMeasure() throws SQLException {
        TextInputDialog textInputDialog = new TextInputDialog();
        styleDialog(textInputDialog);

        textInputDialog.setTitle("Create new Measure");
        textInputDialog.setContentText("Enter the name of your new measure.");

        Optional<String> result = textInputDialog.showAndWait();
        if (result.isPresent()) {
            Measure measure = new Measure(result.get());
            repository.createMeasure(measure);
            repository.addToSong(measure, song, measureCollection.size());
            initializeMeasureList();
        }
    }

    public void deleteMeasure() throws SQLException {
        Measure selectedMeasure = measureList.getSelectionModel().getSelectedItem();
        if (selectedMeasure != null) {
            repository.removeFromSong(selectedMeasure, song);
            initializeMeasureList();
        }
    }

    public void reuseMeasure() {
        notYetImplemented();
    }

    public void saveSequence() {
        notYetImplemented();
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

    private void initializeMeasureList() throws SQLException {
        measureCollection = repository.fetchForSong(song);
        measureList.getItems().setAll(measureCollection);

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
}