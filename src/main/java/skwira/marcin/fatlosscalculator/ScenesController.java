package skwira.marcin.fatlosscalculator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Stack;

@Getter
public final class ScenesController {

    private final static ScenesController INSTANCE = new ScenesController();
    @Setter
    private Lookups.SceneType sceneType;
    private Stage stage;
    @Setter
    private Entry entry;

    private Pair<Lookups.SceneType, Entry> currentScene;
    @Getter
    private Stack<Pair<Lookups.SceneType, Entry>> history = new Stack<>();

    private ScenesController() {}

    public static ScenesController getInstance() {
        return INSTANCE;
    }

    public Object switchScene(Lookups.SceneType sceneType) {
        return switchScene(sceneType, null);
    }

    public Object switchScene(Lookups.SceneType sceneType, Entry e) {
        if(currentScene != null)
            history.push(currentScene);
        currentScene = new Pair<>(sceneType, e);
        return changeScenes(sceneType, e);
    }

    private Object changeScenes(Lookups.SceneType sceneType, Entry e) {
        for (int i = 0; i < history.size(); i++) {
            System.out.printf("%d)%s: %s\n", i, history.get(i).getKey().toString(), (history.get(i).getValue() != null) ? history.get(i).getValue().getName() : "null");
        }
        this.sceneType = sceneType;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(sceneType.toString()));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        this.stage.setScene(scene);
        this.stage.show();
        setSceneType(sceneType);
        if(e != null) {
            switch (sceneType) {
                case CREATE, EDIT -> {
                    CreateSceneController ctrl = (CreateSceneController) fxmlLoader.getController();
                    ctrl.getCreateFormController().setValues(e);
                    return ctrl;
                }
                case DETAILS -> {
                    DetailsSceneController ctrl = (DetailsSceneController) fxmlLoader.getController();
                    ctrl.loadDetails(e);
                    return ctrl;
                }
                case TRACKER -> {
                    TrackerSceneController ctrl = (TrackerSceneController) fxmlLoader.getController();
                    ctrl.setChartValues(e);
                    return ctrl;
                }
            }
        }
        return fxmlLoader.getController();
    }

    public Object goBack() {
        currentScene = history.pop();
        return changeScenes(currentScene.getKey(), currentScene.getValue());
    }

    public void setStage(Stage s) {
        this.stage = s;
        this.stage.setTitle("Fat Loss Calculator");
    }
}
