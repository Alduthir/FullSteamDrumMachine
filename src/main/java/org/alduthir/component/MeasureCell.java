package org.alduthir.component;

import javafx.scene.control.ListCell;
import org.alduthir.model.SongMeasure;

/**
 * Class MeasureCell
 *
 * A viewcomponent used to show a SongMeasure in a Listview.
 * Any listItem for such a cell displays the name of the Measure.
 */
public class MeasureCell extends ListCell<SongMeasure> {
    /**
     * @inheritDoc
     */
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
