package skwira.marcin.fatlosscalculator;

public class FatLossCalculatorApp {

    /*
    * To build an installer using jpackage:
    * 1) build the artifact FatLossCalculator.jar
    * 2) run command from the location of the jar artifact
    * jpackage --type exe --input . --dest . --main-jar FatLossCalculator.jar --main-class skwira.marcin.fatlosscalculator.FatLossCalculatorApp --module-path "C:\Program Files\Java\javafx-jmods-21.0.5" --module-path "C:\Users\skwir\.m2\repository\net\java\dev\jna\jna-jpms\5.16.0\jna-jpms-5.16.0.jar" --module-path "C:\Users\skwir\.m2\repository\net\java\dev\jna\jna-platform-jpms\5.16.0\jna-platform-jpms-5.16.0.jar" --module-path "C:\Users\skwir\.m2\repository\org\projectlombok\lombok\1.18.36\lombok-1.18.36.jar" --module-path "C:\Users\skwir\.m2\repository\org\xerial\sqlite-jdbc\3.47.1.0\sqlite-jdbc-3.47.1.0.jar" --add-modules javafx.controls,javafx.fxml,javafx.graphics,com.sun.jna,com.sun.jna.platform,org.xerial.sqlitejdbc --win-shortcut --win-menu --java-options "--add-opens javafx.graphics/javafx.stage=ALL-UNNAMED" --java-options "--add-opens javafx.graphics/com.sun.javafx.tk.quantum=ALL-UNNAMED" --name "Fat Loss Calculator" --app-version "1.1"
    * */
    public static void main(String[] args) {
        App.main(args);
    }

}
