package fr.sae.terraria.modele.blocks;

import fr.sae.terraria.modele.StowableObjectType;
import fr.sae.terraria.modele.entities.entity.Entity;


public abstract class Block extends Entity implements StowableObjectType
{


    public Block(int x, int y)
    {
        super(x, y);
    }

    public abstract void updates();
}
