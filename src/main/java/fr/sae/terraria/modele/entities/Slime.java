package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.*;


public class Slime extends Entity implements CollideObjectType, MovableObjectType, CollapsibleObjectType, SpawnableObjectType
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

    @Override public void spawn(int x, int y)
    {
        this.setX(x);
        this.setY(y);
        this.getGravity().setXInit(x);
        this.getGravity().setYInit(y);

        this.environment.getEntities().add(0, this);
        this.environment.getSlimes().add(0, this);
    }

    @Override public void moveRight() { super.moveRight(); }

    @Override public void moveLeft() { super.moveLeft(); }

    @Override public void jump() { super.jump(); }

    @Override public void fall() { super.fall(); }

    @Override public void worldLimit() { super.worldLimit(this.environment); }
}
