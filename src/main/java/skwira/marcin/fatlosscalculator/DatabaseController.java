package skwira.marcin.fatlosscalculator;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseController {

    private String dbPath = "./";
    protected Connection conn = null;

    DatabaseController(String dbName) throws Exception {
        conn = DriverManager.getConnection("jdbc:sqlite:"+ dbPath + dbName);
        initTables(conn);
    }

    public Connection getConn() {
        return conn;
    }

    private void initTables(Connection conn) {
        String initTablesQuery = """
                CREATE TABLE IF NOT EXISTS "entries" (
                	"record_name" TEXT NOT NULL,
                	"created" TEXT NOT NULL,
                	"last_updated" TEXT NOT NULL,
                	"sex" TEXT NULL,
                	"dob" TEXT NULL,
                	"body_mass" REAL NULL,
                	"fat_perc" REAL NULL,
                	"lifestyle" TEXT NULL,
                	"condition" TEXT NULL,
                	"target_fat_perc" REAL NULL,
                	"weekly_loss" REAL NULL,
                	"carb_fat_distribution" INTEGER NULL
                )
                ;""";
        try {
            conn.createStatement().execute(initTablesQuery);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    protected void insertEntry(Entry form) {
        String insertQuery = """
                INSERT INTO "entries" (
                    "record_name",
                    "created",
                    "last_updated",
                    "sex",
                    "dob",
                    "body_mass",
                    "fat_perc",
                    "lifestyle",
                    "condition",
                    "target_fat_perc",
                    "weekly_loss",
                    "carb_fat_distribution")
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);""";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setString(1, form.getName());
            preparedStatement.setString(2, LocalDate.now().toString());
            preparedStatement.setString(3, LocalDate.now().toString());
            preparedStatement.setString(4, form.getSex().toString());
            preparedStatement.setString(5, form.getDateOfBirth().toString());
            preparedStatement.setDouble(6, form.getBodyMass());
            preparedStatement.setDouble(7, form.getFatPercentage() / 100);
            preparedStatement.setString(8, form.getLifestyle().toString());
            preparedStatement.setString(9, form.getCondition().toString());
            preparedStatement.setDouble(10, form.getTargetFatPercentage()/100);
            preparedStatement.setDouble(11, form.getWeeklyBMLossPercentage()/100);
            preparedStatement.setInt(12, form.getCarbFat());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected ResultSet selectEntries() throws SQLException {
        String selectQuery = "SELECT * FROM entries";
        return conn.createStatement().executeQuery(selectQuery);
    }
}
