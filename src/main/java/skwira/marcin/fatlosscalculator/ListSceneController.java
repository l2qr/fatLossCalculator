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
                menuController.showButton(Lookups.MenuBtnType.COPY);
                menuController.showButton(Lookups.MenuBtnType.REMOVE);
                selectedEntry = listItem.getEntry();
            } else {
                menuController.hideButton(Lookups.MenuBtnType.EDIT);
                menuController.hideButton(Lookups.MenuBtnType.COPY);
                menuController.hideButton(Lookups.MenuBtnType.REMOVE);
                selectedEntry = null;
            }
        }
    };

    @FXML
    public void initialize() {
        menuController.setListSceneController(this);
        try (ResultSet entries = App.dbController.selectEntries()){
            int i = 0;
            while (entries.next()) {
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
            resizeList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addListItem(Entry entry) {
        HBoxListItem item = entry.getListItem();
        item.setLayoutY((entriesList.getChildren().size() * Entry.LIST_ITEM_HEIGHT));
        item.addEventFilter(MouseEvent.MOUSE_CLICKED, filter);
        entriesList.getChildren().add(item);
        resizeList();
    }

    public void resizeList() {
        double height = entriesList.getChildren().size() * Entry.LIST_ITEM_HEIGHT;
        entriesList.setPrefHeight(height);
    }
}