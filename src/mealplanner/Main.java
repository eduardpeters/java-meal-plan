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
        while (!isAlpha(name)) {
            System.out.println("Wrong format. Use letters only!");
            name = scanner.nextLine();
        }
        System.out.println("Input the ingredients:");
        String allIngredients = scanner.nextLine();
        String[] ingredients = allIngredients.split(",");

        meals.add(new Meal(category, name, ingredients));

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
        }
    }

    public static boolean isAlpha(String str) {
        return str.matches("[a-zA-Z]+");
    }
}