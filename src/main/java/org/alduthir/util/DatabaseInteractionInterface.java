package org.alduthir.util;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface DatabaseInteractionInterface<T> {
    ObservableList<T> fetchAll() throws SQLException, ClassNotFoundException;

    T findById(int id) throws SQLException, ClassNotFoundException;

    void deleteById(int id) throws SQLException, ClassNotFoundException;
}
