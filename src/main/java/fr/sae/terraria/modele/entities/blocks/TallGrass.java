package fr.sae.terraria.modele.entities.blocks;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.ReproductiveObjectType;
import fr.sae.terraria.modele.entities.items.Fiber;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.ArrayList;
import java.util.List;


public class TallGrass extends Block implements ReproductiveObjectType
{
    public static final int WHEN_SPAWN_A_TALL_GRASS = 2_500;
    public static final double TALL_GRASS_SPAWN_RATE = .3;
    public static final double REPRODUCTION_RATE = 500;
    public static final double GROWTH_SPEED = .0005;
    public static final int GROWTH_TALL_GRASS_STEP = 6;
    public static final int LOOTS_FIBRE_MAX = 3;

    private DoubleProperty tallGrassGrowth;

    private Environment environment;


    public TallGrass(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;

        this.tallGrassGrowth = new SimpleDoubleProperty(0);
    }

    public TallGrass(Environment environment) { this(environment, 0, 0); }

    public void updates()
    {
        if (tallGrassGrowth.get() < GROWTH_TALL_GRASS_STEP)
            tallGrassGrowth.set(tallGrassGrowth.get() + GROWTH_SPEED);
    }

    public void breaks()
    {
        Environment.playSound("sound/cut.wav", false);
        this.environment.getPlayer().pickup(new Fiber());
        this.environment.getEntities().remove(this);
    }

    public List<Entity> reproduction(Environment environment)
    {
        List<Entity> children = new ArrayList<>();

        boolean tallGrassMustReproduce = environment.getTicks()%TallGrass.REPRODUCTION_RATE == 0;
        if (tallGrassMustReproduce) {
            List<Entity> entities = environment.getEntities();

            int left = 0;
            int right = 0;
            for (int x = 0; x < entities.size(); x++) {
                if (entities.get(x).getX() == (getX() - environment.widthTile) && entities.get(x).getY() == getY())
                    left++;
                if (entities.get(x).getX() == (getX() + environment.widthTile) && entities.get(x).getY() == getY())
                    right++;
            }

            int x = -1;
            int y = (int) (getY()/environment.heightTile)+1;
            if (left == 0)
                x = (int) (getX() - environment.widthTile)/environment.widthTile;
            else if (right == 0)
                x = (int) (getX() + environment.widthTile)/environment.widthTile;

            if ((x >= 0 && x < environment.getTileMaps().getWidth()) && (y >= 0 && y < environment.getTileMaps().getHeight())) {
                if (environment.getTileMaps().getTile(x, y) != TileMaps.SKY) {
                    TallGrass tallGrassChildren = new TallGrass(this.environment, (int) ((left == 0) ? (getX() - environment.widthTile) : (getX() + environment.widthTile)), (int) getY());
                    children.add(tallGrassChildren);
                }
            }
        }

        return children;
    }


    public DoubleProperty getTallGrassGrowthProperty() { return this.tallGrassGrowth; }
}
