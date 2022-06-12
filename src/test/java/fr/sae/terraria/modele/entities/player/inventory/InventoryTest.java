package fr.sae.terraria.modele.entities.player.inventory;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.player.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class InventoryTest
{
    private static Environment environment;
    private static Inventory inventory;


    @BeforeAll public static void init()
    {
        environment = new Environment(1., 1.);
        Player player = environment.getPlayer();
        inventory = player.getInventory();
    }

    @Test public final void nbStacksIntoInventoryTest()
    {
        for (int i = 0; i < Stack.MAX; i++)
            inventory.put(new Dirt(environment, 1, 1));
        assertEquals(2, inventory.nbStacksIntoInventory());

        for (int i = 0; i < Stack.MAX; i++)
            inventory.put(new Dirt(environment, 1, 1));
        assertEquals(3, inventory.nbStacksIntoInventory());
    }

    @Test public final void nbItemsIntoStackTest()
    {
        inventory.posCursorProperty().set(0);
        for (int i = 0; i < Stack.MAX; i++)
            inventory.put(new Dirt(environment, 1, 1));
        assertEquals(Stack.MAX, inventory.getStack().getNbItems());
    }
}
