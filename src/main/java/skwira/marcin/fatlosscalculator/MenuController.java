package skwira.marcin.fatlosscalculator;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import java.util.Iterator;

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


    private ScenesController sc = ScenesController.getInstance();
    @FXML
    private CreateFormController createFormController;
    @FXML
    private ListSceneController listSceneController;

    public void setCreateFormController(CreateFormController createFormController) {
        this.createFormController = createFormController;
    }

    public void setListSceneController(ListSceneController listSceneController) {
        this.listSceneController = listSceneController;
    }

    @FXML
    public void initialize(Lookups.SceneType scene) {
        menuAddBtn.setOnMouseClicked(this::add);
        menuBackBtn.setOnMouseClicked(this::back);
        menuEditBtn.setOnMouseClicked(this::edit);
        menuSaveBtn.setOnMouseClicked(this::save);
        menuRemoveBtn.setOnMouseClicked(this::remove);
        switch (scene) {
            case CREATE -> {
                menu.getChildren().remove(menuHomeBtn);
                menu.getChildren().remove(menuAddBtn);
                menu.getChildren().remove(menuEditBtn);
            }
            case LIST -> {
                menu.getChildren().remove(menuHomeBtn);
                menu.getChildren().remove(menuBackBtn);
                menu.getChildren().remove(menuEditBtn);
                menu.getChildren().remove(menuSaveBtn);
                menu.getChildren().remove(menuRemoveBtn);
            }
            case DETAILS -> {
                menu.getChildren().remove(menuHomeBtn);
                menu.getChildren().remove(menuAddBtn);
                menu.getChildren().remove(menuSaveBtn);
            }
        }
    }

    protected boolean isBtnShown(Lookups.MenuBtnType bt) {
        for (int i = 0; i < menu.getChildren().size(); i++) {
            if (menu.getChildren().get(i).getId().equals(bt.toString()))
                return true;
        }
        return false;
    }

    protected void showButton(Lookups.MenuBtnType bt) {
        if (!isBtnShown(bt)) {
            switch (bt) {
                case ADD -> {
                    menu.getChildren().add(menuAddBtn);
                }
                case EDIT -> {
                    menu.getChildren().add(menuEditBtn);
                }
                case SAVE -> {
                    menu.getChildren().add(menuSaveBtn);
                }
                case REMOVE -> {
                    menu.getChildren().add(menuRemoveBtn);
                }
                case BACK -> {
                    menu.getChildren().add(0, menuBackBtn);
                }
            }
        }
    }

    protected void hideButton(Lookups.MenuBtnType bt) {
        if (isBtnShown(bt)) {
            Iterator<Node> it = menu.getChildren().iterator();
            while (it.hasNext()) {
                Node node = it.next();
                if (node.getId().equals(bt.toString())) {
                    it.remove();
                }
            }
        }
    }

    private void add(Event e) {
        sc.switchScene(Lookups.SceneType.CREATE);
    }

    private void back(Event e) {
        sc.switchScene(Lookups.SceneType.LIST);
    }

    private void edit(Event e) {
        sc.switchScene(Lookups.SceneType.EDIT);
    }

    private void save(Event e) {
        /* TODO add form validation */
        if (sc.getSceneType() == Lookups.SceneType.CREATE) {
            HelloApplication.dbController.insertEntry(createFormController.getValues());
        } else if (sc.getSceneType() == Lookups.SceneType.EDIT) {
            HelloApplication.dbController.updateEntry(createFormController.getValues());
        }
        sc.switchScene(Lookups.SceneType.LIST);
    }

    private void remove(Event e) {
        /* TODO add a dialog stage asking user to confirm removal */
        ObservableList<Node> entries = listSceneController.getEntriesList().getChildren();
        Iterator<Node> it = entries.iterator();
        while (it.hasNext()) {
            Node node = it.next();
            if(node.getTypeSelector().equals("HBoxListItem")){
                if(((HBoxListItem) node).isSelected()) {
                    HelloApplication.dbController.deleteEntry(((HBoxListItem) node).getEntry().getId());
                    it.remove();
                }
            }
        }
        for(int i = 0; i < entries.size(); i++) {
            Node entry = entries.get(i);
            if(entry.getTypeSelector().equals("HBoxListItem")) {
                entry.setLayoutY(i*Entry.LIST_ITEM_HEIGHT);
            }
        }
    }
}
