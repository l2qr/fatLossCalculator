package skwira.marcin.fatlosscalculator;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

public class CreateFormController {

    @Setter
    @Getter
    private Entry entry = null;
    @FXML
    private TextField bodyMassTextfield;
    @FXML
    private Slider carbFatSlider;
    @FXML
    private ChoiceBox<String> conditionChoice;
    @FXML
    private Button conditionHelp;
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
    private Button lifestyleHelp;
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
    private Label nameError;
    @FXML
    private Label dateError;
    @FXML
    private Label bodyMassError;
    @FXML
    private Label fatPercError;
    @FXML
    private Label targetFatError;

    private Set<Node> invalidInputs = new HashSet<>();

    @FXML
    public void initialize() {
        lifestyleChoice.getItems().addAll(Lookups.Lifestyle.getStrings());
        conditionChoice.getItems().addAll(Lookups.Condition.getStrings());
        carbFatSlider.setLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double aDouble) {
                return String.format("%.0f/%.0f", aDouble, 100 - aDouble);
            }

            @Override
            public Double fromString(String s) {
                return Double.parseDouble(s.substring(0, s.indexOf('/')));
            }
        });
        sexRadio.selectToggle(maleRadio);
        nameTextfield.focusedProperty().addListener(this::nameValidateListener);
        bodyMassTextfield.focusedProperty().addListener(this::textfieldDoubleValidateListener);
        fatTextfield.focusedProperty().addListener(this::textfieldDoubleValidateListener);
        targetFatTextfield.focusedProperty().addListener(this::textfieldDoubleValidateListener);
        datepicker.focusedProperty().addListener(this::datepickerValidateListener);
        lifestyleHelp.setOnMouseClicked(event -> {
            Alert help = new Alert(Alert.AlertType.INFORMATION);
            help.setHeaderText("Lifestyle");
            help.setContentText("""
                    Activity Factor:
                    
                    Sedentary - You work a desk job and don’t exercise
                    Light Activity - You work a desk job but do a bit of regular exercise. Or you don’t exercise but you work at a job that’s pretty active (a nurse, teacher, etc.) where you’re on your for feet most of the day.
                    Moderate Activity - Most of you will probably fall into this category. Maybe you work a sedentary job, but you train like a madman. Or maybe you train moderately, but you also have a job where you stand on your feet all the time. Someone who doesn’t train but works a hard labor job would also fall into this category.
                    Very Active - You train hard most days of the week, and you also work a job where you’re on your feet quite a bit. Overall, you’re active most of the day.
                    Extra Active - You train hard and work a job that is physically intense in nature. As an example, you’re a roofer who also goes to the gym five days a week.
                    """);
            help.setTitle("Lifestyle choice help");
            help.getDialogPane().getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            Stage dialogStage = (Stage) help.getDialogPane().getScene().getWindow();
            help.show();
            FXWinUtil.setDarkMode(dialogStage, true);
        });
        conditionHelp.setOnMouseClicked(event -> {
            Alert help = new Alert(Alert.AlertType.INFORMATION);
            help.setHeaderText("Dietary and Excercise Conditions");
            help.setContentText("""
                    Dietary and Excercise Conditions abbreviations:
                    
                    NP - normal protein diet up to 1.6 grams of protein per kilogram of weight
                    HP - high protein diet more than 1.6 grams of protein per kilogram of weight
                    No RT - resistance training less than 2 hours per week
                    RT - resistance training at least 2 hours per week""");
            help.setTitle("Lifestyle choice help");
            help.getDialogPane().getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            Stage dialogStage = (Stage) help.getDialogPane().getScene().getWindow();
            help.show();
            FXWinUtil.setDarkMode(dialogStage, true);
        });

    }

    public Entry getValues() {
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

    public void clearValues() {
        this.entry = null;
        nameTextfield.clear();
        sexRadio.selectToggle(maleRadio);
        datepicker.setValue(null);
        bodyMassTextfield.clear();
        fatTextfield.clear();
        lifestyleChoice.setValue(null);
        conditionChoice.setValue(null);
        targetFatTextfield.clear();
        weeklyBMLossSlider.setValue(0.8);
        carbFatSlider.setValue(70);
    }
    private void changeControlState(Node node, Label label, boolean valid, String errMsg) {
        changeControlState(node, label, valid);
        label.setText(errMsg);
    }
    private void changeControlState(Node node, Label label, boolean valid) {
        if(valid){
            node.getStyleClass().remove("invalid");
            label.setVisible(false);
            label.setManaged(false);
            invalidInputs.remove(node);
        } else {
            if(!node.getStyleClass().contains("invalid"))
                node.getStyleClass().add("invalid");
            label.setVisible(true);
            label.setManaged(true);
            invalidInputs.add(node);
        }
        System.out.println(invalidInputs.size());
    }

    private void textfieldDoubleValidateListener(ObservableValue<? extends Boolean> observable, Boolean oldVal, Boolean newVal) {
        TextField tf = (TextField) ((ReadOnlyBooleanProperty) observable).getBean();
        Label errorLabel = (Label) tf.getParent().getChildrenUnmodifiable().get(tf.getParent().getChildrenUnmodifiable().size() - 1);
        if (!newVal) { // when focus lost
            try {
                Double.parseDouble(tf.getText());
                changeControlState(tf, errorLabel, true);
            } catch (NumberFormatException e) {
                String error = "Invalid input, must be a valid number";
                if(e.getMessage().equals("empty String"))
                    error = "Can not be empty";
                changeControlState(tf, errorLabel, false, error);
            }
        }
    }

    private void datepickerValidateListener(ObservableValue<? extends Boolean> observable, Boolean oldVal, Boolean newVal) {
        DatePicker datePicker = (DatePicker) ((ReadOnlyBooleanProperty) observable).getBean();
        Label errorLabel = (Label) datePicker.getParent().getChildrenUnmodifiable().get(datePicker.getParent().getChildrenUnmodifiable().size() - 1);
        if (!newVal) { // when focus lost
            try {
                datePicker.getConverter().fromString(
                        datePicker.getEditor().getText());
                changeControlState(datePicker, errorLabel, true);
            } catch (DateTimeParseException e) {
                // Alert user here
                changeControlState(datePicker, errorLabel, false, "Invalid date");
            }
        }
    }

    private void nameValidateListener(ObservableValue<? extends Boolean> observable, Boolean oldVal, Boolean newVal) {
        TextField tf = (TextField) ((ReadOnlyBooleanProperty) observable).getBean();
        Label errorLabel = (Label) tf.getParent().getChildrenUnmodifiable().get(tf.getParent().getChildrenUnmodifiable().size() - 1);
        if (!newVal) {
            if(!tf.getText().isEmpty()) {
                changeControlState(tf, errorLabel, true);
            } else {
                changeControlState(tf, errorLabel, false, "Can not be empty");
            }
        }
    }

    public boolean isValid() {
        /* TODO separate validation methods from listeners
            and call validations here seperately for each input that needs checking
            to have a full check when user tries to save
            */
        return invalidInputs.isEmpty();
    }
}
