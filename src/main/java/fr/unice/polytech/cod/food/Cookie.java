package fr.unice.polytech.cod.food;

import fr.unice.polytech.cod.food.ingredient.*;
import fr.unice.polytech.cod.store.Store;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Cookie {
    private final String name;
    private final Dough dough;
    private final Flavour flavour;
    private final List<Topping> toppingList; // TODO : Maybe topping should be represented as a set
    Mix mix;
    Cooking cooking;

    private final int preparationTime; // en minutes

    public Cookie(String cookieName, Dough dough, Flavour flavour, List<Topping> toppingList, Mix mix, Cooking cooking, int preparationTime) {
        this.name = cookieName;
        this.dough = dough;
        this.flavour = flavour;
        this.toppingList = toppingList; //TODO: Make sure the given topping list isn't > 3
        this.mix = mix;
        this.cooking = cooking;
        this.preparationTime = preparationTime;
    }

    /**
     * Give the set of all ingredients needed to produce one cookie base on its recipe.
     *
     * @return The set of all ingredients needed.
     */
    public Set<Ingredient> getIngredients() {
        Set<Ingredient> ingredients = new HashSet<>();

        if (dough != null) ingredients.add(dough);
        if (flavour != null) ingredients.add(flavour);
        ingredients.addAll(toppingList);
        return ingredients;
    }

    public String getName() {
        return name;
    }

    public Dough getDough() {
        return dough;
    }

    public Flavour getFlavour() {
        return flavour;
    }

    public List<Topping> getToppingList() {
        return toppingList;
    }

    public double getPriceByStore(Store store) {
        Map<Ingredient, Double> taxes = store.getTaxes();
        double price = dough.getQuantity() * dough.getPrice() + taxes.get(dough);
        if(flavour != null) price += flavour.getQuantity() * flavour.getPrice() + taxes.get(flavour);
        if(!toppingList.isEmpty()) {
            for(Topping topping : toppingList)
                price += topping.getQuantity() * topping.getPrice() +taxes.get(topping);
        }
        return price;
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "cookieName='" + name + '\'' +
                ", dough=" + dough +
                ", flavour=" + flavour +
                ", toppingList=" + toppingList +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;

        // Check for the instance
        if (!(object instanceof Cookie c))
            return false;

        // Check for the name equality
        return c.name.equals(name);
    }

    public int getPreparationTime() {
        return preparationTime;
    }
}