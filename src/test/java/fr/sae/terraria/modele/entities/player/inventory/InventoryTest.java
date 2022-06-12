package fr.sae.terraria.modele.entities.player.inventory;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.blocks.Dirt;
import fr.sae.terraria.modele.entities.player.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class InventoryTest
{
    private static Environment environment;
    private static Player player;
    private static Inventory inventory;


    @BeforeAll public static void init()
    {
        environment = new Environment(1., 1.);
        player = environment.getPlayer();
        inventory = player.getInventory();
    }

    @Test public final void nbStacksIntoInventoryTest()
    {
        for (int i = 0; i < 16; i++)
            inventory.put(new Dirt(environment, 1, 1));
        assertEquals(inventory.nbStacksIntoInventory(), 1);

        inventory.put(new Dirt(environment, 1, 1));
        assertEquals(inventory.nbStacksIntoInventory(), 2);
    }
}
