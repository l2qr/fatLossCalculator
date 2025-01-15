package skwira.marcin.fatlosscalculator;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
    @FXML
    private Label lifestyleError;
    @FXML
    private Label conditionError;


    private final Set<Node> invalidInputs = new HashSet<>();

    @FXML
    public void initialize() {
        datepicker.setValue(LocalDate.now().minusYears(20));
        datepicker.commitValue();
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
        nameTextfield.focusedProperty().addListener((observable, oldValue, newValue) -> {
            TextField tf = (TextField) ((ReadOnlyBooleanProperty) observable).getBean();
            Label errorLabel = (Label) tf.getParent().getChildrenUnmodifiable().get(tf.getParent().getChildrenUnmodifiable().size() - 1);
            if (!newValue) {
                validateTextfieldString(tf, errorLabel);
            }
        });
        bodyMassTextfield.focusedProperty().addListener((observable, oldValue, newValue) -> {
            TextField tf = (TextField) ((ReadOnlyBooleanProperty) observable).getBean();
            Label errorLabel = (Label) tf.getParent().getChildrenUnmodifiable().get(tf.getParent().getChildrenUnmodifiable().size() - 1);
            if (!newValue) { // when focus lost
                validateTextfieldDouble(tf, errorLabel, 1d, 400d);
            }
        });
        fatTextfield.focusedProperty().addListener((observable, oldValue, newValue) -> {
            TextField tf = (TextField) ((ReadOnlyBooleanProperty) observable).getBean();
            Label errorLabel = (Label) tf.getParent().getChildrenUnmodifiable().get(tf.getParent().getChildrenUnmodifiable().size() - 1);
            if (!newValue) { // when focus lost
                validateTextfieldDouble(tf, errorLabel, 1d, 90d);
            }
        });
        targetFatTextfield.focusedProperty().addListener((observable, oldValue, newValue) -> {
            TextField tf = (TextField) ((ReadOnlyBooleanProperty) observable).getBean();
            Label errorLabel = (Label) tf.getParent().getChildrenUnmodifiable().get(tf.getParent().getChildrenUnmodifiable().size() - 1);
            if (!newValue) { // when focus lost
                double maxTarget = 89.0;
                if(fatTextfield.getText() != null && !fatTextfield.getText().isEmpty())
                    maxTarget = Double.parseDouble(fatTextfield.getText()) - 1;
                validateTextfieldDouble(tf, errorLabel, 1d, maxTarget);
            }
        });
        datepicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            DatePicker datePicker = (DatePicker) ((ReadOnlyBooleanProperty) observable).getBean();
            Label errorLabel = (Label) datePicker.getParent().getChildrenUnmodifiable().get(datePicker.getParent().getChildrenUnmodifiable().size() - 1);
            if (!newValue) { // when focus lost
                validateDatepicker(datePicker, errorLabel);
            }
        });
        lifestyleChoice.focusedProperty().addListener((observable, oldValue, newValue) -> {
            ChoiceBox<String> cb = (ChoiceBox<String>) ((ReadOnlyBooleanProperty) observable).getBean();
            Label errorLabel = (Label) cb.getParent().getChildrenUnmodifiable().get(cb.getParent().getChildrenUnmodifiable().size() - 1);
            if (!newValue) { // when focus lost
                validateRequiredChoice(cb, errorLabel);
            }
        });
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
        conditionChoice.focusedProperty().addListener((observable, oldValue, newValue) -> {
            ChoiceBox<String> cb = (ChoiceBox<String>) ((ReadOnlyBooleanProperty) observable).getBean();
            Label errorLabel = (Label) cb.getParent().getChildrenUnmodifiable().get(cb.getParent().getChildrenUnmodifiable().size() - 1);
            if (!newValue) { // when focus lost
                validateRequiredChoice(cb, errorLabel);
            }
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
    }

    private void validateTextfieldDouble(TextField tf, Label errorLabel) {
        validateTextfieldDouble(tf, errorLabel, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    private void validateTextfieldDouble(TextField tf, Label errorLabel, Double from, Double to) {
        String error = "Invalid input, must be a valid number";
        boolean valid = false;
        try {
            double input = Double.parseDouble(tf.getText());
            if(input >= from && input <= to) {
                valid = true;
            } else {
                error = String.format("Invalid input. Must be a valid number in the range %.1f - %.1f", from, to);
            }
        } catch (NumberFormatException e) {
            if(e.getMessage().equals("empty String"))
                error = "Can not be empty";
        } finally {
            changeControlState(tf, errorLabel, valid, error);
        }
    }

    private void validateDatepicker(DatePicker datePicker, Label errorLabel) {
        String error = "Invalid date";
        boolean valid = false;
        try {
            datePicker.getConverter().fromString(
                    datePicker.getEditor().getText());
            if(datePicker.getValue() == null) {
                error = "Don't be shy, select a date. We won't tell anyone";
            } else if(datePicker.getValue().isAfter(LocalDate.now())){
                error = "Aren't you a little to young to exist?";
            } else {
                valid = true;
            }
        } catch (DateTimeParseException e) {
            System.out.println(e.getMessage());
        } finally {
            changeControlState(datepicker, errorLabel, valid, error);
        }
    }

    private void validateTextfieldString(TextField tf, Label errorLabel) {
        if(tf.getText()!= null && !tf.getText().isBlank()) {
            changeControlState(tf, errorLabel, true);
        } else {
            changeControlState(tf, errorLabel, false, "Can not be empty");
        }
    }

    private void validateRequiredChoice(ChoiceBox<String> cb, Label errorLabel) {
        if(cb.getValue() != null && !cb.getValue().isBlank()) {
            changeControlState(cb, errorLabel, true);
        } else {
            changeControlState(cb, errorLabel, false, "Select a value");
        }
    }

    public boolean isValid() {
        validateTextfieldString(nameTextfield, nameError);
        validateTextfieldDouble(bodyMassTextfield, bodyMassError, 1.0, 400.0);
        validateTextfieldDouble(fatTextfield, fatPercError, 1.0, 90.0);
        double maxTarget = 89.0;
        if(fatTextfield.getText() != null && !fatTextfield.getText().isEmpty())
            maxTarget = Double.parseDouble(fatTextfield.getText()) - 1;
        validateTextfieldDouble(targetFatTextfield,targetFatError, 1.0, maxTarget);
        validateDatepicker(datepicker, dateError);
        validateRequiredChoice(lifestyleChoice,lifestyleError);
        validateRequiredChoice(conditionChoice,conditionError);
        return invalidInputs.isEmpty();
    }
}
