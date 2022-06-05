package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.CollapsibleObjectType;
import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.MovableObjectType;


public class Slime extends Entity implements CollideObjectType, MovableObjectType, CollapsibleObjectType
{
    public static final int WHEN_SPAWN_A_SLIME = 2500;
    public static final double SLIME_SPAWN_RATE = .2;

    private final Environment environment;


    public Slime(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;
    }

    public Slime(Environment environment) { this(environment, 0, 0); }

    @Override public void updates() { /* TODO document why this method is empty */ }

    @Override public void collide() { /* TODO document why this method is empty */ }

    @Override public void move() { /* TODO document why this method is empty */ }

    @Override public void hit()
    {
        Environment.playSound("sound/daggerswipe.wav", false);
    }

    @Override public void moveRight() { super.moveRight(); }

    @Override public void moveLeft() { super.moveLeft(); }

    @Override public void jump() { super.jump(); }

    @Override public void fall() { super.fall(); }

    @Override public void worldLimit() { super.worldLimit(this.environment); }
}
