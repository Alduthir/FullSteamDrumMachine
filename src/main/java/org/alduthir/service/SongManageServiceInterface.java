package org.alduthir.service;

import com.jfoenix.controls.JFXListView;
import org.alduthir.model.Song;

public interface SongManageServiceInterface {
    void initializeSongList(JFXListView<Song> songList);

    void addSong(JFXListView<Song> songList);

    void deleteSong(JFXListView<Song> songList);

    void playSelectedSong(JFXListView<Song> songList);
}
