package skwira.marcin.fatlosscalculator;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DetailsSceneController {
    @FXML
    private MenuController menuController;

    @FXML
    private TableView detailsTable;

    @FXML
    private TableColumn<Entry.EntryPropertyRow, String> propertyColumn;

    @FXML
    private TableColumn<Entry.EntryPropertyRow, String> valueColumn;

    private Entry entry;

    @FXML
    public void initialize() {
        menuController.initialize(Lookups.SceneType.DETAILS);
        propertyColumn.setCellValueFactory(p -> p.getValue().propertyProperty());
        valueColumn.setCellValueFactory(p -> p.getValue().valueProperty());
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public void loadDetails(Entry entry) {
        setEntry(entry);
        detailsTable.getItems().setAll(entry.getPropertiesRows());
    }

}
