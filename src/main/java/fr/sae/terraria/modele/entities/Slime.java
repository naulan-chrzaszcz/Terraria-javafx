package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.MovableObjectType;
import javafx.beans.property.SimpleDoubleProperty;


public class Slime extends Entity implements CollideObjectType, MovableObjectType
{
    public static final int WHEN_SPAWN_A_SLIME = 2500;
    public static final double SLIME_SPAWN_RATE = .2;
    private Environment environment;


    public Slime(int x, int y)
    {
        super(x, y);
    }

    public Slime(Environment environment)
    {
        this(0, 0);
        this.environment = environment;
    }

    public void updates() { }

    public void collide() { }

    public void move() { }

    public void moveRight() { super.moveRight(); }

    public void moveLeft() { super.moveLeft(); }

    public void jump() { super.jump(); }

    public void fall() { super.fall(); }

    public void worldLimit() { super.worldLimit(environment); }
}
