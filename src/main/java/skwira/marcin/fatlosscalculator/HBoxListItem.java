package skwira.marcin.fatlosscalculator;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

public class HBoxListItem extends HBox {

    public BooleanProperty selected =
            new BooleanPropertyBase(false) {
                @Override
                public Object getBean() {
                    return HBoxListItem.this;
                }

                @Override
                public String getName() {
                    return "selected";
                }

                @Override protected void invalidated() {
                    pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, get());
                }
            };

    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    @Getter
    @Setter
    private Entry entry;

    public HBoxListItem(Entry e) {
        super();
        this.entry = e;
        getStyleClass().add("hboxlistitem");
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean b) {
        this.selected.set(b);
    }

    public void toggle() {
        this.setSelected(!this.isSelected());
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

}
