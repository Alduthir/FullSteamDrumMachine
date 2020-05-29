package org.alduthir.repository;

import org.alduthir.model.Song;

import java.sql.SQLException;

public interface SongRepositoryInterface extends DatabaseInteractionInterface<Song> {
    void createSong(String songName) throws DataPersistanceException;

    void updateBpm(Song song, int bpmValue) throws DataPersistanceException;
}
