package fr.sae.terraria.modele.entities.player.craft.recipes;

import fr.sae.terraria.modele.entities.items.Item;
import fr.sae.terraria.modele.entities.player.inventory.Inventory;
import fr.sae.terraria.modele.entities.player.inventory.Stack;
import fr.sae.terraria.modele.entities.tools.MaterialSet;


public class PickaxeRecipe
{
    public static final class WOOD_RECIPE
    {
        public static final int NB_WOODS = 3;
        public static final int NB_STICKS = 2;

        public static boolean apply(final Inventory inventory)
        {
            boolean haveEnoughForPickaxeHead = false;
            boolean haveEnoughForPickaxeHandler = false;

            for (int i = 0; i < inventory.get().size(); i++) {
                Stack stack = inventory.get().get(i);
                if (Item.isWood(stack.getItem()) && stack.haveEnoughQuantity(NB_WOODS))
                    haveEnoughForPickaxeHead = true;
                if (Item.isStick(stack.getItem()) && stack.haveEnoughQuantity(NB_STICKS))
                    haveEnoughForPickaxeHandler = true;
            }
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
