package org.alduthir.component.factory;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.alduthir.component.MeasureCell;
import org.alduthir.model.SongMeasure;

public class MeasureCellFactory implements Callback<ListView<SongMeasure>, ListCell<SongMeasure>>
{
    @Override
    public ListCell<SongMeasure> call(ListView<SongMeasure> measureListView) {
        return new MeasureCell();
    }
}