package fr.sae.terraria.modele.entities.items;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.ConsumableObjectType;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.modele.entities.player.Player;


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
        Player player = environment.getPlayer();

        if (player.getPv() < player.getPvMax())
            player.setPv(player.getPv() + 1);
    }
}
