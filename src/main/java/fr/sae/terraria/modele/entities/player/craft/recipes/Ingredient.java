package fr.sae.terraria.modele.entities.player.craft.recipes;

import fr.sae.terraria.modele.entities.player.inventory.Stack;


public class Ingredient
{
    private final Stack[] ingredients;


    public Ingredient(int nbIngredient) { this.ingredients = new Stack[nbIngredient]; }

    public Stack[] get() { return this.ingredients; }
}
