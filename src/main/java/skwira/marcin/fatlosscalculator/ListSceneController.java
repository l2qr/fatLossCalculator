package skwira.marcin.fatlosscalculator;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ListSceneController {

    @FXML
    private MenuController menuController;

    @FXML
    AnchorPane entriesList;

    @FXML
    Button removeButton;

    EventHandler<Event> filter = new EventHandler<>() {
        @Override
        public void handle(Event event) {
            HBoxListItem listItem = (HBoxListItem) event.getSource();
            listItem.toggle();
            ObservableList<Node> children = entriesList.getChildren();
            boolean itemsSelected = false;
            for (Node child : children) {
                if (((HBoxListItem) child).isSelected())
                    itemsSelected = true;
            }
            if(itemsSelected) {
                try{
                    ImageView btnImage = (ImageView) removeButton.getChildrenUnmodifiable().get(0);
                    btnImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/remove.png"))));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                try{
                    ImageView btnImage = (ImageView) removeButton.getChildrenUnmodifiable().get(0);
                    btnImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/remove_disabled.png"))));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }
    };

    @FXML
    public void initialize() {
        menuController.initialize(Lookups.MenuScene.LIST);
        ResultSet entries = null;
        try {
            entries = HelloApplication.dbController.selectEntries();
            while (entries.next()) {
                Entry e = new Entry(
                        entries.getDouble("body_mass"),
                        entries.getInt("carb_fat_distribution"),
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
                HBoxListItem item = getListItem(e);
                entriesList.getChildren().add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Label createLabel(String text) {
        Label label = new Label();
        label.setMaxHeight(50);
        label.setText(text);
        label.setAlignment(Pos.BASELINE_LEFT);
        label.setPadding(new Insets(0,0,0,7));
        return label;
    }

    private HBoxListItem getListItem(Entry e) {
        return getListItem(e.getName(), e.getSex().toString(), e.getAge(), e.getBodyMass(), e.getTargetFatPercentage());
    }

    private HBoxListItem getListItem(String name, String sex, long age, double bodymass, double targetFatPerc ) {
        Label nameLabel = createLabel(name);
        nameLabel.setMaxWidth(330);
        nameLabel.setMinWidth(330);

        Label sexLabel = createLabel(sex);
        sexLabel.setMinWidth(70);
        sexLabel.setMaxWidth(70);

        Label ageLabel = createLabel(Long.toString(age));
        ageLabel.setMinWidth(40);
        ageLabel.setMaxWidth(40);

        Label bodymassLabel = createLabel(Double.toString(bodymass) + "kg");
        bodymassLabel.setMinWidth(75);
        bodymassLabel.setMaxWidth(75);

        Label targetLabel = createLabel(Double.toString(targetFatPerc) + "%");
        targetLabel.setMinWidth(50);
        targetLabel.setMaxWidth(50);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        try {
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/skwira/marcin/fatlosscalculator/next.png"))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Button openBtn = new Button();
        openBtn.setMaxHeight(30);
        openBtn.setMaxWidth(30);
        openBtn.setMinWidth(30);
        openBtn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        openBtn.setGraphicTextGap(0);
        openBtn.setGraphic(imageView);

        HBoxListItem listItem = new HBoxListItem();
        listItem.setPrefHeight(50);
        listItem.setPrefWidth(600);
        listItem.setPadding(new Insets(0,3,0,0));
        listItem.getChildren().add(nameLabel);
        listItem.getChildren().add(sexLabel);
        listItem.getChildren().add(ageLabel);
        listItem.getChildren().add(bodymassLabel);
        listItem.getChildren().add(targetLabel);
        listItem.getChildren().add(openBtn);
        listItem.setAlignment(Pos.CENTER);
        listItem.addEventFilter(MouseEvent.MOUSE_CLICKED, filter);
        return listItem;
    }

    @FXML
    protected void onRemoveButtonClick() {
        ObservableList<Node> children = entriesList.getChildren();
        double lastY = 0;
        for (int i = 0; i < children.size(); i++) {
            Node child = children.get(i);
            if(((HBoxListItem)child).isSelected()) {
                lastY = child.getLayoutY();
                children.remove(child);
                for (int j = i; j < children.size(); j++) {
                    Node child2 = children.get(j);
                    double thisY = child2.getLayoutY();
                    child2.setLayoutY(lastY);
                    lastY = thisY;
                }
                i--;
            }
        }
        ImageView btnImage = (ImageView) removeButton.getChildrenUnmodifiable().get(0);
        try {
            btnImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/remove_disabled.png"))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}