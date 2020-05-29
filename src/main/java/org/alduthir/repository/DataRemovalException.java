package org.alduthir.repository;

public class DataRemovalException extends Exception {
    public DataRemovalException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
