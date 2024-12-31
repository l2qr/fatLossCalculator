module skwira.marcin.fatlosscalculator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens skwira.marcin.fatlosscalculator to javafx.fxml;
    exports skwira.marcin.fatlosscalculator;
}