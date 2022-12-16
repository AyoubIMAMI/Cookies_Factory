package fr.unice.polytech.cod.interfaces;

import fr.unice.polytech.cod.order.Order;
import fr.unice.polytech.cod.pojo.Item;
import fr.unice.polytech.cod.food.ingredient.Ingredient;
import fr.unice.polytech.cod.order.Bill;
import fr.unice.polytech.cod.user.Cart;
import fr.unice.polytech.cod.user.User;
import java.util.Set;

public interface CartActions {

    /**
     * If the store as the ingredients, add an item to the cart
     *
     * @param cart - The cart.
     * @param item - The item to add.
     * @return boolean - If the item is add to the cart
     */
    boolean addToCart(Cart cart, Item item);

    /**
     * Remove the given item quantity from the quantity of the item, if the item is present as one copy, then it is deleted.
     *
     * @param cart - The cart.
     * @param item - The item present in the cart.
     */
    Boolean removeFromCart(Cart cart, Item item);

    /**
     * Validate the cart and create an order, that is added to the user and the sore.
     * The cart is validated only if the store has all the ingredients needed
     *
     * @param cart - The cart.
     * @param user - User of the cart.
     * @return The bill of the validate order.
     * @throws Exception - If not has enough ingredients
     */
    Bill validate(Cart cart, User user) throws Exception;

    /**
     * Cancel the client order.
     *
     * @param cart - The cart.
     * @param order - Order to cancel.
     */
    void cancelOrder(Cart cart, Order order);

    /**
     * Take a list of ingredients and return a set of the ingredients needed.
     *
     * @param cart - The cart.
     * @param items - list of ingredients.
     * @return a set of the ingredients needed
     */
    Set<Ingredient> generateIngredientsNeeded(Cart cart, Set<Item> items);

    /**
     * Find a item in a cart.
     *
     * @param cart - The cart.
     * @param cookieName - A cookie name.
     * @return The cookie.
     * @throws Exception if the cookie doesn't in the cart.
     */
    Item findItem(Cart cart, String cookieName) throws Exception;

    /**
     * Get the quantity of the specific item in the cart.
     *
     * @param cart - The cart.
     * @param itemName - The item we are looking for.
     * @return the quantities of the item.
     */
    int getItemQuantity(Cart cart, String itemName);


    /**
     * Calculated the time to cook the cookie of the cart.
     *
     * @param cart - The cart.
     * @return the duration to cook the cookie of the cart.
     */
    int getDuration(Cart cart);

    /**
     * Add a item in the cart.
     *
     * @param cart - The cart
     * @param item - The item to add to the cart.
     */
    void add(Cart cart, Item item);

    /**
     * Is cart empty.
     *
     * @param cart - The cart.
     * @return if the cart is empty.
     */
    boolean isEmpty(Cart cart);

    /**
     * Show all the cookies in our order and give the choice to validate or add/delete more cookies.
     */
    void showCart(Cart cart);
}
