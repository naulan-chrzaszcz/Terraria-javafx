package fr.sae.terraria.modele.entities.blocks;

import fr.sae.terraria.modele.entities.entity.Entity;


public abstract class  Block extends Entity
{


    protected Block(int x, int y) { super(x, y); }

    public abstract void updates();
}
