package mealplanner;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("What would you like to do (add, show, exit)?");
            String command = scanner.nextLine();

            if (command.equals("exit")) {
                break;
            }
        }

        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        String category = scanner.nextLine();
        System.out.println("Input the meal's name:");
        String name = scanner.nextLine();
        System.out.println("Input the ingredients:");
        String allIngredients = scanner.nextLine();
        String[] ingredients = allIngredients.split(",");

        Meal newMeal = new Meal(category, name, ingredients);

        System.out.println();

        newMeal.printMeal();

        System.out.println("The meal has been added!");
    }
}