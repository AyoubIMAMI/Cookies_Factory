package fr.unice.polytech.cod.components;

import fr.unice.polytech.cod.exceptions.InvalidStoreException;
import fr.unice.polytech.cod.food.Cookie;
import fr.unice.polytech.cod.interfaces.CartActions;
import fr.unice.polytech.cod.interfaces.StoreFinder;
import fr.unice.polytech.cod.interfaces.UserAction;
import fr.unice.polytech.cod.order.Bill;
import fr.unice.polytech.cod.order.Order;
import fr.unice.polytech.cod.order.OrderState;
import fr.unice.polytech.cod.pojo.Item;
import fr.unice.polytech.cod.schedule.Interval;
import fr.unice.polytech.cod.store.Store;
import fr.unice.polytech.cod.user.Cart;
import fr.unice.polytech.cod.user.User;
import fr.unice.polytech.cod.user.fidelityAccount.Discount;
import fr.unice.polytech.cod.user.fidelityAccount.FidelityAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class UserActionComponent implements UserAction {

    @Autowired
    CartActions cartActions;
    StoreFinder storeFinder = StoreFinderComponent.getInstance();

    public UserActionComponent(){}
    /**
     * Add cookies to cart
     *
     * @param cookie The cookie to add to the cart
     * @param quantity The quantity of the selected cookie
     */
    @Override
    public boolean chooseCookies(User user, Cookie cookie, int quantity, Cart cart) {
        return cartActions.addToCart(cart, new Item(cookie, quantity));
    }

    /**
     * choose the store for his current order
     *
     * @param name
     * @return
     * @throws InvalidStoreException
     */
    @Override
    public Store selectStore(String name, Cart cart) throws InvalidStoreException {
        Store store = storeFinder.findStore(name);
        cart.setStore(store);
        return (store);    }

    @Override
    public void chooseInterval(User user, Interval interval, Cart cart) {
        interval.reserve();
        cart.setInterval(interval);
    }

    @Override
    public Bill validateCart(User user, Cart cart) throws Exception {
        Instant time = Instant.now();
        if (!cart.isEmpty() && !cart.isTherePenalty(time))
            return cartActions.validate(cart, user);
        else
            throw new Exception("Panier vide impossible de le valider");    }

    @Override
    public void addOrder(User user, Order order) {
        user.getOrders().add(order);
        FidelityAccount fidelityAccount = user.getFidelityAccount();
        if (fidelityAccount != null)
            fidelityAccount.addOrder(order);
    }

    @Override
    public void removeOneItemFromCart(User user, Item item, Cart cart) {
        cartActions.removeFromCart(cart, item);
    }

    @Override
    public void subscribeToFidelityAccount(User user, String name, String email, String password) {
        user.setFidelityAccount(new FidelityAccount(name, email, password));
    }

    @Override
    public void useDiscount(User user, Order order) {
        FidelityAccount fidelityAccount = user.getFidelityAccount();
        if(fidelityAccount == null)
            return;
        Optional<Discount> _discount = fidelityAccount.getDiscount();
        if(_discount.isEmpty())
            return;

        Discount discount = _discount.get();
        order.setDiscount(discount);
        fidelityAccount.resetDiscount();
    }

    @Override
    public boolean cancelOrder(User user, Order order) {

        if (userOrders.contains(order) && order.getOrderState().equals(OrderState.PENDING)) {
            cart.cancelOrder(order);
            return true; //Your order has been canceled
        } else
            return false; //Your order is already in progress. You cannot canceled it

    }


}