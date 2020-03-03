module org.alduthir {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires org.kordamp.iconli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome;
    requires javafx.fxml;
    requires com.jfoenix;

    exports org.alduthir;
    exports org.alduthir.Song;
    exports org.alduthir.Measure;
    exports org.alduthir.Beat;

    opens org.alduthir to javafx.graphics;
    opens org.alduthir.Song to javafx.graphics;
    opens org.alduthir.Measure to javafx.graphics;
    opens org.alduthir.Beat to javafx.graphics;
}