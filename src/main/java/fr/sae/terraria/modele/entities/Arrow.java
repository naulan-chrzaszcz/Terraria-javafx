package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;


public class Arrow extends Entity implements StowableObjectType
{


    public Arrow(int x, int y, int velocity)
    {
        super(x, y);

        this.velocity = velocity;
    }

    public void updates() { }
}
