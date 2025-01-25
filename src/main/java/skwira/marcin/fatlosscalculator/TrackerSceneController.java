package skwira.marcin.fatlosscalculator;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import lombok.Setter;

public class TrackerSceneController {
    @FXML
    LineChart<Integer, Double> weightChart;
    @FXML
    NumberAxis weightXAxis;
    @FXML
    NumberAxis weightYAxis;

    @Setter
    Entry entry;

    @FXML
    public void initialize() {

    }

    public void setChartValues(Entry e) {
        this.entry = e;
        setChartValues();
    }
    public void setChartValues() {
        if(entry != null) {
            XYChart.Series<Integer,Double> goal = new XYChart.Series<>();
            goal.setName("Goal");
            for (int i = 0; i < entry.getTimeToTarget() + 1; i++) {
                double targetWeight = entry.getBodyMass() - (entry.getWeeklyBMLossKG() * i);
                goal.getData().add(new XYChart.Data<>(i, targetWeight));
            }
            weightXAxis.setTickUnit(1);
            weightXAxis.setLowerBound(-1);
            weightXAxis.setUpperBound(Math.ceil(entry.getTimeToTarget() + 1));
//            weightXAxis.getTickMarks().get(0).setLabel("");

            double decPlaces = 1e1;
            double tickUnitYDecPlace = Math.floor((entry.getBmLossRequired()/(entry.getTimeToTarget() + 2)) * 2 * decPlaces)/decPlaces;
            double lowBoundYDecPlace = Math.floor((entry.getBodyMass() - entry.getBmLossRequired() - tickUnitYDecPlace) * decPlaces)/decPlaces;
            double upBoundYDecPlace = Math.floor((entry.getBodyMass() + tickUnitYDecPlace) * decPlaces)/decPlaces;
            weightYAxis.setTickUnit(tickUnitYDecPlace);
            weightYAxis.setUpperBound(upBoundYDecPlace);
            weightYAxis.setLowerBound(lowBoundYDecPlace);

            weightChart.getData().addAll(goal);
        }
        return;
    }
}
