package org.alduthir.service;

import org.alduthir.model.Song;

import java.util.List;

public interface SongManageServiceInterface {
    List<Song> getSongCollection();

    void createSong(String songName);

    void deleteSong(Song song);

    Boolean playSong(Song song);

    void updateBpm(Song song, int bpmValue);
}

