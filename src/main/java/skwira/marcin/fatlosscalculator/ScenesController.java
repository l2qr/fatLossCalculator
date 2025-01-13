package skwira.marcin.fatlosscalculator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class ScenesController {

    private final static ScenesController INSTANCE = new ScenesController();
    private Lookups.SceneType sceneType;


    private Stage stage;

    private ScenesController() {}
    public static ScenesController getInstance() {return INSTANCE;}

    public Object switchScene(Lookups.SceneType sceneType) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(sceneType.toString()));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene);
        stage.show();
        if (sceneType == Lookups.SceneType.EDIT) {
            ((CreateSceneController)fxmlLoader.getController()).getCreateFormController().setValues((Entry) stage.getUserData());
        }
        setSceneType(sceneType);
        return fxmlLoader.getController();
    }

    public Lookups.SceneType getSceneType() {
        return sceneType;
    }

    public void setSceneType(Lookups.SceneType sceneType) {
        this.sceneType = sceneType;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage s){
        this.stage = s;
        this.stage.setTitle("Fat Loss Calculator");
    }
}
