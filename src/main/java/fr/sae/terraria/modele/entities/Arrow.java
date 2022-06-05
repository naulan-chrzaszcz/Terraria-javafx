package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.MovableObjectType;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;


public class Arrow extends Entity implements StowableObjectType, MovableObjectType, CollideObjectType
{
    private final Environment environment;


    public Arrow(final Environment environment, int x, int y, int velocity)
    {
        super(x, y);
        this.environment = environment;

        this.velocity = velocity;
    }

    @Override public void updates() { /* TODO document why this method is empty */ }
    @Override public void move() { /* TODO document why this method is empty */ }
    @Override public void collide() { /* TODO document why this method is empty */ }

    @Override public void moveRight() { super.moveRight(); }
    @Override public void moveLeft() { super.moveLeft(); }
    @Override public void jump() { /* UNE FLECHE NE PEUT SAUTER */ }
    @Override public void fall() { super.fall(); }
    @Override public void worldLimit() { super.worldLimit(this.environment); }
}
