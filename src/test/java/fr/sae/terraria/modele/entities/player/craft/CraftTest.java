package fr.sae.terraria.modele.entities.player.craft;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.blocks.Block;
import fr.sae.terraria.modele.entities.items.Item;
import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.modele.entities.player.craft.recipes.IngredientSet;
import fr.sae.terraria.modele.entities.player.craft.recipes.PickaxeRecipe;
import fr.sae.terraria.modele.entities.player.craft.recipes.RockRecipe;
import fr.sae.terraria.modele.entities.player.inventory.Stack;
import fr.sae.terraria.modele.entities.tools.MaterialSet;
import fr.sae.terraria.modele.entities.tools.Tool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


public class CraftTest
{
    private static Environment environment;
    private static Player player;


    public CraftTest() { super(); }

    @BeforeAll public static void init()
    {
        environment = new Environment(1., 1.);
        player = environment.getPlayer();
    }

    @Test public final void stockTest()
    {
        player.pickup(Item.STONE);
        player.pickup(Item.STONE);
        player.pickup(Item.STONE);
        player.pickup(Item.STONE);

        boolean haveEnoughStones = false;
        for (Stack stack : player.getInventory().get())
            if (Item.isStone(stack.getItem()) && stack.haveEnoughQuantity(RockRecipe.NB_STONES))
                haveEnoughStones = true;
        assertTrue(haveEnoughStones);

        Block block = Craft.rock(environment);
        player.pickup(block);

        boolean haveRock = false;
        for (Stack stack : player.getInventory().get())
            if (stack.getItem() instanceof Block && Block.isRock((Block) stack.getItem()))
                haveRock = true;
        assertTrue(haveRock);

        boolean dontHaveStones = true;
        for (Stack stack : player.getInventory().get())
            if (Item.isStone(stack.getItem())) {
                dontHaveStones = false;
                break;
            }
        assertTrue(dontHaveStones);
    }

    @Test public final void pickaxeTest()
    {
        player.pickup(Item.WOOD);
        player.pickup(Item.WOOD);
        player.pickup(Item.WOOD);
        player.pickup(Item.STICK);
        player.pickup(Item.STICK);

        boolean haveEnoughWoods = false;
        boolean haveEnoughSticks = false;
        for (Stack stack : player.getInventory().get()) {
            if (Item.isWood(stack.getItem()) && stack.haveEnoughQuantity(PickaxeRecipe.WoodRecipe.NB_WOODS))
                haveEnoughWoods = true;
            if (Item.isStick(stack.getItem()) && stack.haveEnoughQuantity(PickaxeRecipe.NB_STICKS))
                haveEnoughSticks = true;
        }

        assertTrue(haveEnoughWoods);
        assertTrue(haveEnoughSticks);
        Tool woodPickaxe = Craft.pickaxe(environment, MaterialSet.WOOD);
        assertTrue(!Objects.isNull(woodPickaxe) && Tool.isPickaxe(woodPickaxe) && MaterialSet.isWood(woodPickaxe.getMaterial()));
        player.pickup(woodPickaxe);

        boolean havePickaxe = false;
        for (Stack stack : player.getInventory().get())
            if (Tool.isPickaxe((Tool) stack.getItem()))
                havePickaxe = true;
        assertTrue(havePickaxe);


        boolean dontHaveWoods = true;
        boolean dontHaveSticks = true;
        for (Stack stack : player.getInventory().get()) {
            System.out.println(stack.getItem());
            if (!Item.isWood(stack.getItem()) && stack.haveEnoughQuantity(PickaxeRecipe.WoodRecipe.NB_WOODS)) {
                dontHaveWoods = false;
                break;
            } else if (!Item.isStick(stack.getItem()) && stack.haveEnoughQuantity(PickaxeRecipe.NB_STICKS)) {
                dontHaveSticks = false;
                break;
            }
        }
        assertTrue(dontHaveSticks && dontHaveWoods);
    }
}
