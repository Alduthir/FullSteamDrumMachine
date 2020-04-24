package org.alduthir.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.alduthir.model.Instrument;
import org.alduthir.model.Measure;
import org.alduthir.util.NamedPreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class InstrumentRepository
 * <p>
 * A database repository class containing query functions for interacting with Instruments.
 */
public class InstrumentRepository extends DatabaseInteractionService<Instrument> {

    /**
     * Call the super constructor attempting to establish a database connection.
     *
     * @throws SQLException           If no connection could be established.
     * @throws ClassNotFoundException If the jdbc Driver could not be found.
     */
    public InstrumentRepository() throws SQLException, ClassNotFoundException {
        super();
    }

    /**
     * @inheritDoc
     */
    @Override
    public ObservableList<Instrument> fetchAll() throws SQLException {
        ObservableList<Instrument> instrumentCollection = FXCollections.observableArrayList();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Instrument");
        while (rs.next()) {
            Instrument instrument = new Instrument(
                    rs.getInt("instrumentId"),
                    rs.getString("name"),
                    rs.getInt("midiNumber")
            );
            instrumentCollection.add(instrument);
        }
        stmt.close();
        return instrumentCollection;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Instrument findById(int id) throws SQLException {
        String sql = "SELECT * FROM Instrument WHERE instrumentId = :instrumentId";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("instrumentId", id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Instrument instrument = new Instrument(
                    rs.getInt("instrumentId"),
                    rs.getString("name"),
                    rs.getInt("midiNumber")
            );
            stmt.close();
            return instrument;
        }
        rs.close();
        throw new SQLException(String.format("No instrument found with instrumentId %d.", id));
    }

    /**
     * @inheritDoc
     */
    @Override
    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM Instrument WHERE instrumentId = :instrumentId";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("instrumentId", id);
        stmt.executeUpdate();
        stmt.close();
    }

    /**
     * Insert a new Instrument into the database and update the given Instrument object with the newly inserted id.
     *
     * @param name       The name given for the new Instrument.
     * @param midiNumber the number corresponding to a key in the midiPlayer channel. (the sound to be played)
     * @return The newly inserted Instrument
     * @throws SQLException If the query throws an exception.
     */
    public Instrument createInstrument(String name, int midiNumber) throws SQLException {
        String sql = "INSERT INTO Instrument(name, midiNumber) VALUES(:name, :midiNumber)";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setString("name", name);
        stmt.setInt("midiNumber", midiNumber);
        stmt.executeUpdate(stmt.getQuery(), Statement.RETURN_GENERATED_KEYS);

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs != null && rs.next()) {
            return findById(rs.getInt(1));
        }

        throw new SQLException("Unable to create new instrument");
    }

    /**
     * Fetch all Instruments for a given Measure.
     *
     * @param measure The measure for which to fetch all linked Instruments.
     * @return An ObservableList of hydrated InstrumentObjects including their beat within the current measure.
     * @throws SQLException if the query throws an Exception.
     */
    public ObservableList<Instrument> fetchForMeasure(Measure measure) throws SQLException {
        ObservableList<Instrument> instrumentCollection = FXCollections.observableArrayList();

        String sql = "SELECT i.instrumentId, i.name, i.midiNumber, mi.beat FROM Instrument i JOIN MeasureInstrument mi ON i.instrumentId = mi.instrumentId WHERE mi.measureId = :measureId";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("measureId", measure.getId());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Instrument instrument = new Instrument(
                    rs.getInt("instrumentId"),
                    rs.getString("name"),
                    rs.getInt("midiNumber"),
                    rs.getString("beat")
            );
            instrumentCollection.add(instrument);
        }
        stmt.close();
        return instrumentCollection;
    }

    /**
     * Fetch a list of Instruments which are not currently included in the given Measure.
     *
     * @param measure The measure from which to exclude Instruments.
     * @return A hydrated list of Instruments.
     * @throws SQLException if the query throws an exception.
     */
    public ObservableList<Instrument> fetchReuseOptionCollection(Measure measure) throws SQLException {
        ObservableList<Instrument> instrumentCollection = FXCollections.observableArrayList();

        String sql = "SELECT * FROM Instrument i LEFT JOIN MeasureInstrument mi ON i.instrumentId = mi.instrumentId WHERE mi.measureId <> :measureId";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("measureId", measure.getId());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Instrument instrument = new Instrument(
                    rs.getInt("instrumentId"),
                    rs.getString("name"),
                    rs.getInt("midiNumber")
            );
            instrumentCollection.add(instrument);
        }
        stmt.close();
        return instrumentCollection;
    }

    /**
     * Add the given Instrument to the Measure.
     *
     * @param instrument The instrument to add to the Measure.
     * @param measure    The measure to which the instrument must be added.
     * @throws SQLException if the query throws an exception.
     */
    public void addToMeasure(Instrument instrument, Measure measure) throws SQLException {
        String sql = "INSERT INTO MeasureInstrument(measureId, instrumentId) VALUES(:measureId, :instrumentId)";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("measureId", measure.getId());
        stmt.setInt("instrumentId", instrument.getId());
        stmt.executeUpdate();
    }

    /**
     * Update the beat for the MeasureInstrument linking the given Measure and Instrument.
     *
     * @param measure     the Measure for which to update the beat.
     * @param instrument  the Instrument for which to update the beat.
     * @param encodedBeat A 16 character string containing 0's and 1's.
     * @throws SQLException if the query throws an exception
     */
    public void updateBeat(Measure measure, Instrument instrument, String encodedBeat) throws SQLException {
        String sql = "UPDATE MeasureInstrument SET beat = :beat WHERE measureId = :measureId AND instrumentId = :instrumentId";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setString("beat", encodedBeat);
        stmt.setInt("measureId", measure.getId());
        stmt.setInt("instrumentId", instrument.getId());
        stmt.executeUpdate();
    }

    /**
     * Remove the given Instrument from the Measure.
     *
     * @param measure    The Measure from which to remove the Instrument
     * @param instrument The instrument to be removed. It is not deleted, only decoupled from the measure.
     * @throws SQLException if the query throws an Exception.
     */
    public void removeFromMeasure(Measure measure, Instrument instrument) throws SQLException {
        String sql = "DELETE FROM MeasureInstrument WHERE measureId = :measureId AND instrumentId = :instrumentId";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("measureId", measure.getId());
        stmt.setInt("instrumentId", instrument.getId());
        stmt.executeUpdate();
        stmt.close();
    }
}
