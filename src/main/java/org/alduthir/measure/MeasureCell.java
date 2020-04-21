package org.alduthir.measure;

import javafx.scene.control.ListCell;

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
