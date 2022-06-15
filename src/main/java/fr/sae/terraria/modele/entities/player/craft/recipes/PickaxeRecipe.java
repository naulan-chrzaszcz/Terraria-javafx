package fr.sae.terraria.modele.entities.player.craft.recipes;

import fr.sae.terraria.modele.entities.items.Item;
import fr.sae.terraria.modele.entities.player.inventory.Inventory;
import fr.sae.terraria.modele.entities.player.inventory.Stack;


public class PickaxeRecipe
{
    public static final class WOOD_RECIPE
    {
        public static final int NB_WOODS = 3;
        public static final int NB_STICKS = 2;
        private static final int NB_INGREDIENTS = 2;


        public static Ingredient apply(final Inventory inventory)
        {
            int i = 0;
            Ingredient ingredient =  new Ingredient(NB_INGREDIENTS);
            boolean haveEnoughForPickaxeHead = false;
            boolean haveEnoughForPickaxeHandler = false;

           for(Stack stack : inventory.get()){
                if (Item.isWood(stack.getItem()) && stack.haveEnoughQuantity(NB_WOODS) && !haveEnoughForPickaxeHead) {
                    haveEnoughForPickaxeHead = true;
                    ingredient.get()[0] = stack;
                    i++;
                }
                if (Item.isStick(stack.getItem()) && stack.haveEnoughQuantity(NB_STICKS) && !haveEnoughForPickaxeHandler) {
                    haveEnoughForPickaxeHandler = true;
                    ingredient.get()[1] = stack;
                    i++;
                }
            }
            return (i == ingredient.get().length) ? ingredient : null;
        }
    }

    public static final class STONE
    {
        public static final int NB_STONE = 3;
        public static final int NB_STICK = 2;
    }

    public static final class IRON
    {
        public static final int NB_IRONS = 3;
        public static final int NB_STICKS = 2;
    }



}
