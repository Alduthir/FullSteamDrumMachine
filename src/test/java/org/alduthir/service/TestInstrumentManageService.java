package org.alduthir.service;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import org.alduthir.controller.AddInstrumentOption;
import org.alduthir.model.Instrument;
import org.alduthir.model.Measure;
import org.alduthir.repository.InstrumentRepositoryInterface;
import org.alduthir.util.NoSelectionModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class TestInstrumentManageService {
    InstrumentRepositoryInterface instrumentRepositoryInterface = mock(InstrumentRepositoryInterface.class);

    MusicPlayerInterface musicPlayerInterface = mock(MusicPlayerInterface.class);

    InstrumentManageServiceInterface spyService = spy(InstrumentManageServiceInterface.class);

    /**
     * Tests that the ListView UI component is filled with the items fetched through the repository.
     * And that its selectionModel is set to an instance of NoSelectionModel.
     */
    @Test
    protected void testInitializeInstrumentCollection() {
        try {
            InstrumentManageService instrumentManageService = new InstrumentManageService(
                    instrumentRepositoryInterface,
                    musicPlayerInterface
            );

            Measure measure = new Measure("TestMeasure");
            JFXListView<Instrument> instrumentList = new JFXListView<>();

            Instrument hiHat = new Instrument(1, "Hihat", 35, "1010101010101011");
            Instrument bass = new Instrument(1, "Bass", 42, "1010101010101010");
            ObservableList<Instrument> instrumentCollection = FXCollections.observableArrayList();
            instrumentCollection.add(hiHat);
            instrumentCollection.add(bass);

            when(instrumentRepositoryInterface.fetchForMeasure(measure)).thenReturn(instrumentCollection);

            instrumentManageService.initializeInstrumentCollection(measure, instrumentList);

            assertEquals(2, instrumentList.getItems().size());
            assertEquals(hiHat, instrumentList.getItems().get(0));
            assertEquals(bass, instrumentList.getItems().get(1));
            assertTrue(instrumentList.getSelectionModel() instanceof NoSelectionModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test that the combobox is filled with the correct options. And that the corresponding VBoxes are shown/hidden
     * correctly based on the initially selected option.
     */
    @Test
    protected void testInitializeNewOrReuseComboBox() {
        InstrumentManageService instrumentManageService = new InstrumentManageService(
                instrumentRepositoryInterface,
                musicPlayerInterface
        );

        JFXComboBox<AddInstrumentOption> newOrReuseComboBox = new JFXComboBox<>();
        VBox reuseBox = new VBox();
        VBox newBox = new VBox();

        instrumentManageService.initiallizeNewOrReuseComboBox(newOrReuseComboBox, reuseBox, newBox);

        assertEquals(2, newOrReuseComboBox.getItems().size());
        assertEquals(AddInstrumentOption.NEW, newOrReuseComboBox.getItems().get(0));
        assertEquals(AddInstrumentOption.REUSE, newOrReuseComboBox.getItems().get(1));
        assertEquals(AddInstrumentOption.NEW, newOrReuseComboBox.getSelectionModel().getSelectedItem());
        assertFalse(reuseBox.isVisible());
        assertTrue(newBox.isVisible());
    }

//    @Test
//    protected void testRemoveInstrument() {
//        try {
//            InstrumentManageService instrumentManageService = new InstrumentManageService(
//                    instrumentRepositoryInterface,
//                    musicPlayerInterface
//            );
//
//            Measure measure = new Measure("TestMeasure");
//            JFXListView<Instrument> instrumentList = new JFXListView<Instrument>();
//
//            Instrument hiHat = new Instrument(1, "Hihat", 35, "1010101010101011");
//            Instrument bass = new Instrument(1, "Bass", 42, "1010101010101010");
//            ObservableList<Instrument> instrumentCollection = FXCollections.observableArrayList();
//            instrumentCollection.add(hiHat);
//            instrumentCollection.add(bass);
//
//            // Set the instrumentCollection to the listview.
//            instrumentList.setItems(instrumentCollection);
//            // Then remove the hihat because this is the stub response we will return in fetchForMeasure.
//            instrumentCollection.remove(hiHat);
//
//            // Verify the remove function is called exactly once, with the measure and hihat as parameters.
//            verify(instrumentRepositoryInterface, times(1)).removeFromMeasure(measure, hiHat);
//            verify(spyService, times(1)).initializeInstrumentCollection(measure, instrumentList);
//            when(instrumentRepositoryInterface.fetchForMeasure(measure)).thenReturn(instrumentCollection);
//
//            instrumentManageService.removeInstrument(measure, hiHat, instrumentList);
//
//            // The hiHat has been removed and initialize has been called, stubbing to a list without hiHat in it.
//            assertEquals(1, instrumentList.getItems().size());
//            assertEquals(bass, instrumentList.getItems().get(0));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    protected void testSaveNewInstrument() {
//        try {
//            InstrumentManageService instrumentManageService = new InstrumentManageService(
//                    instrumentRepositoryInterface,
//                    musicPlayerInterface
//            );
//
//            Measure measure = new Measure("TestMeasure");
//
//            String instrumentName = "TestInstrument";
//            int midiNumber = 35;
//
//            Instrument instrument = new Instrument(1, instrumentName, midiNumber);
//
//            when(instrumentRepositoryInterface.createInstrument(instrumentName, midiNumber)).thenReturn(instrument);
//
//            instrumentManageService.saveNewInstrument(instrumentName, midiNumber, measure);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
