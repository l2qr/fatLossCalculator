package skwira.marcin.fatlosscalculator;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import lombok.Setter;

import java.time.LocalDate;

public class TrackerSceneController {
    @FXML
    LineChart<Integer, Double> weightChart;
    @FXML
    NumberAxis weightXAxis;
    @FXML
    NumberAxis weightYAxis;
    @FXML
    Button addRecordBtn;
    @FXML
    Button removeRecordBtn;

    @Setter
    Entry entry;

    @FXML
    public void initialize() {
        addRecordBtn.setOnMouseClicked(e -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add Record");
            dialog.setHeaderText("Add a new weight and diet record");

            // Create the form elements
            DatePicker datePicker = new DatePicker(LocalDate.now());
            TextField weightField = new TextField();
            TextField caloriesField = new TextField();

            // Create error labels
            Label dateError = new Label();
            Label weightError = new Label();
            Label caloriesError = new Label();

            // Set error label styles
            dateError.setTextFill(Color.RED);
            weightError.setTextFill(Color.RED);
            caloriesError.setTextFill(Color.RED);

            // Create a grid pane for the form layout
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("Date:"), 0, 0);
            grid.add(datePicker, 1, 0);
            grid.add(dateError, 1, 1);

            grid.add(new Label("Weight (kg):"), 0, 2);
            grid.add(weightField, 1, 2);
            grid.add(weightError, 1, 3);

            grid.add(new Label("Calories:"), 0, 4);
            grid.add(caloriesField, 1, 4);
            grid.add(caloriesError, 1, 5);

            dialog.getDialogPane().setContent(grid);

            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);

            // Add validation logic
            okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                boolean isValid = true;

                // Date validation
                if (datePicker.getValue() == null) {
                    dateError.setText("Please select a date");
                    datePicker.setStyle("-fx-border-color: red;");
                    isValid = false;
                } else if (datePicker.getValue().isAfter(LocalDate.now())) {
                    dateError.setText("Date cannot be in the future");
                    datePicker.setStyle("-fx-border-color: red;");
                    isValid = false;
                } else {
                    dateError.setText("");
                    datePicker.setStyle("");
                }

                // Weight validation
                try {
                    double weight = Double.parseDouble(weightField.getText());
                    if (weight <= 0 || weight > 500) {
                        throw new NumberFormatException();
                    }
                    weightError.setText("");
                    weightField.setStyle("");
                } catch (NumberFormatException ex) {
                    weightError.setText("Please enter a valid weight (0-500 kg)");
                    weightField.setStyle("-fx-border-color: red;");
                    isValid = false;
                }

                // Calories validation
                try {
                    int calories = Integer.parseInt(caloriesField.getText());
                    if (calories < 0 || calories > 10000) {
                        throw new NumberFormatException();
                    }
                    caloriesError.setText("");
                    caloriesField.setStyle("");
                } catch (NumberFormatException ex) {
                    caloriesError.setText("Please enter a valid calorie count (0-10000)");
                    caloriesField.setStyle("-fx-border-color: red;");
                    isValid = false;
                }

                if (!isValid) {
                    event.consume();
                }
            });

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    LocalDate date = datePicker.getValue();
                    double weight = Double.parseDouble(weightField.getText());
                    int calories = Integer.parseInt(caloriesField.getText());

                    // TODO: Add logic to save the record
                    System.out.println("New record: Date=" + date + ", Weight=" + weight + ", Calories=" + calories);
                }
                return null;
            });
            dialog.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    // Handle the OK button click
                    System.out.println("OK button clicked");
                    // Add your logic here to handle the new record
                }
            });
        });
        removeRecordBtn.setOnMouseClicked(e -> {

        });
    }

    public void setChartValues(Entry e) {
        this.entry = e;
        setChartValues();
    }
    public void setChartValues() {
        if(entry != null) {
            XYChart.Series<Integer,Double> goalChartSeries = new XYChart.Series<>();
            goalChartSeries.setName("Goal");
            for (int i = 0; i < entry.getTimeToTarget() + 1; i++) {
                double targetWeight = entry.getBodyMass() - (entry.getWeeklyBMLossKG() * i);
                goalChartSeries.getData().add(new XYChart.Data<>(i, targetWeight));
            }
            weightXAxis.setTickUnit(1);
            weightXAxis.setLowerBound(-1);
            weightXAxis.setUpperBound(Math.ceil(entry.getTimeToTarget() + 1));

            double decPlaces = 1e1;
            double tickUnitYDecPlace = Math.floor((entry.getBmLossRequired()/(entry.getTimeToTarget() + 2)) * 2 * decPlaces)/decPlaces;
            double lowBoundYDecPlace = Math.floor((entry.getBodyMass() - entry.getBmLossRequired() - tickUnitYDecPlace) * decPlaces)/decPlaces;
            double upBoundYDecPlace = Math.floor((entry.getBodyMass() + tickUnitYDecPlace) * decPlaces)/decPlaces;
            weightYAxis.setTickUnit(tickUnitYDecPlace);
            weightYAxis.setUpperBound(upBoundYDecPlace);
            weightYAxis.setLowerBound(lowBoundYDecPlace);

            weightChart.getData().addAll(goalChartSeries);
        }
        return;
    }
}
