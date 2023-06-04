package mealplanner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public class UI {
    private Scanner scanner;
    private DBManager dbManager;
    private Set<String> validCategories = Set.of("breakfast", "lunch", "dinner");

    public UI(Scanner scanner, DBManager dbManager) {
        this.scanner = scanner;
        this.dbManager = dbManager;
    }

    public void start() {
        while (true) {
            System.out.println("What would you like to do (add, show, exit)?");
            String command = scanner.nextLine();

            if (command.equals("exit")) {
                break;
            }

            switch (command) {
                case "add" -> addMeal();
                case "show" -> showMeals();
                default -> {
                }
            }
        }
        System.out.println("Bye!");
    }

    private void addMeal() {
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        String category = scanner.nextLine();
        while (!validCategories.contains(category)) {
            System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            category = scanner.nextLine();
        }
        System.out.println("Input the meal's name:");
        String name = scanner.nextLine();
        while (!isValidInput(name)) {
            System.out.println("Wrong format. Use letters only!");
            name = scanner.nextLine();
        }
        System.out.println("Input the ingredients:");
        String allIngredients;
        String[] ingredients = null;
        int count = 0;
        while (count == 0) {
            allIngredients = scanner.nextLine();
            ingredients = allIngredients.split(",");
            for (String ingredient : ingredients) {
                if (ingredient.isEmpty() || !isValidInput(ingredient.trim())) {
                    System.out.println("Wrong format. Use letters only!");
                    count = 0;
                    break;
                }
                count++;
            }
            if (count != ingredients.length) {
                count = 0;
            }
        }
        String[] trimmedIngredients = new String[count];
        for (int i = 0; i < trimmedIngredients.length; i++) {
            trimmedIngredients[i] = ingredients[i].trim();
        }

        Meal newMeal = new Meal(category, name, trimmedIngredients);
        try {
            dbManager.insertMeal(newMeal);
        } catch (SQLException e) {
            System.out.println("DB Error: " + e.getMessage());
        }

        System.out.println("The meal has been added!");
    }

    private void showMeals() {
        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
        String category = scanner.nextLine();
        while (!validCategories.contains(category)) {
            System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            category = scanner.nextLine();
        }
        ArrayList<Meal> meals;
        try {
            meals = dbManager.getMeals(category);
        } catch (SQLException e) {
            System.out.println("DB Error: " + e.getMessage());
            return;
        }
        if (meals.isEmpty()) {
            System.out.println("No meals found.");
        } else {
            System.out.printf("Category: %s\n", category);
            for (Meal meal : meals) {
                System.out.println();
                meal.printMeal();
            }
            System.out.println();
        }
    }

    private boolean isValidInput(String str) {
        String[] words = str.split(" ");
        for (String word : words) {
            if (!isAlpha(word)) {
                return false;
            }
        }
        return true;
    }

    private boolean isAlpha(String str) {
        return str.matches("[a-zA-Z]+");
    }
}
