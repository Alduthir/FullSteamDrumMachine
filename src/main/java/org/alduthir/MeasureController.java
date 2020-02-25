package org.alduthir;

import javafx.fxml.FXML;

import java.io.IOException;

public class MeasureController extends App{

    @FXML
    public void switchToPrimary() throws IOException {
        App.setRoot("songScreen");
    }
}