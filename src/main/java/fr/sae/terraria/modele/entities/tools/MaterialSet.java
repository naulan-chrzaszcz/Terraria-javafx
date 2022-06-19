package fr.sae.terraria.modele.entities.tools;


public enum MaterialSet
{
    WOOD(1.),
    STONE(1.5),
    IRON(2.);

    private final double durabilityMultiplicator;


    MaterialSet(double durabilityMultiplicator)
    {
        this.durabilityMultiplicator = durabilityMultiplicator;
    }

    public static boolean isWood(MaterialSet material) { return material == MaterialSet.WOOD; }
    public static boolean isStone(MaterialSet material) { return material == MaterialSet.STONE; }
    public static boolean isIron(MaterialSet material) { return material == MaterialSet.IRON; }


    public double getDurabilityMultiplicator() { return this.durabilityMultiplicator; }
}
