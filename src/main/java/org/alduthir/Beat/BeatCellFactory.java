package org.alduthir.Beat;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.alduthir.Measure.Measure;
import org.alduthir.Measure.MeasureCell;

public class BeatCellFactory implements Callback<ListView<Beat>, ListCell<Beat>>
{
    @Override
    public ListCell<Beat> call(ListView<Beat> beatListView) {
        return new BeatCell();
    }
}