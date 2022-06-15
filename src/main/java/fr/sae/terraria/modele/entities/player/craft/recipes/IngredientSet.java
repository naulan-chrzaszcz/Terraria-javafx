package fr.sae.terraria.modele.entities.player.craft.recipes;

import fr.sae.terraria.modele.entities.items.Item;
import fr.sae.terraria.modele.entities.player.inventory.Stack;


public class IngredientSet
{


    private static boolean putStackIntoIngredientsList(final Ingredient ingredient, final Stack stack)
    {
        if (ingredient.get().length == 1)
            ingredient.get()[0] = stack;
        else ingredient.get()[ingredient.nbStacks()-1] = stack;

        return true;
    }

    public static boolean sticks(final Ingredient ingredient, final Stack stack, final int nbSticks)
    {
        boolean haveEnoughSticks = Item.isStick(stack.getItem()) && stack.haveEnoughQuantity(nbSticks);
        return haveEnoughSticks && IngredientSet.putStackIntoIngredientsList(ingredient, stack);
    }

    public static boolean woods(final Ingredient ingredient, final Stack stack, final int nbWood)
    {
        boolean haveEnoughWoods = Item.isWood(stack.getItem()) && stack.haveEnoughQuantity(nbWood);
        return haveEnoughWoods && IngredientSet.putStackIntoIngredientsList(ingredient, stack);
    }

    public static boolean stones(final Ingredient ingredient, final Stack stack, final int nbStones)
    {
        boolean haveEnoughStones = Item.isStone(stack.getItem()) && stack.haveEnoughQuantity(nbStones);
        return haveEnoughStones && IngredientSet.putStackIntoIngredientsList(ingredient, stack);
    }

    public static boolean irons(final Ingredient ingredient, final Stack stack, final int nbStones)
    {
        boolean haveEnoughIrons = Item.isIron(stack.getItem()) && stack.haveEnoughQuantity(nbStones);
        return haveEnoughIrons && IngredientSet.putStackIntoIngredientsList(ingredient, stack);
    }
}
