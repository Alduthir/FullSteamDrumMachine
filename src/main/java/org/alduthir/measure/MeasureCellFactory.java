package org.alduthir.measure;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class MeasureCellFactory implements Callback<ListView<SongMeasure>, ListCell<SongMeasure>>
{
    @Override
    public ListCell<SongMeasure> call(ListView<SongMeasure> measureListView) {
        return new MeasureCell();
    }
}