package fr.sae.terraria.modele.entities.blocks;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.PlaceableObjectType;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;


public class Dirt extends Block implements StowableObjectType, CollideObjectType, PlaceableObjectType
{
    public static final int BREAK_RESISTANCE = 2;

    private final Environment environment;

    private final double xOrigin;
    private final double yOrigin;


    public Dirt(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;

        this.xOrigin = x;
        this.yOrigin = y;

        this.pv.set(Dirt.BREAK_RESISTANCE);
    }

    @Override public void updates() { /* TODO document why this method is empty */ }

    @Override public void collide() { /* NE RIEN REMPLIR */ }

    @Override public void breaks()
    {
        Environment.playSound("sound/grassyStep.wav", false);
        this.breakAnimation(environment, this, xOrigin, yOrigin);

        if (this.getPv() <= 0) {
            this.environment.getPlayer().pickup(this);

            int yIndexTile = (int) (getY()/environment.heightTile);
            int xIndexTile = (int) (getX()/environment.widthTile);
            this.environment.getTileMaps().setTile(TileMaps.SKY, yIndexTile, xIndexTile);
            this.environment.getEntities().remove(this);
        }
        this.setPv(this.getPv() - 1);
    }

    @Override public void place(final int x, final int y)
    {
        Environment.playSound("sound/axchop.wav", false);
        Entity entity = new Dirt(this.environment, x*environment.widthTile, y*environment.heightTile);
        entity.setRect(environment.widthTile, environment.heightTile);

        environment.getTileMaps().setTile(TileMaps.DIRT, y, x);
        environment.getEntities().add(0, entity);
    }
}
