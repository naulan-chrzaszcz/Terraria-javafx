package fr.sae.terraria.modele.entities.player.craft.recipes;

import fr.sae.terraria.modele.entities.player.inventory.Inventory;
import fr.sae.terraria.modele.entities.player.inventory.Stack;


public class PickaxeRecipe
{
    private static final int NB_INGREDIENTS = 2;
    public static final int NB_STICKS = 2;


    public static final class WoodRecipe
    {
        public static final int NB_WOODS = 3;


        public static Ingredient apply(final Inventory inventory)
        {
            Ingredient ingredient = new Ingredient(PickaxeRecipe.NB_INGREDIENTS);
            boolean haveEnoughForPickaxeHandler = false;
            boolean haveEnoughForPickaxeHead = false;

            int i = 0;
            do {
                Stack stack = inventory.get().get(i);

                if (!haveEnoughForPickaxeHead)
                    haveEnoughForPickaxeHead = IngredientSet.woods(ingredient, stack, NB_WOODS);
                if (!haveEnoughForPickaxeHandler)
                    haveEnoughForPickaxeHandler = IngredientSet.sticks(ingredient, stack, NB_STICKS);

                i++;
            } while (i < inventory.get().size() && (!haveEnoughForPickaxeHead || !haveEnoughForPickaxeHandler));

            return (haveEnoughForPickaxeHead && haveEnoughForPickaxeHandler) ? ingredient : null;
        }
    }

    public static final class StoneRecipe
    {
        public static final int NB_STONES = 3;


        public static Ingredient apply(final Inventory inventory)
        {
            Ingredient ingredient = new Ingredient(PickaxeRecipe.NB_INGREDIENTS);
            boolean haveEnoughForPickaxeHandler = false;
            boolean haveEnoughForPickaxeHead = false;

            int i = 0;
            do {
                Stack stack = inventory.get().get(i);

                if (!haveEnoughForPickaxeHead)
                    haveEnoughForPickaxeHead = IngredientSet.stones(ingredient, stack, NB_STONES);
                if (!haveEnoughForPickaxeHandler)
                    haveEnoughForPickaxeHandler = IngredientSet.sticks(ingredient, stack, NB_STICKS);

                i++;
            } while (i < inventory.get().size() && (!haveEnoughForPickaxeHead || !haveEnoughForPickaxeHandler));

            return (haveEnoughForPickaxeHead && haveEnoughForPickaxeHandler) ? ingredient : null;
        }
    }

    public static final class IronRecipe
    {
        public static final int NB_IRONS = 3;


        public static Ingredient apply(final Inventory inventory)
        {
            Ingredient ingredient = new Ingredient(PickaxeRecipe.NB_INGREDIENTS);
            boolean haveEnoughForPickaxeHandler = false;
            boolean haveEnoughForPickaxeHead = false;

            int i = 0;
            do {
                Stack stack = inventory.get().get(i);

                if (!haveEnoughForPickaxeHead)
                    haveEnoughForPickaxeHead = IngredientSet.irons(ingredient, stack, NB_IRONS);
                if (!haveEnoughForPickaxeHandler)
                    haveEnoughForPickaxeHandler = IngredientSet.sticks(ingredient, stack, NB_STICKS);

                i++;
            } while (i < inventory.get().size() && (!haveEnoughForPickaxeHead || !haveEnoughForPickaxeHandler));

            return (haveEnoughForPickaxeHead && haveEnoughForPickaxeHandler) ? ingredient : null;
        }
    }
}
