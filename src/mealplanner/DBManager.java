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
        statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS plan (
                    day varchar(10),
                    category varchar(50),
                    meal_id integer
                );""");
        statement.close();
    }

    public ArrayList<Meal> getMeals(String category) throws SQLException {
        ArrayList<Meal> meals = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM meals WHERE category = ?;");
        statement.setString(1, category);
        ResultSet rsMeal = statement.executeQuery();
        PreparedStatement st = connection.prepareStatement("""
                SELECT * FROM ingredients WHERE meal_id = ?;""");
        while (rsMeal.next()) {
            int mealID = rsMeal.getInt("meal_id");
            ArrayList<String> ingredients = getMealIngredients(mealID);
            String mealCategory = rsMeal.getString("category");
            String mealName = rsMeal.getString("meal");
            String[] ingredientsArray = new String[ingredients.size()];
            meals.add(new Meal(mealID, mealCategory, mealName, ingredients.toArray(ingredientsArray)));
        }
        rsMeal.close();
        st.close();
        statement.close();
        return meals;
    }

    public ArrayList<String> getMealIngredients(int meal_id) throws SQLException {
        ArrayList<String> ingredients = new ArrayList<>();
        PreparedStatement st = connection.prepareStatement("""
                SELECT * FROM ingredients WHERE meal_id = ?;""");
        st.setInt(1, meal_id);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            ingredients.add(rs.getString("ingredient"));
        }
        rs.close();
        st.close();
        return ingredients;
    }

    public ArrayList<String> getOptions(String category) throws SQLException {
        ArrayList<String> options = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM meals WHERE category = ? ORDER BY meal;");
        statement.setString(1, category);
        ResultSet rsOptions = statement.executeQuery();
        while (rsOptions.next()) {
            options.add(rsOptions.getString("meal"));
        }
        rsOptions.close();
        statement.close();
        return options;
    }

    public String getPlannedMeal(String day, String category) throws SQLException {
        String name = "";
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM plan WHERE day = ? AND category = ?");
        statement.setString(1, day);
        statement.setString(2, category);
        ResultSet rsPlan = statement.executeQuery();
        while (rsPlan.next()) {
            PreparedStatement st = connection.prepareStatement("SELECT meal FROM meals WHERE meal_id = ?;");
            st.setInt(1, rsPlan.getInt("meal_id"));
            ResultSet rsMeals = st.executeQuery();
            while (rsMeals.next()) {
                name = rsMeals.getString("meal");
            }
            rsMeals.close();
            st.close();
        }
        rsPlan.close();
        statement.close();
        return name;
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

    public void insertPlannedMeal(String day, String category, String meal) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT meal_id from meals WHERE meal = ? LIMIT 1;");
        statement.setString(1, meal);
        ResultSet rsMeals = statement.executeQuery();
        PreparedStatement st = connection.prepareStatement("""
                INSERT INTO plan (day, category, meal_id)
                VALUES (?, ?, ?)""");
        while (rsMeals.next()) {
            st.setString(1, day);
            st.setString(2, category);
            st.setInt(3, rsMeals.getInt("meal_id"));
            st.executeUpdate();
        }
        rsMeals.close();
        st.close();
        statement.close();
    }

    public ArrayList<Integer> getPlannedMealIds() throws SQLException {
        ArrayList<Integer> mealIds = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT meal_id FROM plan;");
        while (rs.next()) {
            mealIds.add(rs.getInt("meal_id"));
        }
        rs.close();
        statement.close();
        return mealIds;
    }

    public void clearPlan() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("TRUNCATE TABLE plan");
        statement.close();
    }
}
