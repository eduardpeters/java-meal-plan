package mealplanner;

import java.sql.*;
import java.util.ArrayList;

public class DBManager {
    private Connection connection;

    public DBManager(String DB_URL, String DB_USER, String DB_PASS) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        connection.setAutoCommit(true);
        this.connection = connection;
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
    }

    public void createTables() throws SQLException {
        Statement statement = connection.createStatement();
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
        statement.close();
    }

    public void loadMeals(ArrayList<Meal> meals) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rsMeal = statement.executeQuery("SELECT * FROM meals");
        PreparedStatement st = connection.prepareStatement("""
                SELECT * FROM ingredients WHERE meal_id = ?;""");
        while (rsMeal.next()) {
            st.setInt(1, rsMeal.getInt("meal_id"));
            ResultSet rsIngredients = st.executeQuery();
            ArrayList<String> ingredients = new ArrayList<>();
            while (rsIngredients.next()) {
                ingredients.add(rsIngredients.getString("ingredient"));
            }
            rsIngredients.close();
            String category = rsMeal.getString("category");
            String mealName = rsMeal.getString("meal");
            String[] ingredientsArray = new String[ingredients.size()];
            meals.add(new Meal(category, mealName, ingredients.toArray(ingredientsArray)));
        }
        rsMeal.close();
        st.close();
        statement.close();
    }

    public void insertMeal(Meal meal) throws SQLException {
        Statement statement = connection.createStatement();
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
        statement.close();
    }
}