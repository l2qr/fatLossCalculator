package skwira.marcin.fatlosscalculator;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CreateSceneController {

    @FXML
    private CreateFormController createFormController;

    @FXML
    private MenuController menuController;

    @FXML
    public void initialize() {
        menuController.initialize(Lookups.MenuScene.CREATE, createFormController);
    }


}
