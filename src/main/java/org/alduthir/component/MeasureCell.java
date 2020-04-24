package org.alduthir.component;

import javafx.scene.control.ListCell;
import org.alduthir.model.SongMeasure;

public class MeasureCell extends ListCell<SongMeasure> {
    @Override
    public void updateItem(SongMeasure songMeasure, boolean empty) {
        super.updateItem(songMeasure, empty);

        String name = null;

        // Format name
        if (songMeasure != null) {
            name = songMeasure.getMeasure().toString();
        }
        this.setText(name);
        setGraphic(null);
    }
}
