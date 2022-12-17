package fr.unice.polytech.cod.interfaces;

import fr.unice.polytech.cod.food.PartyCookie;
import fr.unice.polytech.cod.pojo.ingredient.*;

import java.util.HashMap;

public interface IPartyCookieHandler {
    void customize(PartyCookie partyCookie,HashMap<Ingredient, Boolean> customIngrdedients);
}
