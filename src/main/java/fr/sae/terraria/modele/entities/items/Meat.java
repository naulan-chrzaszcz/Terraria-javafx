package fr.sae.terraria.modele.entities.items;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.EatableObjectType;
import fr.sae.terraria.modele.entities.player.inventory.Inventory;
import fr.sae.terraria.modele.entities.player.Player;


public class Meat extends Item implements EatableObjectType
{
    private final Environment environment;


    public Meat(final Environment environment)
    {
        super();
        this.environment = environment;
    }

    @Override public void eat()
    {
        Player player = environment.getPlayer();

        if (player.getPv() < player.getPvMax()) {
            Inventory inventory = player.getInventory();

            player.setPv(player.getPv() + 1);
            inventory.get().get(inventory.getPosCursor()).remove();
        }
    }
}
