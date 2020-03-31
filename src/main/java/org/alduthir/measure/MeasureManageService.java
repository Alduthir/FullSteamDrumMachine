package org.alduthir.measure;

import com.jfoenix.controls.JFXListView;
import javafx.scene.control.TextInputDialog;
import org.alduthir.song.Song;
import org.alduthir.util.StyledTextInputDialog;

import java.sql.SQLException;
import java.util.Optional;

public class MeasureManageService {
    private MeasureRepository repository;

    public MeasureManageService() {
        try {
            repository = new MeasureRepository();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initializeMeasureList(Song song, JFXListView<Measure> measureList) throws SQLException {
        measureList.getItems().setAll(repository.fetchForSong(song));
    }

    public void deleteMeasure(Song song, JFXListView<Measure> measureList) throws SQLException {
        Measure selectedMeasure = measureList.getSelectionModel().getSelectedItem();
        if (selectedMeasure != null) {
            repository.removeFromSong(selectedMeasure, song);
            initializeMeasureList(song, measureList);
        }
    }

    public void addMeasure(Song song, JFXListView<Measure> measureList) throws SQLException {
        TextInputDialog textInputDialog = new StyledTextInputDialog();
        textInputDialog.setTitle("Create new Measure");
        textInputDialog.setContentText("Enter the name of your new measure.");

        Optional<String> result = textInputDialog.showAndWait();
        if (result.isPresent()) {
            Measure measure = new Measure(result.get());
            repository.createMeasure(measure);
            repository.addToSong(measure, song, measureList.getItems().size());
            initializeMeasureList(song, measureList);
        }
    }
}
