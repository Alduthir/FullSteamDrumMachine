package org.alduthir;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController extends App{

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}