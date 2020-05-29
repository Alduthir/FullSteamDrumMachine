package org.alduthir.repository;

public class DataPersistanceException extends Exception {
    public DataPersistanceException(String errorMessage) {
        super(errorMessage);
    }

    public DataPersistanceException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
