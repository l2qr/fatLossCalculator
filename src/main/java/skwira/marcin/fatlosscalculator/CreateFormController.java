package skwira.marcin.fatlosscalculator;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;

public class CreateFormController {

    private Entry entry = null;
    @FXML
    private TextField bodyMassTextfield;
    @FXML
    private Slider carbFatSlider;
    @FXML
    private ChoiceBox<String> conditionChoice;
    @FXML
    private VBox createFormList;
    @FXML
    private ScrollPane createFormPane;
    @FXML
    private DatePicker datepicker;
    @FXML
    private TextField fatTextfield;
    @FXML
    private RadioButton femaleRadio;
    @FXML
    private ChoiceBox<String> lifestyleChoice;
    @FXML
    private RadioButton maleRadio;
    @FXML
    private TextField nameTextfield;
    @FXML
    private ToggleGroup sexRadio;
    @FXML
    private TextField targetFatTextfield;
    @FXML
    private Slider weeklyBMLossSlider;

    /* TODO Help button click listener and help dialog with choice explanations */

    @FXML
    public void initialize() {
        lifestyleChoice.getItems().addAll(Lookups.Lifestyle.getStrings());
        conditionChoice.getItems().addAll(Lookups.Condition.getStrings());
        carbFatSlider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double aDouble) {
                return String.format("%.0f/%.0f", aDouble, 100 - aDouble);
            }

            @Override
            public Double fromString(String s) {
                return Double.parseDouble(s.substring(0, s.indexOf('/')));
            }
        });
        bodyMassTextfield.focusedProperty().addListener(this::textfieldDoubleValidateListener);
        fatTextfield.focusedProperty().addListener(this::textfieldDoubleValidateListener);
        targetFatTextfield.focusedProperty().addListener(this::textfieldDoubleValidateListener);
        datepicker.focusedProperty().addListener((arg0, oldVal, newVal) -> {
            if(!newVal) {
//                datepicker.;
            }
        });
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public Entry getValues() {
        /* TODO add error handling on empty strings etc. */
        if(this.entry == null)
            this.entry = new Entry();
        this.entry.setName(nameTextfield.getText());
        this.entry.setSex(Lookups.Sex.valueOfString(((RadioButton) sexRadio.getSelectedToggle()).getText()));
        this.entry.setDateOfBirth(datepicker.getValue());
        this.entry.setBodyMass(Double.parseDouble(bodyMassTextfield.getText()));
        this.entry.setFatPercentage(Double.parseDouble(fatTextfield.getText()) / 100);
        this.entry.setLifestyle(Lookups.Lifestyle.valueOfString(lifestyleChoice.getValue()));
        this.entry.setCondition(Lookups.Condition.valueOfString(conditionChoice.getValue()));
        this.entry.setTargetFatPercentage(Double.parseDouble(targetFatTextfield.getText()) / 100);
        this.entry.setWeeklyBMLossPercentage(weeklyBMLossSlider.getValue() / 100);
        this.entry.setCarbFat(carbFatSlider.getValue() / 100.0f);
        return this.entry;
    }

    public void setValues(Entry entry) {
        this.entry = entry;
        nameTextfield.setText(entry.getName());
        if (entry.getSex() == Lookups.Sex.MALE) {
            sexRadio.selectToggle(maleRadio);
        } else {
            sexRadio.selectToggle(femaleRadio);
        }
        datepicker.setValue(entry.getDateOfBirth());
        bodyMassTextfield.setText(String.format("%.1f", entry.getBodyMass()));
        fatTextfield.setText(String.format("%.1f", entry.getFatPercentage() * 100));
        lifestyleChoice.setValue(entry.getLifestyle().toString());
        conditionChoice.setValue(entry.getCondition().toString());
        targetFatTextfield.setText(String.format("%.1f", entry.getTargetFatPercentage() * 100));
        weeklyBMLossSlider.setValue(entry.getWeeklyBMLossPercentage() * 100);
        carbFatSlider.setValue(entry.getCarbFat() * 100);
    }

    private void textfieldDoubleValidateListener(ObservableValue arg0, Boolean oldVal, Boolean newVal) {
        TextField tf = (TextField) ((ReadOnlyBooleanProperty) arg0).getBean();
        if (!newVal) { // when focus lost
            if(!validateDouble(tf.getText())) {
                tf.getStyleClass().add("invalid");
            }else{
                if(tf.getStyleClass().contains("invalid"))
                    tf.getStyleClass().remove("invalid");
            }
        }
    }

    private boolean validateDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean validateDate(String s) {
        try {
            LocalDate.parse(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
