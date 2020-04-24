package org.alduthir.component;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import org.alduthir.model.Song;
import org.alduthir.repository.SongRepository;

import java.sql.SQLException;

public class BpmSpinner {
    public static final int MIN_BPM = 20;
    public static final int MAX_BPM = 250;

    private SongRepository repository;

    /**
     * The constructor retrieves the repository. Exceptions may occur while creating the repository if no connection
     * can be established.
     */
    public BpmSpinner() {
        try {
            repository = new SongRepository();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the spinner with a range of possible values. Also binds an onScrollEvent allowing incrementing of the
     * value. Whenever the scrollEvent is triggered the repository is called to update the BPM value in the database.
     *
     * @param song       required to set the initial value of the spinner and to know what to bind the bpm to.
     * @param bpmSpinner the UI element to be initialized.
     */
    public void initializeBpmSpinner(Song song, Spinner<Integer> bpmSpinner) {
        bpmSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_BPM, MAX_BPM));
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
