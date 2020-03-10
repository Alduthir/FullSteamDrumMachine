package org.alduthir.util;

import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseInteractionInterface<T> {
    public ObservableList<T> fetchAll() throws SQLException, ClassNotFoundException;

    public T findById(int id) throws SQLException, ClassNotFoundException;

    public void deleteById(int id) throws SQLException, ClassNotFoundException;
}
