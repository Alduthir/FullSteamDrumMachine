package org.alduthir.instrument;

import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.alduthir.measure.Measure;
import org.alduthir.util.AbstractDatabaseInteractionService;
import org.alduthir.util.NamedPreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InstrumentRepository extends AbstractDatabaseInteractionService<Instrument> {

    public InstrumentRepository() throws SQLException, ClassNotFoundException {
    }

    @Override
    public ObservableList<Instrument> fetchAll() throws SQLException {
        ObservableList<Instrument> instrumentCollection = FXCollections.observableArrayList();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Instrument");
        while (rs.next()) {
            Instrument instrument = new Instrument(
                    rs.getInt("instrumentId"),
                    rs.getString("name"),
                    rs.getString("midiPath")
            );
            instrumentCollection.add(instrument);
        }
        stmt.close();
        return instrumentCollection;
    }

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
                    rs.getString("midiPath")
            );
            stmt.close();
            return instrument;
        }
        rs.close();
        throw new SQLException(String.format("No instrument found with instrumentId %d.", id));
    }

    @Override
    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM Instrument WHERE instrumentId = :instrumentId";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("instrumentId", id);
        stmt.executeUpdate();
        stmt.close();
    }

    public void createInstrument(Instrument instrument) throws SQLException {
        String sql = "INSERT INTO Instrument(name, midiPath) VALUES(:name, :midiPath)";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setString("name", instrument.getName());
        stmt.setString("midiPath", instrument.getMidiPath());
        stmt.executeUpdate(stmt.getQuery(), Statement.RETURN_GENERATED_KEYS);

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs != null && rs.next()) {
            instrument.setId(rs.getInt(1));
        }
    }

    public ObservableList<Instrument> fetchForMeasure(Measure measure) throws SQLException {
        ObservableList<Instrument> instrumentCollection = FXCollections.observableArrayList();

        String sql = "SELECT * FROM Instrument i JOIN MeasureInstrument mi ON i.instrumentId = mi.instrumentId WHERE mi.measureId = :measureId";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("measureId", measure.getId());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Instrument instrument = new Instrument(
                    rs.getInt("instrumentId"),
                    rs.getString("name"),
                    rs.getString("midiPath")
            );
            instrumentCollection.add(instrument);
        }
        stmt.close();
        return instrumentCollection;
    }

    public void addToMeasure(Instrument instrument, Measure measure) throws SQLException {
        String sql = "INSERT INTO MeasureInstrument(measureId, instrumentId) VALUES(:measureId, :instrumentId)";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("measureId", measure.getId());
        stmt.setInt("instrumentId", instrument.getId());
        stmt.executeUpdate();
    }

    public void updateBeat(Instrument instrument, String encodedBeat) {
    }

    public void removeFromMeasure(Measure measure, Instrument instrument) throws SQLException {
        String sql = "DELETE FROM MeasureInstrument WHERE measureId = :measureId AND instrumentId = :instrumentId";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("measureId", measure.getId());
        stmt.setInt("instrumentId", instrument.getId());
        stmt.executeUpdate();
        stmt.close();
    }
}
