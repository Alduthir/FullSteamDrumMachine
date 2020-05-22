package org.alduthir.service;

import org.alduthir.model.Measure;
import org.alduthir.model.Song;
import org.alduthir.model.SongMeasure;
import org.alduthir.repository.MeasureRepositoryInterface;
import org.alduthir.repository.SongRepositoryInterface;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class TestSongManageService {
    SongRepositoryInterface songRepositoryInterface = mock(SongRepositoryInterface.class);

    MeasureRepositoryInterface measureRepositoryInterface = mock(MeasureRepositoryInterface.class);

    MusicPlayerInterface musicPlayerInterface = mock(MusicPlayerInterface.class);

    @Test
    protected void testDeleteSong() throws SQLException {
        SongManageService songManageService = new SongManageService(
                measureRepositoryInterface,
                songRepositoryInterface,
                musicPlayerInterface
        );

        Song song = new Song(1, "TestSong", 75);
        Measure measure = new Measure("TestMeasure");

        List<SongMeasure> songMeasureCollection = new ArrayList<>();
        SongMeasure firstSongMeasure = new SongMeasure(1, song, measure);
        SongMeasure secondSongMeasure = new SongMeasure(2, song, measure);

        songMeasureCollection.add(firstSongMeasure);
        songMeasureCollection.add(secondSongMeasure);

        when(measureRepositoryInterface.fetchForSong(song)).thenReturn(songMeasureCollection);

        ArgumentCaptor<SongMeasure> argument = ArgumentCaptor.forClass(SongMeasure.class);

        verify(measureRepositoryInterface, times(2)).removeFromSong(argument.capture());
        verify(measureRepositoryInterface, times(2)).removeFromSong(argument.capture());

        verify(songRepositoryInterface, times(1)).deleteById(1);

        songManageService.deleteSong(song);

        List<SongMeasure> values = argument.getAllValues();
        assertTrue(values.contains(firstSongMeasure));
        assertTrue(values.contains(secondSongMeasure));
    }
}
