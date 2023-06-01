package mealplanner;

import java.util.ArrayList;
import java.util.Scanner;

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

    }

    public static void addMeal(Scanner scanner, ArrayList<Meal> meals) {
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        String category = scanner.nextLine();
        System.out.println("Input the meal's name:");
        String name = scanner.nextLine();
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
}