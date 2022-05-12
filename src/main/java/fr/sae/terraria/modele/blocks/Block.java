package fr.sae.terraria.modele.blocks;

import fr.sae.terraria.modele.entities.Entity;


public abstract class Block extends Entity
{


    public Block(int x, int y)
    {
        super(x, y);
    }

    public abstract void updates();
}
