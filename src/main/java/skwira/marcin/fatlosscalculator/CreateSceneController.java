package skwira.marcin.fatlosscalculator;

import javafx.fxml.FXML;
import lombok.Getter;

public class CreateSceneController {

    @Getter
    @FXML
    private CreateFormController createFormController;

    @FXML
    private MenuController menuController;

    @FXML
    public void initialize() {
        menuController.setCreateFormController(createFormController);
    }

}
