package fr.sae.terraria.modele.entities.items;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.ConsumableObjectType;
import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.modele.entities.player.inventory.Inventory;


public class Meat extends Item implements ConsumableObjectType
{
    private final Environment environment;


    public Meat(final Environment environment)
    {
        super();
        this.environment = environment;
    }

    @Override public void consumes()
    {
        Player player = environment.getPlayer();

        if (player.getPv() < player.getPvMax()) {
            Inventory inventory = player.getInventory();

            player.setPv(player.getPv() + 1);
            inventory.get().get(inventory.getPosCursor()).remove();
        }
    }
}
