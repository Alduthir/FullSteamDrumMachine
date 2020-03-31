package org.alduthir.util;

import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import org.alduthir.App;

public class StyledTextInputDialog extends TextInputDialog {

    public StyledTextInputDialog() {
        var textInputDialog = new TextInputDialog();
        DialogPane dialogPane = textInputDialog.getDialogPane();
        dialogPane.getStylesheets().add(
                App.class.getResource("styles.css").toExternalForm());
        dialogPane.getStyleClass().add("fx-dialog");
    }
}
