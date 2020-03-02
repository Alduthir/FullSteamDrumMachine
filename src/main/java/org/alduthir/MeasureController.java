package org.alduthir;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import org.alduthir.Song.Song;
import org.alduthir.Song.SongCellFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MeasureController extends App {

    public Song song;

    @FXML
    public JFXButton editButton;
    public JFXButton playButton;
    public JFXListView<Object> measureList;
    public JFXButton addMeasureButton;
    public JFXButton reuseMeasureButton;
    public JFXButton deleteMeasureButton;
    public JFXButton backButton;
    public Spinner<Integer> bpmSpinner;

    public void loadSong(Song song)
    {
        this.song = song;
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
    public void switchToPrimary() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("songScreen.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("Full Steam Drum Machine");
        stage.show();
    }
}