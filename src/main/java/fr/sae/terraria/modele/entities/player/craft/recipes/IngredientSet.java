package fr.sae.terraria.modele.entities.player.craft.recipes;

import fr.sae.terraria.modele.entities.items.Item;
import fr.sae.terraria.modele.entities.player.inventory.Stack;


public class IngredientSet
{



    public static boolean sticks(final Ingredient ingredient, final Stack stack, final int nbSticks)
    {
        if (Item.isStick(stack.getItem()) && stack.haveEnoughQuantity(nbSticks)) {
            ingredient.get()[ingredient.nbStacks()] = stack;
            return true;
        }
        return false;
    }

    public static boolean woods(final Ingredient ingredient, final Stack stack, final int nbWood)
    {
        if (Item.isWood(stack.getItem()) && stack.haveEnoughQuantity(nbWood)) {
            ingredient.get()[ingredient.nbStacks()] = stack;
            return true;
        }
        return false;
    }

    public static boolean stones(final Ingredient ingredient, final Stack stack, final int nbStones)
    {
        if (Item.isStone(stack.getItem()) && stack.haveEnoughQuantity(nbStones)) {
            if (ingredient.get().length == 1)
                ingredient.get()[0] = stack;
            else ingredient.get()[ingredient.nbStacks()] = stack;

            return true;
        }
        return false;
    }

    public static boolean irons(final Ingredient ingredient, final Stack stack, final int nbStones)
    {
        if (Item.isIron(stack.getItem()) && stack.haveEnoughQuantity(nbStones)) {
            ingredient.get()[ingredient.nbStacks()] = stack;
            return true;
        }
        return false;
    }
}
