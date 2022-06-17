package fr.sae.terraria.modele.entities.player.craft;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.blocks.Block;
import fr.sae.terraria.modele.entities.blocks.BlockSet;
import fr.sae.terraria.modele.entities.items.Item;
import fr.sae.terraria.modele.entities.player.craft.recipes.Ingredient;
import fr.sae.terraria.modele.entities.player.craft.recipes.PickaxeRecipe;
import fr.sae.terraria.modele.entities.player.craft.recipes.RockRecipe;
import fr.sae.terraria.modele.entities.player.craft.recipes.TorchRecipe;
import fr.sae.terraria.modele.entities.player.inventory.Inventory;
import fr.sae.terraria.modele.entities.player.inventory.Stack;
import fr.sae.terraria.modele.entities.tools.MaterialSet;
import fr.sae.terraria.modele.entities.tools.Tool;
import fr.sae.terraria.modele.entities.tools.ToolSet;

import java.util.Objects;


public class Craft
{


    public static Block rock(final Environment environment)
    {
        Inventory inventory = environment.getPlayer().getInventory();

        Ingredient rockIngredients = RockRecipe.apply(inventory);
        if (!Objects.isNull(rockIngredients)) {
            Stack stack = rockIngredients.get()[0];
            stack.removeQuantity(RockRecipe.NB_STONES);
        }

        return (!Objects.isNull(rockIngredients)) ? new Block(BlockSet.ROCK, environment) : null;
    }

    public static Block torch(final Environment environment)
    {
        Inventory inventory = environment.getPlayer().getInventory();

        Ingredient torchIngredients = TorchRecipe.apply(inventory);
        if (!Objects.isNull(torchIngredients)) for (int i = 0; i < torchIngredients.get().length; i++) {
            Stack stack = torchIngredients.get()[i];

            if (Item.isStick(stack.getItem()))
                stack.removeQuantity(TorchRecipe.NB_STICKS);
            if (Item.isCoal(stack.getItem()))
                stack.removeQuantity(TorchRecipe.NB_COALS);
        }

        return (!Objects.isNull(torchIngredients)) ? new Block(BlockSet.TORCH, environment) : null;
    }

    public static Tool pickaxe(final Environment environment, final MaterialSet material)
    {
        Inventory inventory = environment.getPlayer().getInventory();

        Ingredient pickaxeIngredients = null;
        if (MaterialSet.isWood(material))
            pickaxeIngredients = PickaxeRecipe.WoodRecipe.apply(inventory);
        else if (MaterialSet.isStone(material))
            pickaxeIngredients = PickaxeRecipe.StoneRecipe.apply(inventory);
        else if (MaterialSet.isIron(material))
            pickaxeIngredients = PickaxeRecipe.IronRecipe.apply(inventory);

        if (!Objects.isNull(pickaxeIngredients)) for (int i = 0; i < pickaxeIngredients.get().length; i++) {
            Stack stack = pickaxeIngredients.get()[i];

            if (Item.isStick(stack.getItem()))
                stack.removeQuantity(PickaxeRecipe.NB_STICKS);

            if (MaterialSet.isWood(material) && Item.isWood(stack.getItem()))
                stack.removeQuantity(PickaxeRecipe.WoodRecipe.NB_WOODS);
            else if (MaterialSet.isStone(material) && Item.isStone(stack.getItem()))
                stack.removeQuantity(PickaxeRecipe.StoneRecipe.NB_STONES);
            else if (MaterialSet.isIron(material) && Item.isIron(stack.getItem()))
                stack.removeQuantity(PickaxeRecipe.IronRecipe.NB_IRONS);
        }

        return (!Objects.isNull(pickaxeIngredients)) ? new Tool(ToolSet.PICKAXE, material) : null;
    }
}
