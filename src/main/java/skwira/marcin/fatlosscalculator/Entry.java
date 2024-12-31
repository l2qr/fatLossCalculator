package skwira.marcin.fatlosscalculator;

import java.time.LocalDate;

public class Entry {

    private double bodyMass;
    private int carbFat;
    private Lookups.Condition condition;
    private LocalDate dateOfBirth;
    private double fatPercentage;
    private Lookups.Lifestyle lifestyle;
    private String name;
    private Lookups.Sex sex;
    private double targetFatPercentage;
    private double weeklyBMLossPercentage;

    private int age;
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

    public Entry(double bodyMass, int carbFat, Lookups.Condition condition, LocalDate dateOfBirth, double fatPercentage, Lookups.Lifestyle lifestyle, String name, Lookups.Sex sex, double targetFatPercentage, double weeklyBMLossPercentage) {
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
        this.age = LocalDate.now().getYear() - dateOfBirth.getYear();
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
// TODO finish the calculations for protein, carbs, fat, fiber
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
}
