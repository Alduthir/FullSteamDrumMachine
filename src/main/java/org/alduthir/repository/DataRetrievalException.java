package org.alduthir.repository;

public class DataRetrievalException extends Exception {
    public DataRetrievalException(String errorMessage) {
        super(errorMessage);
    }

    public DataRetrievalException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
