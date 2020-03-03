package org.alduthir.Measure;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.alduthir.Song.Song;
import org.alduthir.Song.SongCell;

public class MeasureCellFactory implements Callback<ListView<Measure>, ListCell<Measure>>
{
    @Override
    public ListCell<Measure> call(ListView<Measure> measureListView) {
        return new MeasureCell();
    }
}