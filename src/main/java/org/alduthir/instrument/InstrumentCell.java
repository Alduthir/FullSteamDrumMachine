package org.alduthir.instrument;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import org.alduthir.App;
import org.alduthir.controller.BeatController;
import org.alduthir.measure.Measure;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.sql.SQLException;

public class InstrumentCell extends ListCell<Instrument> {
    private HBox hbox = new HBox();
    private JFXButton deleteButton = new JFXButton();
    private Label label = new Label("");
    private HBox checkboxContainer = new HBox();
    private ObservableList<JFXCheckBox> checkBoxCollection = FXCollections.observableArrayList();
    private Instrument instrument;
    private Measure measure;

    public InstrumentCell(Measure measure) {
        super();
        this.measure = measure;
        initDeleteButton();

        label.setStyle("-fx-label-padding: 10 0 0 0; -fx-min-width: 70; -fx-max-width: 70; -fx-text-overrun: ellipsis");

        HBox.setHgrow(checkboxContainer, Priority.ALWAYS);
        initCheckBoxContainer();

        hbox.getChildren().addAll(deleteButton, label, checkboxContainer);
        hbox.setSpacing(5);
    }

    private void initCheckBoxContainer() {
        for (int i = 0; i < 14; i++) {
            JFXCheckBox checkbox = new JFXCheckBox();
            checkBoxCollection.add(checkbox);
        }
        checkboxContainer.getChildren().addAll(checkBoxCollection);
        checkboxContainer.setStyle("-fx-alignment: center-left; -fx-spacing:10;");
    }

    private void initDeleteButton() {
        deleteButton.setStyle("-fx-background-color: red; -fx-padding: 10 10 10 10; -fx-fill-height: true");
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
            String name = instrument.toString();
            label.setText(name);
            label.setTooltip(new Tooltip(name));
            setGraphic(hbox);
            addClickHandlers();
        } else {
            setGraphic(null);
        }
    }

    private void addClickHandlers() {
        deleteButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/beatScreen.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            BeatController controller = loader.getController();

            try {
                controller.removeInstrument(measure, instrument);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
