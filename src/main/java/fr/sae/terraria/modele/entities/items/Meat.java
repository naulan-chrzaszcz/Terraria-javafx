package fr.sae.terraria.modele.entities.items;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.ConsumableObjectType;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.modele.entities.player.inventory.Inventory;
import fr.sae.terraria.modele.entities.player.inventory.Stack;


public class Meat implements ConsumableObjectType, StowableObjectType
{
    private final Environment environment;


    public Meat(final Environment environment)
    {
        super();
        this.environment = environment;
    }

    @Override public void consumes()
    {
        Environment.playSound("sound/eat.wav", false);

        Player player = this.environment.getPlayer();
        if (player.getPv() < player.getPvMax())
            player.setPv(player.getPv() + 1);

        Inventory inventory = player.getInventory();
        inventory.get().get(inventory.getPosCursor()).remove();
    }
}
