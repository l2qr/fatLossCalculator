package skwira.marcin.fatlosscalculator;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Entry {

    @Setter
    private String name;
    @Setter
    private LocalDate createdDate;
    @Setter
    private LocalDate lastUpdateDate;
    @Setter
    private Lookups.Sex sex;
    @Setter
    private LocalDate dateOfBirth;
    @Setter
    private double bodyMass;
    @Setter
    private double fatPercentage;
    @Setter
    private Lookups.Lifestyle lifestyle;
    @Setter
    private Lookups.Condition condition;
    @Setter
    private double targetFatPercentage;
    @Setter
    private double weeklyBMLossPercentage;
    @Setter
    private double carbFat;
    @Setter
    private int id;
    private long age;
    private Lookups.BMI bmi;
    private double fatMass;
    private double leanMass;
    private double bmr;
    private double fmLossRequired;
    private double bmLossRequired;
    private double weeklyBMLossKG;
    private double timeToTarget;
    private double fmLossCalorieCost;
    private double lbmLossCalorieCost;
    private double totalBMLossCalorieCost;
    private double requiredDailyCalorieDeficit;
    private double dailyCalorieIntake;
    private double proteinGrams;
    private double carbsGrams;
    private double fatGrams;
    private double fiberGrams;

    static final int LIST_ITEM_HEIGHT = 40;

    @Override
    public String toString() {
        return "CreateFormSchema{" +
                "id=" + id +
                ", bodyMass=" + bodyMass +
                ", carbFat=" + carbFat +
                ", condition=" + condition +
                ", dateOfBirth=" + dateOfBirth +
                ", fat=" + fatPercentage +
                ", lifestyle=" + lifestyle +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", targetFat=" + targetFatPercentage +
                ", weeklyBMLossPercentage=" + weeklyBMLossPercentage +
                '}';
    }

    public Entry() {
    }

    public Entry(
            int id, double bodyMass, double carbFat, String createdDate, String condition,
            String dateOfBirth, double fatPercentage, String lastUpdateDate, String lifestyle,
            String name, String sex, double targetFatPercentage, double weeklyBMLossPercentage
    ) {
        this(
                id, bodyMass, carbFat,
                LocalDate.parse(createdDate),
                Lookups.Condition.valueOfString(condition),
                LocalDate.parse(dateOfBirth),
                fatPercentage,
                LocalDate.parse(lastUpdateDate),
                Lookups.Lifestyle.valueOfString(lifestyle),
                name,
                Lookups.Sex.valueOfString(sex),
                targetFatPercentage,
                weeklyBMLossPercentage
        );
    }

    public Entry(
            int id, double bodyMass, double carbFat, LocalDate createdDate, Lookups.Condition condition,
            LocalDate dateOfBirth, double fatPercentage, LocalDate lastUpdateDate, Lookups.Lifestyle lifestyle,
            String name, Lookups.Sex sex, double targetFatPercentage, double weeklyBMLossPercentage
    ) {
        this.id = id;
        this.createdDate = createdDate;
        this.lastUpdateDate = lastUpdateDate;
        this.bodyMass = bodyMass;
        this.carbFat = carbFat;
        this.condition = condition;
        this.dateOfBirth = dateOfBirth;
        this.fatPercentage = fatPercentage;
        this.lifestyle = lifestyle;
        this.name = name;
        this.sex = sex;
        this.targetFatPercentage = targetFatPercentage;
        this.weeklyBMLossPercentage = weeklyBMLossPercentage;
        calculateValues();
    }

    private void calculateValues() {
        this.age = ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
        if (sex == Lookups.Sex.MALE) {
            if (fatPercentage >= 0.27)
                this.bmi = Lookups.BMI.OBESE;
            if (fatPercentage >= 0.22)
                this.bmi = Lookups.BMI.OVERWEIGHT;
            if (fatPercentage >= 0.11)
                this.bmi = Lookups.BMI.NORMAL;
            if (fatPercentage < 0.11)
                this.bmi = Lookups.BMI.LEAN;
        } else {
            if (fatPercentage >= 0.4)
                this.bmi = Lookups.BMI.OBESE;
            if (fatPercentage >= 0.35)
                this.bmi = Lookups.BMI.OVERWEIGHT;
            if (fatPercentage >= 0.23)
                this.bmi = Lookups.BMI.NORMAL;
            if (fatPercentage < 0.23)
                this.bmi = Lookups.BMI.LEAN;
        }
        this.fatMass = bodyMass * fatPercentage;
        this.leanMass = bodyMass * (1 - fatPercentage);
        this.bmr = (13.587 * leanMass) + (9.613 * fatMass) +
                (sex == Lookups.Sex.MALE ? 198 : 0) - (3.351 * age) + 674;
        this.fmLossRequired = ((fatPercentage - targetFatPercentage) / fatPercentage) * fatMass;
        this.bmLossRequired = fmLossRequired / Lookups.fmVsLbmLossMap.get(bmi).get(condition);
        this.weeklyBMLossKG = weeklyBMLossPercentage * bodyMass;
        this.timeToTarget = bmLossRequired / weeklyBMLossKG;
        this.fmLossCalorieCost = fmLossRequired * 0.87 * 1000 * 9;
        this.lbmLossCalorieCost = (bmLossRequired - fmLossRequired) * 0.3 * 1000 * 4;
        this.totalBMLossCalorieCost = fmLossCalorieCost + lbmLossCalorieCost;
        this.requiredDailyCalorieDeficit = totalBMLossCalorieCost / (timeToTarget * 7);
        this.dailyCalorieIntake = bmr * Lookups.activityFactorMap.get(lifestyle);
        if (age > 60) {
            this.proteinGrams = leanMass * Lookups.proteinIntakeFactor.get(60);
        } else if (age > 50) {
            this.proteinGrams = leanMass * Lookups.proteinIntakeFactor.get(50);
        } else if (age > 40) {
            this.proteinGrams = leanMass * Lookups.proteinIntakeFactor.get(40);
        } else if (age > 30) {
            this.proteinGrams = leanMass * Lookups.proteinIntakeFactor.get(30);
        } else {
            this.proteinGrams = leanMass * Lookups.proteinIntakeFactor.get(0);
        }
        this.carbsGrams = (dailyCalorieIntake - proteinGrams * 4) * carbFat / 4;
        this.fatGrams = (dailyCalorieIntake - proteinGrams * 4) * (1 - carbFat) / 9;
        this.fiberGrams = dailyCalorieIntake / 1000 * 12.5;
    }


    @org.jetbrains.annotations.NotNull
    private Label createLabel(String text) {
        Label label = new Label();
        label.setMaxHeight(60);
        label.setText(text);
        HBoxListItem.setHgrow(label, Priority.NEVER);
        label.getStyleClass().add("list-label");
        return label;
    }

    public HBoxListItem getListItem() {
        Label nameLabel = createLabel(this.name);
        nameLabel.setMaxWidth(300);
        nameLabel.setMinWidth(300);
        nameLabel.setWrapText(true);

        Label sexLabel = createLabel(this.sex.toString());
        sexLabel.setMinWidth(70);
        sexLabel.setMaxWidth(70);

        Label ageLabel = createLabel(Long.toString(this.age));
        ageLabel.setMinWidth(40);
        ageLabel.setMaxWidth(40);

        Label bodymassLabel = createLabel(String.format("%.1fkg", this.bodyMass));
        bodymassLabel.setMinWidth(75);
        bodymassLabel.setMaxWidth(75);

        Label targetLabel = createLabel(String.format("%.1f%%", this.targetFatPercentage * 100));
        targetLabel.setMinWidth(40);
        targetLabel.setMaxWidth(40);

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
        openBtn.setMinHeight(30);
        openBtn.setMaxHeight(30);
        openBtn.setMaxWidth(30);
        openBtn.setMinWidth(30);
        openBtn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        openBtn.setGraphicTextGap(0);
        openBtn.setGraphic(imageView);
        HBoxListItem listItem = new HBoxListItem(this);
        listItem.setPrefHeight(LIST_ITEM_HEIGHT);
        listItem.setPrefWidth(600);
        listItem.setPadding(Insets.EMPTY);
        listItem.setSpacing(0);

        listItem.getChildren().add(nameLabel);
        listItem.getChildren().add(sexLabel);
        listItem.getChildren().add(ageLabel);
        listItem.getChildren().add(bodymassLabel);
        listItem.getChildren().add(targetLabel);
        listItem.getChildren().add(openBtn);
        listItem.setAlignment(Pos.CENTER);

        openBtn.setOnMouseClicked(e -> ScenesController.getInstance().switchToDetailsScene(this));

        return listItem;
    }

    public static class EntryPropertyRow {
        private final StringProperty property = new SimpleStringProperty(this, "property");
        private final StringProperty value = new SimpleStringProperty(this, "value");

        public EntryPropertyRow(String property, String value) {
            this.property.set(property);
            this.value.set(value);
        }

        public String getProperty() {
            return property.get();
        }

        public StringProperty propertyProperty() {
            return property;
        }

        public String getValue() {
            return value.get();
        }

        public StringProperty valueProperty() {
            return value;
        }
    }

    public List<EntryPropertyRow> getPropertiesRows() {
        List<EntryPropertyRow> rows = new ArrayList<>();
        rows.add(new EntryPropertyRow("ID", Integer.toString(this.id)));
        rows.add(new EntryPropertyRow("Name", this.name));
        rows.add(new EntryPropertyRow("Created Date", this.createdDate.toString()));
        rows.add(new EntryPropertyRow("Last Updated", this.lastUpdateDate.toString()));
        rows.add(new EntryPropertyRow("Sex", this.sex.toString()));
        rows.add(new EntryPropertyRow("Date Of Birth", this.dateOfBirth.toString()));
        rows.add(new EntryPropertyRow("Body Mass", String.format("%.1f", this.bodyMass) + "kg"));
        rows.add(new EntryPropertyRow("Fat Percentage", String.format("%.1f", this.fatPercentage * 100) + "%"));
        rows.add(new EntryPropertyRow("Lifestyle", this.lifestyle.toString()));
        rows.add(new EntryPropertyRow("Condition", this.condition.toString()));
        rows.add(new EntryPropertyRow("Target Body Fat", String.format("%.1f", this.targetFatPercentage * 100) + "%"));
        rows.add(new EntryPropertyRow("Weekly Body Mass Loss", String.format("%.1f", this.weeklyBMLossPercentage * 100) + "%"));
        rows.add(new EntryPropertyRow("Carbs/Fats Ratio", String.format("%.0f", this.carbFat * 100) + "/" + String.format("%.0f", 100 - this.carbFat * 100)));
        rows.add(new EntryPropertyRow("Age", Long.toString(this.age)));
        rows.add(new EntryPropertyRow("BMI", this.bmi.toString()));
        rows.add(new EntryPropertyRow("Fat Mass", String.format("%.1f", this.fatMass) + "kg"));
        rows.add(new EntryPropertyRow("Lean Mass", String.format("%.1f", this.leanMass) + "kg"));
        rows.add(new EntryPropertyRow("Base Metabolic Rate", String.format("%.0f", this.bmr) + "kcal"));
        rows.add(new EntryPropertyRow("Fat Mass Loss Required", String.format("%.1f", this.fmLossRequired) + "kg"));
        rows.add(new EntryPropertyRow("Body Mass Loss Required", String.format("%.1f", this.bmLossRequired) + "kg"));
        rows.add(new EntryPropertyRow("Weekly Body Mass Loss", String.format("%.1f", this.weeklyBMLossKG) + "kg"));
        rows.add(new EntryPropertyRow("Time to target", String.format("%.1f", this.timeToTarget) + " weeks"));
        rows.add(new EntryPropertyRow("Fat Mass Loss Calorie Cost", String.format("%.0f", this.fmLossCalorieCost) + "kcal"));
        rows.add(new EntryPropertyRow("Lean Body Mass Calorie Cost", String.format("%.0f", this.lbmLossCalorieCost) + "kcal"));
        rows.add(new EntryPropertyRow("Total Body Mass Calorie Cost", String.format("%.0f", this.totalBMLossCalorieCost) + "kcal"));
        rows.add(new EntryPropertyRow("Required Daily Calorie Deficit", String.format("%.0f", this.requiredDailyCalorieDeficit) + "kcal"));
        rows.add(new EntryPropertyRow("Daily Calorie Intake", String.format("%.1f", this.dailyCalorieIntake) + "kcal"));
        rows.add(new EntryPropertyRow("Protein", String.format("%.0f", this.proteinGrams) + "g"));
        rows.add(new EntryPropertyRow("Carbs", String.format("%.0f", this.carbsGrams) + "g"));
        rows.add(new EntryPropertyRow("Fat", String.format("%.0f", this.fatGrams) + "g"));
        rows.add(new EntryPropertyRow("Fiber", String.format("%.0f", this.fiberGrams) + "g"));
        return rows;
    }

}
