package skwira.marcin.fatlosscalculator;

import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    static DatabaseController dbController;
    ScenesController sc = ScenesController.getInstance();

    @Override
    public void start(Stage stage) {
        sc.setStage(stage);
        sc.switchToListScene();
        FXWinUtil.setDarkMode(stage, true);
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