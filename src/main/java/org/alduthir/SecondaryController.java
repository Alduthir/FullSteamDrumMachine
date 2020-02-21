package org.alduthir;

import javafx.fxml.FXML;

import java.io.IOException;

public class SecondaryController extends App{

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}