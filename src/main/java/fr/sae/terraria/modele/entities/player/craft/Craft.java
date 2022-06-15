package fr.sae.terraria.modele.entities.player.craft;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.blocks.Block;
import fr.sae.terraria.modele.entities.blocks.BlockSet;
import fr.sae.terraria.modele.entities.items.Item;

import fr.sae.terraria.modele.entities.player.craft.recipes.Ingredient;
import fr.sae.terraria.modele.entities.player.craft.recipes.PickaxeRecipe;
import fr.sae.terraria.modele.entities.player.craft.recipes.RockRecipe;
import fr.sae.terraria.modele.entities.player.inventory.Inventory;
import fr.sae.terraria.modele.entities.player.inventory.Stack;
import fr.sae.terraria.modele.entities.tools.*;

import java.util.Objects;


public class Craft
{


    public static Block rock(final Environment environment)
    {
        Inventory inventory = environment.getPlayer().getInventory();

        for (int i = 0; i < inventory.get().size(); i++) {
            Stack stack = inventory.get().get(i);
            if (Item.isStone(stack.getItem()) && stack.removeQuantity(RockRecipe.NB_STONES))
                return new Block(BlockSet.ROCK, environment);
        }
        return null;
    }

    public static Tool pickaxe(final Environment environment, final MaterialSet material)
    {
        Inventory inventory = environment.getPlayer().getInventory();
        Ingredient pickaxeIngredients = PickaxeRecipe.WOOD_RECIPE.apply(inventory);
        if (!Objects.isNull(pickaxeIngredients)){
            ((Stack)pickaxeIngredients.get()[0].getItem()).removeQuantity(PickaxeRecipe.WOOD_RECIPE.NB_WOODS);
            ((Stack)pickaxeIngredients.get()[1].getItem()).removeQuantity(PickaxeRecipe.WOOD_RECIPE.NB_STICKS);
            return new Tool(ToolSet.PICKAXE, MaterialSet.IRON);
        }
        return null;
    }
}
