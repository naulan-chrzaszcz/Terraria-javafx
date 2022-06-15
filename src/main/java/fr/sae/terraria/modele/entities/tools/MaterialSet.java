package fr.sae.terraria.modele.entities.tools;


public enum MaterialSet
{
    WOOD,
    IRON;


    public static boolean isWood(MaterialSet material) { return material == MaterialSet.IRON; }
}
