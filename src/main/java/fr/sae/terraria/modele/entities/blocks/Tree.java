package fr.sae.terraria.modele.entities.blocks;


import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.items.Wood;

public class Tree extends Block
{
    public static final int WHEN_SPAWN_A_TREE = 5_000;
    public static final double TREE_SPAWN_RATE = .2;

    private Environment environment;


    public Tree(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;
    }

    public Tree(Environment environment) { this(environment, 0, 0); }

    public void updates() { }

    public void breaks()
    {
        this.environment.getPlayer().pickup(new Wood());
    }
}
