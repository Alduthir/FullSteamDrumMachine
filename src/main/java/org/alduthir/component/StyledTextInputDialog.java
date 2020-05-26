package org.alduthir.component;

import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;

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
        dialogPane.getStyleClass().add("fx-dialog");
    }
}
