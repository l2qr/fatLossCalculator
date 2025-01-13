package skwira.marcin.fatlosscalculator;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

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

}
