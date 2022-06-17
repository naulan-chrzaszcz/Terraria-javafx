package fr.sae.terraria.modele.entities.blocks;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.SpawnableObjectType;


public class Tree extends Block implements SpawnableObjectType
{
    public static final double TREE_SPAWN_RATE = .2;
    public static final double TREE_CLUSTER = 10;
    public static final int STICKS_DROP = 3;

    private final Environment environment;


    public Tree(final Environment environment, int x, int y)
    {
        super(BlockSet.TREE, environment, x, y);
        this.environment = environment;
    }

    public Tree(final Environment environment) { this(environment, 0, 0); }

    @Override public void spawn(int x, int y)
    {
        this.setX(x);
        this.setY(y);
        this.environment.getEntities().add(this);
        this.environment.getTrees().add(this);
    }
}
