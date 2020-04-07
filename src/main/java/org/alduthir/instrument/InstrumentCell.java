package org.alduthir.instrument;

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
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;

public class InstrumentCell extends ListCell<Instrument> {
    private HBox hbox = new HBox();
    private JFXButton deleteButton = new JFXButton();
    private Label label = new Label("");
    private HBox checkboxContainer = new HBox();
    private ObservableList<JFXCheckBox> checkBoxCollection = FXCollections.observableArrayList();
    private Instrument instrument;
    private List<InstrumentActionListener> listeners = new ArrayList<InstrumentActionListener>();

    public InstrumentCell() {
        super();
        initDeleteButton();

        label.setStyle("-fx-label-padding: 10 0 0 0; -fx-min-width: 60; -fx-max-width: 60; -fx-text-overrun: ellipsis");

        HBox.setHgrow(checkboxContainer, Priority.ALWAYS);
        initCheckBoxContainer();

        hbox.getChildren().addAll(deleteButton, label, checkboxContainer);
        hbox.setSpacing(5);
    }

    public void addListener(InstrumentActionListener listener) {
        listeners.add(listener);
    }

    private void initCheckBoxContainer() {
        for (int i = 0; i < 16; i++) {
            JFXCheckBox checkbox = new JFXCheckBox();
            checkbox.setOnMouseClicked(mouseEvent -> updateBeat());
            checkBoxCollection.add(checkbox);
        }
        checkboxContainer.getChildren().addAll(checkBoxCollection);
        checkboxContainer.setStyle("-fx-alignment: center-left; -fx-spacing:8;");
    }

    private void initDeleteButton() {
        deleteButton.setStyle("-fx-background-color: red; -fx-padding: 10 0 10 0; -fx-fill-height: true");
        FontIcon fi = new FontIcon();
        fi.setIconLiteral("fa-ban");
        fi.setIconSize(18);
        fi.setIconColor(Paint.valueOf("#ff0000"));
        deleteButton.setGraphic(fi);
    }

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

    private void initializeBeat() {
        int index = 0;
        String beat = instrument.getBeat();
        for(JFXCheckBox checkBox: checkBoxCollection){
            checkBox.setSelected(beat.charAt(index) == '1');
            index++;
        }
    }

    public void updateBeat()
    {
        StringBuilder beat = new StringBuilder();
        for(JFXCheckBox checkbox : checkBoxCollection){
            int isChecked = checkbox.isSelected() ? 1 : 0;
            beat.append(isChecked);
        }

        for (InstrumentActionListener listener : listeners) {
            listener.updateAction(beat.toString(), instrument);
        }
    }
}
