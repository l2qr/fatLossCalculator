package skwira.marcin.fatlosscalculator;

import javafx.application.Application;
import javafx.stage.Stage;


/* TODO add the goal tracking functionality
*   graph of the weight to target vs current weight based on measurements
*   "add weight" dialog/screen,
*   "add calories consumed" dialog screen
*   calculation of the current BMR from the records: weight gain/loss vs calories consumed vs estimated BMR
* */


public class App extends Application {

    static DatabaseController dbController;
    ScenesController sc = ScenesController.getInstance();

    @Override
    public void start(Stage stage) {
        sc.setStage(stage);
        sc.switchScene(Lookups.SceneType.LIST);
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