package fr.unice.polytech.cod.components;

import fr.unice.polytech.cod.food.Cookie;
import fr.unice.polytech.cod.food.ingredient.Ingredient;
import fr.unice.polytech.cod.interfaces.*;
import fr.unice.polytech.cod.order.Order;
import fr.unice.polytech.cod.order.OrderState;
import fr.unice.polytech.cod.pojo.CookieBook;
import fr.unice.polytech.cod.schedule.TimeClock;
import fr.unice.polytech.cod.store.Chef;
import fr.unice.polytech.cod.store.Store;
import fr.unice.polytech.cod.store.SurpriseBasket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class StoreComponent implements StoreModifier, StoreAccessor {
    private final ChefAction chefAction;
    private final StockExplorer stockExplorer;
    private final StoreFinder storeFinder;
    private final CookieBookManager cookieBookManager;

    @Autowired
    public StoreComponent(ChefAction chefAction, StockExplorer stockExplorer, StoreFinder storeFinder,CookieBookManager cookieBookManager) {
        this.chefAction = chefAction;
        this.stockExplorer = stockExplorer;
        this.storeFinder = storeFinder;
        this.cookieBookManager = cookieBookManager;
    }

    @Override
    public void changeOpeningHour(Store store, TimeClock open, TimeClock close) {
        store.setOpenHour(open);
        store.setCloseHour(close);
        for (Chef chef: store.getListChef()){
            chefAction.updateSchedule(chef,store);
        }
    }

    @Override
    public List<Cookie> getAvailableCookie(Store store) {
        List<Cookie> cookieAvailable = new ArrayList<>();

        store.getCookieBook().getCookies().stream()
                .filter(cookie -> stockExplorer.hasEnoughIngredients(store.getStock(), cookie.getIngredients()))
                .forEach(cookieAvailable::add);
        return cookieAvailable;
    }

    @Override
    public void addChef(Store store,Chef chef){
        store.getListChef().add(chef);
    }

    @Override
    public void addCookieStore(Store store, Cookie cookie, String storeName) throws Exception {
        Store store1 = this.storeFinder.findStore(storeName);
        cookieBookManager.addCookieRecipe(store1.getCookieBook(), cookie);
    }

    @Override
    public void removeCookieStore(Store store, Cookie cookie, String storeName) throws Exception {
        Store store1 = this.storeFinder.findStore(storeName);
        cookieBookManager.removeCookieRecipe(store1.getCookieBook(), cookie);
    }

    @Override
    public boolean setTax(Store store, String ingredientName, double newPrice) {
        Optional<Ingredient> _stockIngredient = stockExplorer.findIngredient(store.getStock(), ingredientName);
        if(_stockIngredient.isEmpty())
            return false;

        _stockIngredient.get().setPrice(newPrice);
        return true;
    }

    @Override
    public List<SurpriseBasket> getAllSurpriseBasket(Store store) {
        List<SurpriseBasket> surpriseBaskets = new ArrayList<>();
        getObsoleteOrders(store).forEach(obsoleteOrder -> surpriseBaskets.add(new SurpriseBasket(obsoleteOrder)));
        return surpriseBaskets;
    }

    @Override
    public List<Order> getObsoleteOrders(Store store) {
        return store.getOrderList().stream()
                .filter(order -> order.getOrderState() == OrderState.OBSOLETE)
                .toList();
    }
}
