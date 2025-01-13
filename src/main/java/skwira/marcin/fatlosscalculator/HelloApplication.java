package skwira.marcin.fatlosscalculator;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    static DatabaseController dbController;
    ScenesController sc = ScenesController.getInstance();

    @Override
    public void start(Stage stage) throws IOException {
        sc.setStage(stage);
        sc.switchScene(Lookups.SceneType.LIST);
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