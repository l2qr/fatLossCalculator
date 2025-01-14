package skwira.marcin.fatlosscalculator;

import lombok.Getter;

import java.sql.*;
import java.time.LocalDate;

@Getter
public class DatabaseController {

    protected Connection conn;

    DatabaseController(String dbName) throws Exception {
        String dbPath = "./";
        conn = DriverManager.getConnection("jdbc:sqlite:"+ dbPath + dbName);
        initTables(conn);
    }

    private void initTables(Connection conn) {
        String query = """
                CREATE TABLE IF NOT EXISTS "entries" (
                    "id" INTEGER PRIMARY KEY,
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
                	"carb_fat_distribution" REAL NULL
                )
                ;""";
        try {
            conn.createStatement().execute(query);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    protected void insertEntry(Entry form) {
        String query = """
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
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, form.getName());
            preparedStatement.setString(2, LocalDate.now().toString());
            preparedStatement.setString(3, LocalDate.now().toString());
            preparedStatement.setString(4, form.getSex().toString());
            preparedStatement.setString(5, form.getDateOfBirth().toString());
            preparedStatement.setDouble(6, form.getBodyMass());
            preparedStatement.setDouble(7, form.getFatPercentage());
            preparedStatement.setString(8, form.getLifestyle().toString());
            preparedStatement.setString(9, form.getCondition().toString());
            preparedStatement.setDouble(10, form.getTargetFatPercentage());
            preparedStatement.setDouble(11, form.getWeeklyBMLossPercentage());
            preparedStatement.setDouble(12, form.getCarbFat());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void updateEntry(Entry entry) {
        String query = """
                UPDATE "entries" SET
                    "record_name"=?,
                    "last_updated"=?,
                    "sex"=?,
                    "dob"=?,
                    "body_mass"=?,
                    "fat_perc"=?,
                    "lifestyle"=?,
                    "condition"=?,
                    "target_fat_perc"=?,
                    "weekly_loss"=?,
                    "carb_fat_distribution"=?
                WHERE "id"=?;""";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, entry.getName());
            preparedStatement.setString(2, LocalDate.now().toString());
            preparedStatement.setString(3, entry.getSex().toString());
            preparedStatement.setString(4, entry.getDateOfBirth().toString());
            preparedStatement.setDouble(5, entry.getBodyMass());
            preparedStatement.setDouble(6, entry.getFatPercentage());
            preparedStatement.setString(7, entry.getLifestyle().toString());
            preparedStatement.setString(8, entry.getCondition().toString());
            preparedStatement.setDouble(9, entry.getTargetFatPercentage());
            preparedStatement.setDouble(10, entry.getWeeklyBMLossPercentage());
            preparedStatement.setDouble(11, entry.getCarbFat());
            preparedStatement.setInt(12, entry.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void deleteEntry(int id) {
        String query = "DELETE FROM entries WHERE \"id\"=?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);
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
