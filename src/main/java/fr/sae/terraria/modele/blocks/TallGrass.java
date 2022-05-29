package fr.sae.terraria.modele.blocks;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.ArrayList;
import java.util.List;


public class TallGrass extends Block
{
    public static final int GROWTH_TALL_GRASS_STEP = 4;
    public static final double GROWTH_SPEED = .005;
    private static final int LOOTS_DROP_MAX = 3;

    private DoubleProperty tallGrassGrowth;


    public TallGrass(int x, int y)
    {
        super(x, y);

        this.tallGrassGrowth = new SimpleDoubleProperty(0);
    }

    public TallGrass() { this(0, 0); }

    public void updates()
    {
        if (tallGrassGrowth.get() < GROWTH_TALL_GRASS_STEP)
            tallGrassGrowth.set(tallGrassGrowth.get() + GROWTH_SPEED);
    }

    public int loots()
    {
        double nbItemsAtLoots = Math.random()*LOOTS_DROP_MAX;

        if (nbItemsAtLoots != 0) {

        }

        return (int) (nbItemsAtLoots);
    }


    public DoubleProperty getTallGrassGrowthProperty() { return this.tallGrassGrowth; }
}
