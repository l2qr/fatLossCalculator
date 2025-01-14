module skwira.marcin.fatlosscalculator {
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.sun.jna;
    requires static lombok;
    requires com.sun.jna.platform;
    requires org.jetbrains.annotations;

    opens skwira.marcin.fatlosscalculator to javafx.fxml;
    exports skwira.marcin.fatlosscalculator;
}
