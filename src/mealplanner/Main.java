package mealplanner;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {

        Dotenv dotenv = Dotenv.load();
        final String DB_URL = dotenv.get("DB_URL");
        final String DB_USER = dotenv.get("DB_USER");
        final String DB_PASS = dotenv.get("DB_PASS");

        DBManager dbManager = new DBManager(DB_URL, DB_USER, DB_PASS);

        Scanner scanner = new Scanner(System.in);

        dbManager.createTables();

        UI ui = new UI(scanner, dbManager);
        ui.start();

        dbManager.closeConnection();
    }
}