package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.MovableObjectType;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;


public class Arrow extends Entity implements StowableObjectType, MovableObjectType, CollideObjectType
{
    private final Environment environment;


    public Arrow(Environment environment, int x, int y, int velocity)
    {
        super(x, y);
        this.environment = environment;

        this.velocity = velocity;
    }

    public void updates() { /* TODO document why this method is empty */ }
    public void move() { /* TODO document why this method is empty */ }
    public void collide() { /* TODO document why this method is empty */ }

    public void moveRight() { super.moveRight(); }
    public void moveLeft() { super.moveLeft(); }
    public void jump() { /* UNE FLECHE NE PEUT SAUTER */ }
    public void fall() { super.fall(); }
    public void worldLimit() { super.worldLimit(environment); }
}
