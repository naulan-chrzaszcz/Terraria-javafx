package fr.sae.terraria.modele.entities.player.craft.recipes;

import fr.sae.terraria.modele.entities.player.inventory.Inventory;
import fr.sae.terraria.modele.entities.player.inventory.Stack;


public class TorchRecipe
{
    private static final int NB_INGREDIENTS = 2;
    public static final int NB_COALS = 1;
    public static final int NB_STICKS = 1;


    public static Ingredient apply(final Inventory inventory)
    {
        Ingredient ingredient = new Ingredient(TorchRecipe.NB_INGREDIENTS);
        boolean haveEnoughCoal = false;
        boolean haveEnoughSticks = false;

        int i = 0;
        do {
            Stack stack = inventory.get().get(i);

            if (!haveEnoughCoal)
                haveEnoughCoal = IngredientSet.coals(ingredient, stack, TorchRecipe.NB_COALS);
            if (!haveEnoughSticks)
                haveEnoughSticks = IngredientSet.sticks(ingredient, stack, TorchRecipe.NB_COALS);
        } while (i < inventory.get().size() && (!haveEnoughCoal || !haveEnoughSticks));

        return (haveEnoughCoal && haveEnoughSticks) ? ingredient : null;
    }
}
