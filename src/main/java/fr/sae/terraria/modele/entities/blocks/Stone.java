package fr.sae.terraria.modele.entities.blocks;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.PlaceableObjectType;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.modele.entities.tools.Pickaxe;
import fr.sae.terraria.modele.entities.tools.Tool;


public class Stone extends Block implements StowableObjectType, CollideObjectType, PlaceableObjectType
{
    public static final int BREAK_RESISTANCE = 5;

    private final Environment environment;

    private final double xOrigin;
    private final double yOrigin;


    public Stone(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;

        this.xOrigin = x;
        this.yOrigin = y;

        this.pv.set(Stone.BREAK_RESISTANCE);
    }

    @Override public void updates() { /* TODO document why this method is empty */ }

    @Override public void collide() { /* NE RIEN REMPLIR */ }

    @Override public void breaks()
    {
        Environment.playSound("sound/brick" + ((int) (Math.random()*2)+1) + ".wav", false);
        this.breakAnimation(environment, this, xOrigin, yOrigin);

        // Une fois le bloc détruit, il donne la pierre et le supprime de la TileMaps
        if (this.getPv() <= 0) {
            this.environment.getPlayer().pickup(this);

            int yIndexTile = (int) (getY()/environment.heightTile);
            int xIndexTile = (int) (getX()/environment.widthTile);
            this.environment.getTileMaps().setTile(TileMaps.SKY, yIndexTile, xIndexTile);
            this.environment.getEntities().remove(this);
        }

        // S'il utilise le bon outil, il commencera à casser le bloc sinon use l'outil sans casser le bloc.
        if (environment.getPlayer().getItemSelected() instanceof Pickaxe)
            this.setPv(this.getPv() - 1);
        if (environment.getPlayer().getItemSelected() instanceof Tool)
            ((Tool) environment.getPlayer().getItemSelected()).use();
    }

    @Override public void place(int x, int y)
    {
        Environment.playSound("sound/axchop.wav", false);
        Entity entity = new Stone(this.environment, x*environment.widthTile, y*environment.heightTile);
        entity.setRect(environment.widthTile, environment.heightTile);

        environment.getTileMaps().setTile(TileMaps.STONE, y, x);
        environment.getEntities().add(0, entity);
    }
}
