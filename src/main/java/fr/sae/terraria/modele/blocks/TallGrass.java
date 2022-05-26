package fr.sae.terraria.modele.blocks;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.Entity;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public class TallGrass extends Entity
{
    public static final int GROWTH_TALLGRASS_STEP = 4;
    public static final double GROWTH_SPEED = .005;

    private DoubleProperty tallGrassGrowth;


    public TallGrass(int x, int y)
    {
        super(x, y);

        this.tallGrassGrowth = new SimpleDoubleProperty(0);
    }

    public void updates()
    {
        if (tallGrassGrowth.get() < GROWTH_TALLGRASS_STEP)
            tallGrassGrowth.set(tallGrassGrowth.get() + GROWTH_SPEED);
    }


    public DoubleProperty getTallGrassGrowthProperty() { return this.tallGrassGrowth; }
}
