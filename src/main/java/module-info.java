module org.alduthir {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires org.kordamp.iconli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome;
    requires javafx.fxml;
    requires com.jfoenix;
    requires java.naming;
    requires java.sql;
    requires java.desktop;

    exports org.alduthir;
    exports org.alduthir.controller;
    exports org.alduthir.song;
    exports org.alduthir.measure;
    exports org.alduthir.instrument;
    exports org.alduthir.util;

    opens org.alduthir to javafx.fxml;
    opens org.alduthir.controller to javafx.graphics;
    opens org.alduthir.song to javafx.graphics;
    opens org.alduthir.measure to javafx.graphics;
    opens org.alduthir.instrument to javafx.graphics;
}