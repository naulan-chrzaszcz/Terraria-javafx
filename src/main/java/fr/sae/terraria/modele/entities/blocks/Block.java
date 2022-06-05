package fr.sae.terraria.modele.entities.blocks;

import fr.sae.terraria.modele.entities.entity.BreakableObjectType;
import fr.sae.terraria.modele.entities.entity.Entity;


public abstract class  Block extends Entity implements BreakableObjectType
{


    protected Block(int x, int y) { super(x, y); }

    @Override public abstract void updates();
}
