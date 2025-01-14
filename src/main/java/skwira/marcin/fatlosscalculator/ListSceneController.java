package skwira.marcin.fatlosscalculator;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ListSceneController {

    @Getter
    @FXML
    private AnchorPane entriesList;
    @FXML
    private MenuController menuController;

    @Setter
    @Getter
    private Entry selectedEntry;

    EventHandler<Event> filter = new EventHandler<>() {
        @Override
        public void handle(Event event) {
            if ((event.getSource()) instanceof Button) {
                return;
            }
            HBoxListItem listItem = (HBoxListItem) event.getSource();
            ObservableList<Node> children = entriesList.getChildren();
            for (Node child : children) {
                if (!child.equals(listItem)) {
                    ((HBoxListItem) child).setSelected(false);
                }
            }
            listItem.toggle();
            if (listItem.isSelected()) {
                menuController.showButton(Lookups.MenuBtnType.EDIT);
                menuController.showButton(Lookups.MenuBtnType.REMOVE);
                selectedEntry = listItem.getEntry();
            } else {
                menuController.hideButton(Lookups.MenuBtnType.EDIT);
                menuController.hideButton(Lookups.MenuBtnType.REMOVE);
                selectedEntry = null;
            }
        }
    };

    @FXML
    public void initialize() {
        menuController.setListSceneController(this);
        ResultSet entries;
        try {
            entries = HelloApplication.dbController.selectEntries();
            int i = 0;
            ResultSetMetaData rsmd = entries.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (entries.next()) {
                /*for (int j = 1; j <= columnsNumber; j++) {
                    if (j > 1) System.out.print(",  ");
                    System.out.print(rsmd.getColumnName(j) + ": ");
                    String columnValue = entries.getString(j);
                    System.out.print(columnValue);
                }
                System.out.println();*/
                Entry e = new Entry(
                        entries.getInt("id"),
                        entries.getDouble("body_mass"),
                        entries.getDouble("carb_fat_distribution"),
                        entries.getString("created"),
                        entries.getString("condition"),
                        entries.getString("dob"),
                        entries.getDouble("fat_perc"),
                        entries.getString("last_updated"),
                        entries.getString("lifestyle"),
                        entries.getString("record_name"),
                        entries.getString("sex"),
                        entries.getDouble("target_fat_perc"),
                        entries.getDouble("weekly_loss")
                );
                HBoxListItem item = e.getListItem();
                item.setLayoutY((i * Entry.LIST_ITEM_HEIGHT));
                item.addEventFilter(MouseEvent.MOUSE_CLICKED, filter);
                entriesList.getChildren().add(item);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}