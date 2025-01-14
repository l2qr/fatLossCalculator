package skwira.marcin.fatlosscalculator;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Getter;

public class DetailsSceneController {
    @FXML
    private MenuController menuController;

    @FXML
    private TableView detailsTable;

    @FXML
    private TableColumn<Entry.EntryPropertyRow, String> propertyColumn;

    @FXML
    private TableColumn<Entry.EntryPropertyRow, String> valueColumn;

    @Getter
    private Entry entry;

    @FXML
    public void initialize() {
        menuController.setDetailsSceneController(this);
        propertyColumn.setCellValueFactory(p -> p.getValue().propertyProperty());
        valueColumn.setCellValueFactory(p -> p.getValue().valueProperty());
    }

    public void loadDetails(Entry entry) {
        ScenesController.getInstance().setEntry(entry);
        this.entry = entry;
        detailsTable.getItems().setAll(entry.getPropertiesRows());
    }

}
