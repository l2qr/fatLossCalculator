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

public class CreateFormController {

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
        lifestyleChoice.getItems().addAll(Lookups.Lifestyle.getNames());
        conditionChoice.getItems().addAll(Lookups.Condition.getNames());
    }

    public Entry getValues() {
        /* TODO add error handling on empty strings etc. */
        Entry result = new Entry();
        result.setBodyMass(Double.parseDouble(bodyMassTextfield.getText()));
        result.setCarbFat((int)carbFatSlider.getValue());
        result.setCondition(Lookups.Condition.valueOfLabel(conditionChoice.getValue()));
        result.setDateOfBirth(datepicker.getValue());
        result.setFatPercentage(Double.parseDouble(fatTextfield.getText()));
        result.setLifestyle(Lookups.Lifestyle.valueOfLabel(lifestyleChoice.getValue()));
        result.setName(nameTextfield.getText());
        result.setSex(Lookups.Sex.valueOfLabel(((RadioButton)sexRadio.getSelectedToggle()).getText()));
        result.setTargetFatPercentage(Double.parseDouble(targetFatTextfield.getText()));
        result.setWeeklyBMLossPercentage(weeklyBMLossSlider.getValue());
        return result;
    }
}
