package fr.sae.terraria.modele.entities.player.craft.recipes;

import fr.sae.terraria.modele.entities.player.inventory.Stack;

import java.util.Objects;


public class Ingredient
{
    private final Stack[] ingredients;


    public Ingredient(final int nbIngredient) { this.ingredients = new Stack[nbIngredient]; }

    public int nbStacks()
    {
        Stack stack;
        int i = 0;
        do {
            stack = ingredients[i];
            i++;
        } while (i < ingredients.length && !Objects.isNull(stack));

        return i;
    }


    public Stack[] get() { return this.ingredients; }
}
