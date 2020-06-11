package org.alduthir.repository;

import org.alduthir.model.Song;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestSongRepository {
    DataSource dataSource = mock(DataSource.class);
    Connection dbConnection = mock(Connection.class);
    ResultSet resultSet = mock(ResultSet.class);
    Statement statement = mock(Statement.class);

    SongRepository songRepository;

    @Test
    public void testFetchAll() throws DataRetrievalException, SQLException {
        int songId = 1;
        String songName = "test";
        int bpm = 75;

        songRepository = new SongRepository(dataSource);

        when(dataSource.getConnection()).thenReturn(dbConnection);
        when(dbConnection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(Mockito.any())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);

        when(resultSet.getInt("songId")).thenReturn(songId);
        when(resultSet.getString("name")).thenReturn(songName);
        when(resultSet.getInt("bpm")).thenReturn(bpm);

        List<Song> songList = songRepository.fetchAll();
        assertEquals(1, songList.size());
        Song song = songList.get(0);

        assertEquals(songId, song.getId());
        assertEquals(songName, song.getName());
        assertEquals(bpm, song.getBpm());
    }

    @Test
    public void testFetchAllWithSqlException() throws SQLException {
        songRepository = new SongRepository(dataSource);

        when(dataSource.getConnection()).thenReturn(dbConnection);
        when(dbConnection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(Mockito.any())).thenThrow(SQLException.class);

        assertThrows(DataRetrievalException.class, () -> {
            songRepository.fetchAll();
        });
    }
}
