package org.alduthir.component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import org.alduthir.model.Instrument;
import org.alduthir.listener.InstrumentActionListener;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * Class InstrumentCell
 * <p>
 * The definition of a ListCell<Instrument> Contains a lot of logic for a ListCell because it is displayed as a custom
 * UI element containing multiple checkboxes and buttons. Whenever one of the checkboxes is changed, or a button is
 * clicked. An event is raised calling any Listeners to handle the new data.
 */
public class InstrumentCell extends ListCell<Instrument> {
    private HBox hbox = new HBox();
    private JFXButton deleteButton = new JFXButton();
    private Label label = new Label("");
    private HBox checkboxContainer = new HBox();
    private ObservableList<JFXCheckBox> checkBoxCollection = FXCollections.observableArrayList();
    private Instrument instrument;
    private List<InstrumentActionListener> listeners = new ArrayList<InstrumentActionListener>();

    /**
     * Create the ListCell element. And make sure it's UI elements are correctly created.
     */
    public InstrumentCell() {
        super();
        initDeleteButton();

        label.setStyle("-fx-label-padding: 10 0 0 0; -fx-min-width: 60; -fx-max-width: 60; -fx-text-overrun: ellipsis");

        HBox.setHgrow(checkboxContainer, Priority.ALWAYS);
        initCheckBoxContainer();

        hbox.getChildren().addAll(deleteButton, label, checkboxContainer);
        hbox.setSpacing(5);
    }

    /**
     * Add a listener for any actions triggered from the ListCell.
     *
     * @param listener An implementation of InstrumentActionListener
     */
    public void addListener(InstrumentActionListener listener) {
        listeners.add(listener);
    }

    /**
     * Create a list of JFXCheckBoxes for the cell.
     */
    private void initCheckBoxContainer() {
        for (int i = 0; i < 16; i++) {
            JFXCheckBox checkbox = new JFXCheckBox();
            checkbox.setOnMouseClicked(mouseEvent -> updateBeat());
            checkBoxCollection.add(checkbox);
        }
        checkboxContainer.getChildren().addAll(checkBoxCollection);
        checkboxContainer.setStyle("-fx-alignment: center-left; -fx-spacing:8;");
    }

    /**
     * Create and style a deleteButton for the cell.
     */
    private void initDeleteButton() {
        deleteButton.setStyle("-fx-background-color: red; -fx-padding: 10 0 10 0; -fx-fill-height: true");
        FontIcon fi = new FontIcon();
        fi.setIconLiteral("fa-ban");
        fi.setIconSize(18);
        fi.setIconColor(Paint.valueOf("#ff0000"));
        deleteButton.setGraphic(fi);
    }

    /**
     * Overrides basic ListCell functionality to ensure the correct graphics are set for the listCell containing
     * all the UI elements set up in this class.
     *
     * @param instrument The Instrument object the cell is made for.
     * @param empty      Whether or not the cell is empty.
     */
    @Override
    public void updateItem(Instrument instrument, boolean empty) {
        super.updateItem(instrument, empty);
        setPrefHeight(45);
        setText(null);
        if (instrument != null) {
            this.instrument = instrument;
            initializeBeat();
            String name = instrument.toString();
            label.setText(name);
            label.setTooltip(new Tooltip(name));
            setGraphic(hbox);

            deleteButton.setOnMouseClicked(mouseEvent -> {
                for (InstrumentActionListener listener : listeners) {
                    listener.removeAction(instrument);
                }
            });
        } else {
            setGraphic(null);
        }
    }

    /**
     * Checks checkboxes based on the encoded Beat String. A character of '0' is unchecked, while '1' is checked.
     */
    private void initializeBeat() {
        int index = 0;
        String beat = instrument.getBeat();
        for (JFXCheckBox checkBox : checkBoxCollection) {
            checkBox.setSelected(beat.charAt(index) == '1');
            index++;
        }
    }

    /**
     * Call every listener to update the beat for the given instrument using the encoded string of checkboxes.
     */
    public void updateBeat() {
        StringBuilder beat = new StringBuilder();
        for (JFXCheckBox checkbox : checkBoxCollection) {
            int isChecked = checkbox.isSelected() ? 1 : 0;
            beat.append(isChecked);
        }

        for (InstrumentActionListener listener : listeners) {
            listener.updateAction(beat.toString(), instrument);
        }
    }
}
