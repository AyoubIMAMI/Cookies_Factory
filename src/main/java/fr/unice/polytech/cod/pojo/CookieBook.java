package fr.unice.polytech.cod.pojo;

import fr.unice.polytech.cod.exceptions.CookieAlreadyExistingException;
import fr.unice.polytech.cod.exceptions.NotMatchingCatalogRequirementException;
import fr.unice.polytech.cod.food.Cookie;
import fr.unice.polytech.cod.food.ingredient.Cooking;
import fr.unice.polytech.cod.food.ingredient.Ingredient;
import fr.unice.polytech.cod.food.ingredient.Mix;
import fr.unice.polytech.cod.store.Store;

import java.util.ArrayList;
import java.util.List;

public class CookieBook {
    private final List<Cookie> cookies;
    private final IngredientCatalog ingredientCatalog = IngredientCatalog.instance;

    public CookieBook() {
        cookies = new ArrayList<>();

        cookies.add(new Cookie("Cookie au chocolat",
                ingredientCatalog.getDough("chocolate"),
                ingredientCatalog.getFlavour("chili"),
                List.of(ingredientCatalog.getTopping("milk chocolate"), ingredientCatalog.getTopping("M&M’s")),
                new Mix(Mix.MixState.MIXED),
                new Cooking(Cooking.CookingState.CHEWY),
                10));

        cookies.add(new Cookie("Cookie à la vanille",
                ingredientCatalog.getDough("plain"),
                ingredientCatalog.getFlavour("vanilla"),
                List.of(ingredientCatalog.getTopping("milk chocolate"), ingredientCatalog.getTopping("white chocolate")),
                new Mix(Mix.MixState.MIXED),
                new Cooking(Cooking.CookingState.CHEWY),
                5));

        cookies.add(new Cookie("Cookie à la pistache",
                ingredientCatalog.getDough("peanut butter"),
                ingredientCatalog.getFlavour("chili"),
                List.of(ingredientCatalog.getTopping("milk chocolate")),
                new Mix(Mix.MixState.MIXED),
                new Cooking(Cooking.CookingState.CHEWY),
                10));

    }

    /**
     * return the availables cookies depend from the store
     *
     * @param store
     * @return a list of cookies
     */
    public List<Cookie> getAvailableCookie(Store store) {
        List<Cookie> cookieAvailable = new ArrayList<>();
        for (Cookie cookie : cookies) {
            if (store.hasEnoughIngredients(cookie.getIngredients()))
                cookieAvailable.add(cookie);
        }
        if(store.hasPartyChef()){
            //cookieAvailable.addAll(store.getPartyCookies());
        }
        return cookieAvailable;
    }

    /**
     * Find a Dough with a cookie name in the book
     *
     * @param cookieName The name of the cookie
     * @return The Cookie with the given name
     */
    public Cookie getCookie(String cookieName) {
        for (Cookie cookie : this.cookies) {
            if (cookie.getName().equals(cookieName)) {
                return new Cookie(cookie);
            }
        }
        return null;
    }

    /**
     * Add a cookie to the cookie book if it's not in the book and all the ingredients are in the catalog
     *
     * @param cookie The given cookie
     */
    public void addCookieRecipe(Cookie cookie) throws NotMatchingCatalogRequirementException, CookieAlreadyExistingException {
        if (!this.cookies.contains(cookie)) {
            for (Ingredient ingredient : cookie.getIngredients()) {
                if (!ingredientCatalog.isInCatalog(ingredient))
                    throw new NotMatchingCatalogRequirementException();
            }
            this.cookies.add(cookie);
        } else {
            throw new CookieAlreadyExistingException();
        }
    }

    /**
     * Remove a cookie to the cookie book
     *
     * @param cookieToRemove Yhe given cookie
     */
    public void removeCookieRecipe(Cookie cookieToRemove) {
        for (Cookie cookie1 : this.cookies) {
            if (cookieToRemove.equals(cookie1)) {
                cookies.remove(cookieToRemove);
                break;
            }
        }
    }

    public List<Cookie> getCookies() {
        return cookies;
    }
}