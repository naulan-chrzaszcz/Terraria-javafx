package fr.sae.terraria.modele.entities.tools;


public enum MaterialSet
{
    WOOD,
    STONE,
    IRON;


    public static boolean isWood(MaterialSet material) { return material == MaterialSet.WOOD; }
    public static boolean isStone(MaterialSet material) { return material == MaterialSet.STONE; }
    public static boolean isIron(MaterialSet material) { return material == MaterialSet.IRON; }
}
