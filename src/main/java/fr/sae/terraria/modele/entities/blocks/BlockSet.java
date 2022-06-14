package fr.sae.terraria.modele.entities.blocks;


public enum BlockSet
{
    FLOOR_TOP(3),
    FLOOR_RIGHT(3),
    FLOOR_LEFT(3),
    DIRT(3),
    ROCK(5),
    TALL_GRASS(2),
    TORCH(1),
    TREE(4);

    private final int durability;


    BlockSet(final int durability)
    {
        this.durability = durability;
    }

    public int getDurability() { return this.durability; }
}
