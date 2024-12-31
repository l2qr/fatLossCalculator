package skwira.marcin.fatlosscalculator;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        while (entries.next()) {
            String nameLabel = entries.getString("record_name");
            String sex = entries.getString("");
            String age = entries.getString("");
            String bodymass = entries.getDouble("body_mass");
            String target = entries.getString("");
        }
    }

    private Label createLabel(String text) {
        Label label = new Label();
        label.setPrefHeight(50);
        label.setPrefWidth(500);
        label.setPadding(new Insets(5,5,5,5));
        label.setText(text);
        return label;
    }

    private HBox getListItem(ResultSet res) {

        HBoxListItem listItem = new HBoxListItem();
        listItem.setPrefHeight(50);
        listItem.setPrefWidth(600);
        listItem.setPadding(new Insets(5,5,5,5));

        Label nameLabel = createLabel("");
        Label sexLabel = createLabel("");
        Label ageLabel = createLabel("");
        Label bodymassLabel = createLabel("");
        Label targetLabel = createLabel("");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        try {
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("./next.png"))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Button openBtn = new Button();
        openBtn.setPrefHeight(30);
        openBtn.setPrefWidth(30);
        openBtn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        openBtn.setGraphicTextGap(0);
        openBtn.getChildrenUnmodifiable().add(imageView);

        listItem.getChildren().add(nameLabel);
        listItem.getChildren().add(sexLabel);
        listItem.getChildren().add(ageLabel);
        listItem.getChildren().add(bodymassLabel);
        listItem.getChildren().add(targetLabel);
        listItem.getChildren().add(targetLabel);
        listItem.getChildren().add(openBtn);
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