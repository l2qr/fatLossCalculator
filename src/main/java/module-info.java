module skwira.marcin.fatlosscalculator {
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires java.sql;
    requires com.sun.jna;
    requires static lombok;
    requires com.sun.jna.platform;
    requires org.xerial.sqlitejdbc;

    opens skwira.marcin.fatlosscalculator to javafx.fxml;
    exports skwira.marcin.fatlosscalculator;
}
