package fr.sae.terraria.modele.entities.blocks;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;


public class Torch extends Block implements StowableObjectType
{
    private final Environment environment;


    public Torch(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;
    }

    public void updates() { /* TODO document why this method is empty */ }

    public void breaks()
    {
        this.environment.getEntities().remove(this);
        this.environment.getTorches().remove(this);
    }
}