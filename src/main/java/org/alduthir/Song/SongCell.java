package org.alduthir.Song;

import javafx.scene.control.ListCell;

public class SongCell extends ListCell<Song> {
    @Override
    public void updateItem(Song song, boolean empty) {
        super.updateItem(song, empty);

        String name = null;

        // Format name
        if (song != null) {
            name = song.toString();
        }
        
        this.setText(name);
        setGraphic(null);
    }
}
