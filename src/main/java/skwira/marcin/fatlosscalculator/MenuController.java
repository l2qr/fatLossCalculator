package skwira.marcin.fatlosscalculator;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private HBox menu;
    @FXML
    private Button menuHomeBtn;
    @FXML
    private Button menuBackBtn;
    @FXML
    private Pane menuSpacer;
    @FXML
    private Button menuAddBtn;
    @FXML
    private Button menuEditBtn;
    @FXML
    private Button menuRemoveBtn;
    @FXML
    private Button menuSaveBtn;

    @FXML
    private CreateFormController createFormController = null;

    @FXML
    public void initialize(Lookups.MenuScene scene) {
        switch (scene) {
            case LIST -> {
                menu.getChildren().remove(menuHomeBtn);
                menu.getChildren().remove(menuBackBtn);
                menu.getChildren().remove(menuSaveBtn);
                menuAddBtn.setOnMouseClicked(e -> {
                    try {
                        switchToCreateScene(e);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
            case CREATE -> {
                menu.getChildren().remove(menuHomeBtn);
                menu.getChildren().remove(menuAddBtn);
                menu.getChildren().remove(menuEditBtn);
                menuBackBtn.setOnMouseClicked(e -> {
                    try {
                        switchToListScene(e);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                menuSaveBtn.setOnMouseClicked(this::saveNewRecord);
            }
        }
    }

    public void initialize(Lookups.MenuScene scene, CreateFormController cfController) {
        initialize(scene);
        this.createFormController = cfController;
    }

    private void switchToCreateScene(Event event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("createScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 450);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void switchToListScene(Event event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("listScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 450);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void saveNewRecord(Event event) {
        System.out.println(createFormController.getValues().toString());
        HelloApplication.dbController.insertEntry(createFormController.getValues());
    }
}
