package skwira.marcin.fatlosscalculator;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Lookups {
    public static Map<Integer, Double> proteinIntakeFactor;
    public static Map<BMI, Map<Condition, Double>> fmVsLbmLossMap = new HashMap<>();
    //    public static final double[] activityFactor = {1.2, 1.375, 1.55, 1.725, 1.9};
    public static Map<Lifestyle, Double> activityFactorMap;

    static {
        proteinIntakeFactor = Map.of(
                60, 3.8,
                50, 3.5,
                40, 3.1,
                30, 2.8,
                0, 2.4
        );
        activityFactorMap = Map.of(
                Lifestyle.SEDENTARY, 1.2,
                Lifestyle.LIGHT_ACTIVITY, 1.375,
                Lifestyle.MODERATE_ACTIVITY, 1.55,
                Lifestyle.VERY_ACTIVE, 1.725,
                Lifestyle.EXTRA_ACTIVE, 1.9
        );
        fmVsLbmLossMap.put(BMI.OBESE, Map.of(
                Condition.NP_NO_RT, 0.8,
                Condition.HP_NO_RT, 0.9,
                Condition.NP_RT, 0.9,
                Condition.HP_RT, 0.95
        ));
        fmVsLbmLossMap.put(BMI.OVERWEIGHT, Map.of(
                Condition.NP_NO_RT, 0.7,
                Condition.HP_NO_RT, 0.8,
                Condition.NP_RT, 0.8,
                Condition.HP_RT, 0.9
        ));
        fmVsLbmLossMap.put(BMI.NORMAL, Map.of(
                Condition.NP_NO_RT, 0.6,
                Condition.HP_NO_RT, 0.7,
                Condition.NP_RT, 0.7,
                Condition.HP_RT, 0.8
        ));
        fmVsLbmLossMap.put(BMI.LEAN, Map.of(
                Condition.NP_NO_RT, 0.5,
                Condition.HP_NO_RT, 0.6,
                Condition.NP_RT, 0.6,
                Condition.HP_RT, 0.7
        ));
    }

    public enum SceneType {
        LIST("listScene.fxml"),
        CREATE("createScene.fxml"),
        DETAILS("detailsScene.fxml"),
        EDIT("createScene.fxml");

        private final String name;

        private SceneType(String s) {
            name = s;
        }

        public static SceneType valueOfString(String string) {
            for (SceneType c : values()) {
                if (c.name.equals(string)) {
                    return c;
                }
            }
            return null;
        }

        public static String[] getStrings() {
            return Stream.of(SceneType.values()).map(SceneType::toString).toArray(String[]::new);
        }

        public boolean equalsString(String string) {
            return name.equals(string);
        }

        @Override
        public String toString() {
            return this.name;
        }

    }

    public enum MenuBtnType {
        ADD("menuAddBtn"),
        BACK("menuBackBtn"),
        EDIT("menuEditBtn"),
        SAVE("menuSaveBtn"),
        REMOVE("menuRemoveBtn");

        private final String name;

        private MenuBtnType(String s) {
            name = s;
        }

        public static MenuBtnType valueOfString(String string) {
            for (MenuBtnType c : values()) {
                if (c.name.equals(string)) {
                    return c;
                }
            }
            return null;
        }

        public static String[] getStrings() {
            return Stream.of(MenuBtnType.values()).map(MenuBtnType::toString).toArray(String[]::new);
        }

        public boolean equalsString(String string) {
            return name.equals(string);
        }

        @Override
        public String toString() {
            return this.name;
        }

    }

    public enum Sex {
        MALE("Male"),
        FEMALE("Female");

        private final String name;

        private Sex(String s) {
            name = s;
        }

        public static Sex valueOfString(String string) {
            for (Sex l : values()) {
                if (l.name.equals(string)) {
                    return l;
                }
            }
            return null;
        }

        public static String[] getStrings() {
            return Stream.of(Sex.values()).map(Sex::toString).toArray(String[]::new);
        }

        public boolean equalsString(String string) {
            return name.equals(string);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public enum Lifestyle {
        SEDENTARY("Sedentary"),
        LIGHT_ACTIVITY("Light Activity"),
        MODERATE_ACTIVITY("Moderate Activity"),
        VERY_ACTIVE("Very active"),
        EXTRA_ACTIVE("Extra Active");

        private final String name;

        private Lifestyle(String s) {
            name = s;
        }

        public static Lifestyle valueOfString(String string) {
            for (Lifestyle l : values()) {
                if (l.name.equals(string)) {
                    return l;
                }
            }
            return null;
        }

        public static String[] getStrings() {
            return Stream.of(Lifestyle.values()).map(Lifestyle::toString).toArray(String[]::new);
        }

        public boolean equalsString(String string) {
            return name.equals(string);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public enum Condition {
        NP_NO_RT("NP + No RT"),
        HP_NO_RT("HP + No RT"),
        NP_RT("NP + RT"),
        HP_RT("HP + RT");

        private final String name;

        private Condition(String s) {
            name = s;
        }

        public static Condition valueOfString(String string) {
            for (Condition c : values()) {
                if (c.name.equals(string)) {
                    return c;
                }
            }
            return null;
        }

        public static String[] getStrings() {
            return Stream.of(Condition.values()).map(Condition::toString).toArray(String[]::new);
        }

        public boolean equalsString(String string) {
            return name.equals(string);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public enum BMI {
        OBESE("Obese"),
        OVERWEIGHT("Overweight"),
        NORMAL("Normal"),
        LEAN("Lean");
        private final String name;

        private BMI(String s) {
            name = s;
        }

        public static BMI valueOfString(String string) {
            for (BMI c : values()) {
                if (c.name.equals(string)) {
                    return c;
                }
            }
            return null;
        }

        public static String[] getStrings() {
            return Stream.of(BMI.values()).map(BMI::toString).toArray(String[]::new);
        }

        public boolean equalsString(String string) {
            return name.equals(string);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
