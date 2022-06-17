package fr.sae.terraria.modele.entities.player.craft.recipes;

import fr.sae.terraria.modele.entities.player.inventory.Inventory;
import fr.sae.terraria.modele.entities.player.inventory.Stack;


public class RockRecipe
{
    private static final int NB_INGREDIENTS = 1;
    public static final int NB_STONES = 4;


    public static Ingredient apply(final Inventory inventory)
    {
        Ingredient ingredient = new Ingredient(RockRecipe.NB_INGREDIENTS);
        boolean haveEnoughStone;

        int i = 0;
        do {
            Stack stack = inventory.get().get(i);
            haveEnoughStone = IngredientSet.stones(ingredient, stack, RockRecipe.NB_STONES);

            i++;
        } while (i < inventory.get().size() && !haveEnoughStone);

        return haveEnoughStone ? ingredient : null;
    }
}
