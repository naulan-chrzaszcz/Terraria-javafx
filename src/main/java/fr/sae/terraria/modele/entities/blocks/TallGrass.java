package fr.sae.terraria.modele.entities.blocks;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.ReproductiveObjectType;
import fr.sae.terraria.modele.entities.entity.SpawnableObjectType;
import fr.sae.terraria.modele.entities.items.Fiber;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.ArrayList;
import java.util.List;


public class TallGrass extends Block implements ReproductiveObjectType, SpawnableObjectType
{
    public static final int WHEN_SPAWN_A_TALL_GRASS = 2_500;
    public static final double TALL_GRASS_SPAWN_RATE = .3;
    public static final double REPRODUCTION_RATE = 500;
    public static final double GROWTH_SPEED = .0005;
    public static final int GROWTH_TALL_GRASS_STEP = 6;
    public static final int LOOTS_FIBRE_MAX = 3;

    private final DoubleProperty tallGrassGrowth;

    private final Environment environment;


    public TallGrass(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;

        this.tallGrassGrowth = new SimpleDoubleProperty(0);
    }

    public TallGrass(Environment environment) { this(environment, 0, 0); }

    @Override public void updates()
    {
        // L'animation de pousse
        if (tallGrassGrowth.get() < GROWTH_TALL_GRASS_STEP)
            tallGrassGrowth.set(tallGrassGrowth.get() + GROWTH_SPEED);
    }

    /** Joue un son et donne au joueur entre 1 et 3 de fibre */
    @Override public void breaks()
    {
        Environment.playSound("sound/cut.wav", false);

        for (int loot = (int) (Math.random()*3)+1; loot < LOOTS_FIBRE_MAX; loot++)
            this.environment.getPlayer().pickup(new Fiber());
        this.environment.getEntities().remove(this);
    }

    /** Reproduit les hautes herbes à gauche et à droite de la haute herbe parente */
    @Override public List<Entity> reproduction(Environment environment)
    {
        List<Entity> children = new ArrayList<>();

        boolean tallGrassMustReproduce = environment.getTicks()%TallGrass.REPRODUCTION_RATE == 0;
        if (tallGrassMustReproduce) {
            List<Entity> entities = environment.getEntities();
            int heightTile = environment.heightTile;
            int widthTile = environment.widthTile;
            int widthMaps = environment.getTileMaps().getWidth();
            int heightMaps = environment.getTileMaps().getHeight();

            int x = -1;
            int y = (int) (getY()/heightTile)+1;

            // Check si personne à côté
            int left = 0; int right = 0;
            for (Entity entity : entities) {
                boolean haveAnEntityOnLeft = entity.getX() == (getX() - widthTile) && entity.getY() == getY();
                boolean haveAnEntityOnRight = entity.getX() == (getX() + widthTile) && entity.getY() == getY();

                if (haveAnEntityOnLeft) left++;
                if (haveAnEntityOnRight) right++;
            }

            // Si les casses à côté sont libres
            boolean leftIsAvailable = (left == 0);
            boolean rightIsAvailable = (right == 0);
            if (leftIsAvailable)
                x = (int) (getX() - widthTile)/widthTile;
            else if (rightIsAvailable)
                x = (int) (getX() + widthTile)/widthTile;

            // La place sur la carte
            boolean isntOutTheMap = (x >= 0 && x < widthMaps) && (y >= 0 && y < heightMaps);
            if (isntOutTheMap) {
                boolean dontHaveTile = environment.getTileMaps().getTile(x, y) != TileMaps.SKY;

                if (dontHaveTile) {
                    int xTallGrassChildren = (int) ((left == 0) ? (getX() - widthTile) : (getX() + widthTile));
                    int yTallGrassChildren = (int) getY();

                    children.add(new TallGrass(this.environment, xTallGrassChildren, yTallGrassChildren));
                }
            }
        }

        return children;
    }

    @Override public void spawn(int x, int y)
    {
        this.setX(x);
        this.setY(y);
        this.environment.getEntities().add(0, this);
    }


    public DoubleProperty getTallGrassGrowthProperty() { return this.tallGrassGrowth; }
}
