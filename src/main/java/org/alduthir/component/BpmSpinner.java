package org.alduthir.component;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import org.alduthir.model.Song;
import org.alduthir.repository.SongRepository;

import java.sql.SQLException;

public class BpmSpinner {
    private SongRepository repository;

    public BpmSpinner() {
        try {
            repository = new SongRepository();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initializeBpmSpinner( Song song, Spinner<Integer> bpmSpinner) {
        bpmSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(20, 250));
        bpmSpinner.getValueFactory().setValue(song.getBpm());
        bpmSpinner.setOnScroll(e -> {
            double delta = e.getDeltaY();

            if (delta < 0) {
                bpmSpinner.decrement();
            } else if (delta > 0) {
                bpmSpinner.increment();
            }

            song.setBpm(bpmSpinner.getValue());
            try {
                repository.updateBpm(song);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.consume();
        });
    }
}
