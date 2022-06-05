package fr.sae.terraria.modele.entities.blocks;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.PlaceableObjectType;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;


public class Torch extends Block implements StowableObjectType, PlaceableObjectType
{
    private final Environment environment;

    private double xOrigin;
    private double yOrigin;


    public Torch(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;

        this.xOrigin = x;
        this.yOrigin = y;
    }

    @Override public void updates() { /* TODO document why this method is empty */ }

    @Override public void breaks()
    {
        this.breakAnimation(environment, this, xOrigin, yOrigin);

        this.environment.getEntities().remove(this);
        this.environment.getTorches().remove(this);
    }

    @Override public void place(int x, int y)
    {
        Environment.playSound("sound/axchop.wav", false);
        Torch entity = new Torch(this.environment, x*environment.widthTile, y*environment.heightTile);
        entity.setRect(environment.widthTile, environment.heightTile);

        environment.getEntities().add(0, entity);
        environment.getTorches().add(0, entity);
    }
}