package skwira.marcin.fatlosscalculator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Entry {

    private String name;
    private LocalDate createdDate;
    private LocalDate lastUpdateDate;
    private Lookups.Sex sex;
    private LocalDate dateOfBirth;
    private double bodyMass;
    private double fatPercentage;
    private Lookups.Lifestyle lifestyle;
    private Lookups.Condition condition;
    private double targetFatPercentage;
    private double weeklyBMLossPercentage;
    private int carbFat;

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


    @Override
    public String toString() {
        return "CreateFormSchema{" +
                "bodyMass=" + bodyMass +
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

    public Entry() {}

    public Entry(
            double bodyMass, int carbFat, String createdDate, String condition,
            String dateOfBirth, double fatPercentage, String lastUpdateDate, String lifestyle,
            String name, String sex, double targetFatPercentage, double weeklyBMLossPercentage
    ) {
        this(
                bodyMass,
                carbFat,
                LocalDate.parse(createdDate),
                Lookups.Condition.valueOfLabel(condition),
                LocalDate.parse(dateOfBirth),
                fatPercentage,
                LocalDate.parse(lastUpdateDate),
                Lookups.Lifestyle.valueOfLabel(lifestyle),
                name,
                Lookups.Sex.valueOfLabel(sex),
                targetFatPercentage,
                weeklyBMLossPercentage
        );
    }

    public Entry(
            double bodyMass, int carbFat, LocalDate createdDate, Lookups.Condition condition,
            LocalDate dateOfBirth, double fatPercentage, LocalDate lastUpdateDate, Lookups.Lifestyle lifestyle,
            String name, Lookups.Sex sex, double targetFatPercentage, double weeklyBMLossPercentage
    ) {
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
        if(sex == Lookups.Sex.MALE) {
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
        this.fmLossRequired = ((fatPercentage - targetFatPercentage)/fatPercentage) * fatMass;
        this.bmLossRequired = fmLossRequired / Lookups.fmVsLbmLossMap.get(bmi).get(condition);
        this.weeklyBMLossKG = weeklyBMLossPercentage * bodyMass;
        this.timeToTarget = bmLossRequired / weeklyBMLossKG;
        this.fmLossCalorieCost = fmLossRequired * 0.87 * 1000 * 9;
        this.lbmLossCalorieCost = (bmLossRequired - fmLossRequired) * 0.3 * 1000 * 4;
        this.totalBMLossCalorieCost = fmLossCalorieCost + lbmLossCalorieCost;
        this.requiredDailyCalorieDeficit = totalBMLossCalorieCost / (timeToTarget * 7);
        this.dailyCalorieIntake = bmr * Lookups.activityFactorMap.get(lifestyle);
        if(age > 60) {
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
        this.fiberGrams = dailyCalorieIntake/1000*12.5;
    }

    public double getBodyMass() {
        return bodyMass;
    }

    public void setBodyMass(double bodyMass) {
        this.bodyMass = bodyMass;
    }

    public int getCarbFat() {
        return carbFat;
    }

    public void setCarbFat(int carbFat) {
        this.carbFat = carbFat;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Lookups.Condition getCondition() {
        return condition;
    }

    public void setCondition(Lookups.Condition condition) {
        this.condition = condition;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public double getFatPercentage() {
        return fatPercentage;
    }

    public void setFatPercentage(double fatPercentage) {
        this.fatPercentage = fatPercentage;
    }

    public Lookups.Lifestyle getLifestyle() {
        return lifestyle;
    }

    public void setLifestyle(Lookups.Lifestyle lifestyle) {
        this.lifestyle = lifestyle;
    }

    public LocalDate getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDate lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Lookups.Sex getSex() {
        return sex;
    }

    public void setSex(Lookups.Sex sex) {
        this.sex = sex;
    }

    public double getTargetFatPercentage() {
        return targetFatPercentage;
    }

    public void setTargetFatPercentage(double targetFatPercentage) {
        this.targetFatPercentage = targetFatPercentage;
    }

    public double getWeeklyBMLossPercentage() {
        return weeklyBMLossPercentage;
    }

    public void setWeeklyBMLossPercentage(double weeklyBMLossPercentage) {
        this.weeklyBMLossPercentage = weeklyBMLossPercentage;
    }

    public long getAge() {
        return age;
    }

    public Lookups.BMI getBmi() {
        return bmi;
    }

    public double getFatMass() {
        return fatMass;
    }

    public double getLeanMass() {
        return leanMass;
    }

    public double getBmr() {
        return bmr;
    }

    public double getFmLossRequired() {
        return fmLossRequired;
    }

    public double getBmLossRequired() {
        return bmLossRequired;
    }

    public double getWeeklyBMLossKG() {
        return weeklyBMLossKG;
    }

    public double getTimeToTarget() {
        return timeToTarget;
    }

    public double getFmLossCalorieCost() {
        return fmLossCalorieCost;
    }

    public double getLbmLossCalorieCost() {
        return lbmLossCalorieCost;
    }

    public double getTotalBMLossCalorieCost() {
        return totalBMLossCalorieCost;
    }

    public double getRequiredDailyCalorieDeficit() {
        return requiredDailyCalorieDeficit;
    }

    public double getDailyCalorieIntake() {
        return dailyCalorieIntake;
    }

    public double getProteinGrams() {
        return proteinGrams;
    }

    public double getCarbsGrams() {
        return carbsGrams;
    }

    public double getFatGrams() {
        return fatGrams;
    }

    public double getFiberGrams() {
        return fiberGrams;
    }
}
