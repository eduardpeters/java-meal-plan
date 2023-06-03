package mealplanner;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {

        Dotenv dotenv = Dotenv.load();
        final String DB_URL = dotenv.get("DB_URL");
        final String DB_USER = dotenv.get("DB_USER");
        final String DB_PASS = dotenv.get("DB_PASS");

        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        connection.setAutoCommit(true);
        Statement statement = connection.createStatement();

        DBManager dbManager = new DBManager(connection, statement);

        Scanner scanner = new Scanner(System.in);
        ArrayList<Meal> meals = new ArrayList<>();

        dbManager.createTables();
        dbManager.loadMeals(meals);

        UI ui = new UI(scanner, meals, dbManager);
        ui.start();

        statement.close();
        connection.close();
    }
}