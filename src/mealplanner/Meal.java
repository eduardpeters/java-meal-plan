package mealplanner;

public class Meal {
    private String category;
    private String name;
    private String[] ingredients;

    public Meal(String category, String name, String[] ingredients) {
        this.category = category;
        this.name = name;
        this.ingredients = ingredients;
    }

    public void printMeal() {
        System.out.println("Category: " + this.category);
        System.out.println("Name: " + this.name);
        System.out.println("Ingredients:");
        for (String ingredient : this.ingredients) {
            System.out.println(ingredient);
        }
    }
}
