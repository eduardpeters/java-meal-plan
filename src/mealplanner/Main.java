package mealplanner;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        String category = scanner.nextLine();
        System.out.println("Input the meal's name:");
        String name = scanner.nextLine();
        System.out.println("Input the ingredients:");
        String allIngredients = scanner.nextLine();
        String[] ingredients = allIngredients.split(",");

        System.out.println("");

        System.out.println("Category: " + category);
        System.out.println("Name: " + name);
        System.out.println("Ingredients:");
        for (int i = 0; i < ingredients.length; i++) {
            System.out.println(ingredients[i]);
        }
        System.out.println("The meal has been added!");
    }
}