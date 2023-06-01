package mealplanner;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Meal> meals = new ArrayList<>();

        while (true) {
            System.out.println("What would you like to do (add, show, exit)?");
            String command = scanner.nextLine();

            if (command.equals("exit")) {
                break;
            }

            switch (command) {
                case "add" -> addMeal(scanner, meals);
                case "show" -> showMeals(meals);
                default -> {
                }
            }
        }
        System.out.println("Bye!");
    }

    public static void addMeal(Scanner scanner, ArrayList<Meal> meals) {
        Set<String> validCategories = Set.of("breakfast", "lunch", "dinner");

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

        meals.add(new Meal(category, name, trimmedIngredients));

        System.out.println("The meal has been added!");
    }

    public static void showMeals(ArrayList<Meal> meals) {
        if (meals.isEmpty()) {
            System.out.println("No meals saved. Add a meal first.");
        } else {
            for (Meal meal : meals) {
                System.out.println();
                meal.printMeal();
            }
            System.out.println();
        }
    }

    public static boolean isValidInput(String str) {
        String[] words = str.split(" ");
        for (String word : words) {
            if (!isAlpha(word)) {
                return false;
            }
        }
        return true;
    }
    public static boolean isAlpha(String str) {
        return str.matches("[a-zA-Z]+");
    }
}