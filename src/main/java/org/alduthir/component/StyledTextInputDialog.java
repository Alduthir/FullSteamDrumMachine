package org.alduthir.component;

import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import org.alduthir.App;

/**
 * A simple textInputDialog styled in accordance with the rest of the application
 */
public class StyledTextInputDialog extends TextInputDialog {

    /**
     * Create a TextInputDialog and add the fx-dialog CSS class to it.
     */
    public StyledTextInputDialog() {
        var textInputDialog = new TextInputDialog();
        DialogPane dialogPane = textInputDialog.getDialogPane();
        dialogPane.getStylesheets().add(
                App.class.getResource("styles.css").toExternalForm());
        dialogPane.getStyleClass().add("fx-dialog");
    }
}
