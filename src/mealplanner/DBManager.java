package mealplanner;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
    private Connection connection;
    private Statement statement;

    public DBManager(Connection connection, Statement statement) {
        this.connection = connection;
        this.statement = statement;
    }

    public void createTables() throws SQLException {
        statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS meals (
                    meal_id integer,
                    category varchar(50),
                    meal varchar(255)
                );""");
        statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS ingredients (
                    ingredient_id integer,
                    ingredient varchar(255),
                    meal_id integer
                );""");
    }
}
