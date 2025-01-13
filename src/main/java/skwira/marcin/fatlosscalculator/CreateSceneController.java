package skwira.marcin.fatlosscalculator;

import javafx.fxml.FXML;

public class CreateSceneController {

    @FXML
    private CreateFormController createFormController;

    @FXML
    private MenuController menuController;

    @FXML
    public void initialize() {
        menuController.initialize(Lookups.SceneType.CREATE);
        menuController.setCreateFormController(createFormController);
    }

    public CreateFormController getCreateFormController() {
        return createFormController;
    }

}
