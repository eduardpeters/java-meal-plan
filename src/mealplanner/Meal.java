package mealplanner;

public class Meal {
    private String category;
    private String name;
    private String[] ingredients;
    private int id;

    public Meal(String category, String name, String[] ingredients) {
        this.category = category;
        this.name = name;
        this.ingredients = ingredients;
    }

    public Meal(int id, String category, String name, String[] ingredients) {
        this(category, name, ingredients);
        this.id = id;
    }

    public String getCategory() {
        return this.category;
    }

    public String getName() {
        return this.name;
    }

    public String[] getIngredients() {
        return this.ingredients;
    }

    public int getID() { return this.id; }

    public void printFullMeal() {
        System.out.println("Category: " + this.category);
        printMeal();
    }

    public void printMeal() {
        System.out.println("Name: " + this.name);
        System.out.println("Ingredients:");
        for (String ingredient : this.ingredients) {
            System.out.println(ingredient);
        }
    }
}
