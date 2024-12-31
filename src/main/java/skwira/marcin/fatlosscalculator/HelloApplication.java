package skwira.marcin.fatlosscalculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    static DatabaseController dbController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("listScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 450);
        try {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Fat Loss Calculator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try {
            dbController = new DatabaseController("fatloss.sqlite");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        launch();
    }
}