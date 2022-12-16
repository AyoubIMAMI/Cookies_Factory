package fr.unice.polytech.cod.components;

import fr.unice.polytech.cod.exceptions.CookieAlreadyExistingException;
import fr.unice.polytech.cod.exceptions.NotMatchingCatalogRequirementException;
import fr.unice.polytech.cod.food.Cookie;
import fr.unice.polytech.cod.food.ingredient.Ingredient;
import fr.unice.polytech.cod.interfaces.CatalogExplorer;
import fr.unice.polytech.cod.interfaces.CookieBookManager;
import fr.unice.polytech.cod.interfaces.StockExplorer;
import fr.unice.polytech.cod.pojo.CookieBook;
import fr.unice.polytech.cod.pojo.IngredientCatalog;
import fr.unice.polytech.cod.store.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CookieBookComponent implements CookieBookManager {
    private CatalogExplorer catalogExplorer;

    @Autowired
    public CookieBookComponent(CatalogExplorer catalogExplorer){
        this.catalogExplorer = catalogExplorer;
    }

    @Override
    public void addCookieRecipe(CookieBook cookieBook, Cookie cookie) throws CookieAlreadyExistingException, NotMatchingCatalogRequirementException {
        if (!cookieBook.getCookies().contains(cookie)) {
            for (Ingredient ingredient : cookie.getIngredients()) {
                if(!catalogExplorer.isInCatalog(IngredientCatalog.instance, ingredient))
                    throw new NotMatchingCatalogRequirementException();
            }
            cookieBook.getCookies().add(cookie);
        } else {
            throw new CookieAlreadyExistingException();
        }
    }

    @Override
    public void removeCookieRecipe(CookieBook cookieBook, Cookie cookieToRemove) {
        cookieBook.getCookies().removeIf(cookie -> cookie.equals(cookieToRemove));
    }

    @Override
    public List<Cookie> getCookies(CookieBook cookieBook) {
        return null;
    }

    @Override
    public Cookie getCookie(CookieBook cookieBook, String cookieName) {
        for (Cookie cookie : cookieBook.getCookies()) {
            if (cookie.getName().equals(cookieName)) {
                return new Cookie(cookie);
            }
        }
        return null;
    }
}
