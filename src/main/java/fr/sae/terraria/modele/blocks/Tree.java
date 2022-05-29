package fr.sae.terraria.modele.blocks;


public class Tree extends Block
{
    public static final int WHEN_SPAWN_A_TREE = 5_000;
    public static final double TREE_SPAWN_RATE = .2;


    public Tree(int x, int y) { super(x, y); }
    public Tree() { this(0, 0); }

    public void updates()
    {
        
    }
}
