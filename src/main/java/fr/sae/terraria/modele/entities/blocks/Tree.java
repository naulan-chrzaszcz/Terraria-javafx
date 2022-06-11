package fr.sae.terraria.modele.entities.blocks;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.SpawnableObjectType;
import fr.sae.terraria.modele.entities.items.Wood;


public class Tree extends Block implements SpawnableObjectType
{
    public static final double TREE_SPAWN_RATE = .2;
    public static final double TREE_CLUSTER = 10;

    private final Environment environment;


    public Tree(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;
    }

    public Tree(Environment environment) { this(environment, 0, 0); }

    @Override public void updates() { /* TODO document why this method is empty */ }

    @Override public void breaks()
    {
        // Environment.playSound("sound/grassyStep.wav", false);
        this.environment.getPlayer().pickup(new Wood());
        this.environment.getEntities().remove(this);
        this.environment.getTrees().remove(this);
    }

    @Override public void spawn(int x, int y)
    {
        this.setX(x);
        this.setY(y);
        this.environment.getEntities().add(this);
        this.environment.getTrees().add(this);
    }
}
