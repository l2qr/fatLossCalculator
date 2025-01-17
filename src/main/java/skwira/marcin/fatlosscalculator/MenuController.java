package skwira.marcin.fatlosscalculator;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Setter;

import java.sql.ResultSet;
import java.util.Iterator;

public class MenuController {

    @FXML
    private HBox menu;
    @FXML
    private Button menuHomeBtn;
    @FXML
    private Button menuBackBtn;
    @FXML
    private Button menuAddBtn;
    @FXML
    private Button menuEditBtn;
    @FXML
    private Button menuCopyBtn;
    @FXML
    private Button menuRemoveBtn;
    @FXML
    private Button menuSaveBtn;


    private final ScenesController sc = ScenesController.getInstance();
    @Setter
    @FXML
    private CreateFormController createFormController;
    @Setter
    @FXML
    private ListSceneController listSceneController;
    @Setter
    @FXML
    private DetailsSceneController detailsSceneController;


    @FXML
    public void initialize() {
        menuAddBtn.setOnMouseClicked(this::add);
        menuBackBtn.setOnMouseClicked(this::back);
        menuEditBtn.setOnMouseClicked(this::edit);
        menuCopyBtn.setOnMouseClicked(this::copy);
        menuSaveBtn.setOnMouseClicked(this::save);
        menuRemoveBtn.setOnMouseClicked(this::remove);
        switch (sc.getSceneType()) {
            case CREATE, EDIT -> {
                menu.getChildren().remove(menuHomeBtn);
                menu.getChildren().remove(menuAddBtn);
                menu.getChildren().remove(menuEditBtn);
                menu.getChildren().remove(menuCopyBtn);
            }
            case LIST -> {
                menu.getChildren().remove(menuHomeBtn);
                menu.getChildren().remove(menuBackBtn);
                menu.getChildren().remove(menuEditBtn);
                menu.getChildren().remove(menuCopyBtn);
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
                case ADD -> menu.getChildren().add(menuAddBtn);
                case EDIT -> menu.getChildren().add(menuEditBtn);
                case COPY -> menu.getChildren().add(menuCopyBtn);
                case SAVE -> menu.getChildren().add(menuSaveBtn);
                case REMOVE -> menu.getChildren().add(menuRemoveBtn);
                case BACK -> menu.getChildren().add(0, menuBackBtn);
            }
        }
    }

    protected void hideButton(Lookups.MenuBtnType bt) {
        if (isBtnShown(bt)) {
            menu.getChildren().removeIf(node -> node.getId().equals(bt.toString()));
        }
    }

    private void add(Event e) {
        sc.switchToCreateScene();
    }

    private void back(Event e) {
        sc.switchToListScene();
    }

    private void edit(Event e) {
        if (sc.getSceneType() == Lookups.SceneType.LIST) {
            sc.switchToEditScene(listSceneController.getSelectedEntry());
        }
        if(sc.getSceneType() == Lookups.SceneType.DETAILS) {
            sc.switchToEditScene(detailsSceneController.getEntry());
        }
    }

    private void copy(Event e) {
        if (sc.getSceneType() == Lookups.SceneType.LIST) {
            Entry entry = listSceneController.getSelectedEntry();
            ObservableList<Node> entries = listSceneController.getEntriesList().getChildren();
            int id = App.dbController.insertEntry(entry);
            try (ResultSet rs = App.dbController.selectEntry(id)){
                if(rs.next()) {
                    Entry entryCopy = new Entry(
                            rs.getInt("id"),
                            rs.getDouble("body_mass"),
                            rs.getDouble("carb_fat_distribution"),
                            rs.getString("created"),
                            rs.getString("condition"),
                            rs.getString("dob"),
                            rs.getDouble("fat_perc"),
                            rs.getString("last_updated"),
                            rs.getString("lifestyle"),
                            rs.getString("record_name"),
                            rs.getString("sex"),
                            rs.getDouble("target_fat_perc"),
                            rs.getDouble("weekly_loss")
                    );
                    listSceneController.addListItem(entryCopy);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        if(sc.getSceneType() == Lookups.SceneType.DETAILS) {
            sc.switchToCreateScene(detailsSceneController.getEntry());
        }
    }


    private void save(Event e) {
        if(createFormController.isValid()) {
            if (sc.getSceneType() == Lookups.SceneType.CREATE) {
                App.dbController.insertEntry(createFormController.getValues());
            } else if (sc.getSceneType() == Lookups.SceneType.EDIT) {
                App.dbController.updateEntry(createFormController.getValues());
            }
            sc.switchToListScene();
        } else {
            Alert validationFailedDialog = new Alert(Alert.AlertType.INFORMATION);
            validationFailedDialog.setHeaderText(null);
            validationFailedDialog.setContentText("Form contains errors. Please correct them to continue.");
            validationFailedDialog.setTitle("Errors detected");
            validationFailedDialog.showAndWait();
        }
    }

    private void remove(Event e) {
        switch(sc.getSceneType()) {
            case CREATE, EDIT -> createFormController.clearValues();
            case DETAILS -> showDialog(detailsSceneController.getEntry().getId());
            case LIST -> showDialog(listSceneController.getEntriesList().getChildren());
        }
    }

    private void showDialog(int id) {
        Alert confirmationDialog = createRemoveDialog();
        Stage dialogStage = (Stage) confirmationDialog.getDialogPane().getScene().getWindow();
        Button ok = (Button) confirmationDialog.getDialogPane().lookupButton(ButtonType.OK);
        ok.setOnAction(event -> {
            App.dbController.deleteEntry(id);
            sc.switchToListScene();
            confirmationDialog.close();
        });
        confirmationDialog.show();
        FXWinUtil.setDarkMode(dialogStage, true);
    }


    private void showDialog(ObservableList<Node> entries) {
        Alert confirmationDialog = createRemoveDialog();
        Stage dialogStage = (Stage) confirmationDialog.getDialogPane().getScene().getWindow();
        Button ok = (Button) confirmationDialog.getDialogPane().lookupButton(ButtonType.OK);
        ok.setOnAction(event -> {
            Iterator<Node> it = entries.iterator();
            while (it.hasNext()) {
                Node node = it.next();
                if (node.getTypeSelector().equals("HBoxListItem")) {
                    if (((HBoxListItem) node).isSelected()) {
                        App.dbController.deleteEntry(((HBoxListItem) node).getEntry().getId());
                        it.remove();
                    }
                }
            }
            for (int i = 0; i < entries.size(); i++) {
                Node entry = entries.get(i);
                if (entry.getTypeSelector().equals("HBoxListItem")) {
                    entry.setLayoutY(i * Entry.LIST_ITEM_HEIGHT);
                }
            }
            listSceneController.resizeList();
            hideButton(Lookups.MenuBtnType.EDIT);
            hideButton(Lookups.MenuBtnType.COPY);
            hideButton(Lookups.MenuBtnType.REMOVE);
            confirmationDialog.close();
        });
        confirmationDialog.show();
        FXWinUtil.setDarkMode(dialogStage, true);
    }

    private Alert createRemoveDialog() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirm deletion");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Are you sure you want to remove this record?");
        confirmationDialog.getDialogPane().getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        return  confirmationDialog;
    }
}
