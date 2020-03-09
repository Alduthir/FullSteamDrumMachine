package org.alduthir.Measure;

import javafx.scene.control.ListCell;

public class MeasureCell extends ListCell<Measure> {
    @Override
    public void updateItem(Measure measure, boolean empty) {
        super.updateItem(measure, empty);

        String name = null;

        // Format name
        if (measure != null) {
            name = measure.toString();
        }
        this.setText(name);
        setGraphic(null);
    }
}
