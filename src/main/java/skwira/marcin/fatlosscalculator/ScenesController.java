package skwira.marcin.fatlosscalculator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
public final class ScenesController {

    private final static ScenesController INSTANCE = new ScenesController();
    @Setter
    private Lookups.SceneType sceneType;
    private Stage stage;
    @Setter
    private Entry entry;
    /* TODO make app remember navigation history.
        * Change back button behaviour to go back to previous page
        * Utilise the home button to go to the list scene */

    private ScenesController() {}

    public static ScenesController getInstance() {
        return INSTANCE;
    }

    private Object switchScene(Lookups.SceneType sceneType) {
        this.sceneType = sceneType;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(sceneType.toString()));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.stage.setScene(scene);
        this.stage.show();
        setSceneType(sceneType);
        return fxmlLoader.getController();
    }

    public Object switchToCreateScene() {
        return switchScene(Lookups.SceneType.CREATE);
    }
    public Object switchToCreateScene(Entry e) {
        CreateSceneController ctrl = (CreateSceneController) switchScene(Lookups.SceneType.CREATE);
        ctrl.getCreateFormController().setValues(e);
        return ctrl;
    }

    public Object switchToListScene() {
        return switchScene(Lookups.SceneType.LIST);
    }

    public Object switchToEditScene(Entry e) {
        CreateSceneController ctrl = (CreateSceneController) switchScene(Lookups.SceneType.EDIT);
        ctrl.getCreateFormController().setValues(e);
        return ctrl;
    }

    public Object switchToDetailsScene(Entry e) {
        DetailsSceneController ctrl = (DetailsSceneController) switchScene(Lookups.SceneType.DETAILS);
        ctrl.loadDetails(e);
        return ctrl;
    }

    public void setStage(Stage s) {
        this.stage = s;
        this.stage.setTitle("Fat Loss Calculator");
    }
}
