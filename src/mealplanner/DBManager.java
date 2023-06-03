package mealplanner;

import java.sql.*;
import java.util.ArrayList;

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

    public void loadMeals(ArrayList<Meal> meals) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM meals");
        while (rs.next()) {
            // Placeholder for loading
            System.out.println("id = " + rs.getInt("meal_id"));
        }
        rs.close();
    }

    public void insertMeal(Meal meal) throws SQLException {
        // First determine last id in meals
        ResultSet rs = statement.executeQuery("""
            SELECT * FROM meals
            ORDER BY meal_id DESC
            LIMIT 1;""");
        int lastMealID = 0;
        if (rs.next()) {
            lastMealID = rs.getInt("meal_id");
        }
        rs.close();
        PreparedStatement st = connection.prepareStatement("""
                INSERT INTO meals (meal_id, category,meal)
                VALUES (?,?,?)""");
        st.setInt(1, ++lastMealID);
        st.setString(2, meal.getCategory());
        st.setString(3, meal.getName());
        st.executeUpdate();
        st.close();
        // First determine last id in ingredients
        rs = statement.executeQuery("""
            SELECT * FROM ingredients
            ORDER BY ingredient_id DESC
            LIMIT 1;""");
        int lastIngredientID = 0;
        if (rs.next()) {
            lastIngredientID = rs.getInt("ingredient_id");
        }
        rs.close();
        st = connection.prepareStatement("""
                INSERT INTO ingredients (ingredient_id, ingredient, meal_id)
                VALUES (?,?,?)""");
        st.setInt(3, lastMealID);
        for (String ingredient : meal.getIngredients()) {
            st.setInt(1, ++lastIngredientID);
            st.setString(2, ingredient);
            st.executeUpdate();
        }
        st.close();
    }
}
