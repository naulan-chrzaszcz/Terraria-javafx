package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.EntityMovable;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;


public class Arrow extends EntityMovable implements StowableObjectType, CollideObjectType
{


    public Arrow(final Environment environment, int x, int y, int velocity)
    {
        super(x, y, environment);

        this.velocity = velocity;
    }

    @Override public void updates() { /* TODO document why this method is empty */ }
    @Override public void move() { /* TODO document why this method is empty */ }
    @Override public void collide() { /* TODO document why this method is empty */ }
}
