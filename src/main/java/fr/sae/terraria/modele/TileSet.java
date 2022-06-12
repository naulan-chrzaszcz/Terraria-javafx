package fr.sae.terraria.modele;

import fr.sae.terraria.modele.entities.blocks.BlockSet;


public enum TileSet
{
    SKY,
    ROCK,
    DIRT,
    FLOOR_TOP,
    FLOOR_LEFT,
    FLOOR_RIGHT,
    TALL_GRASS,
    TREE;


    public static boolean isSkyTile(TileSet item) { return item == TileSet.SKY; }
    public static boolean isRockTile(TileSet item) { return item == TileSet.ROCK; }
    public static boolean isDirtTile(TileSet item) { return item == TileSet.DIRT; }
    public static boolean isFloorTopTile(TileSet item) { return item == TileSet.FLOOR_TOP; }
    public static boolean isFloorRightTile(TileSet item) { return item == TileSet.FLOOR_RIGHT; }
    public static boolean isFloorLeftTile(TileSet item) { return item == TileSet.FLOOR_LEFT; }

    public static TileSet getTileSet(BlockSet blockSet)
    {
        if (blockSet == BlockSet.DIRT)
            return TileSet.DIRT;
        if (blockSet == BlockSet.ROCK)
            return TileSet.ROCK;
        if (blockSet == BlockSet.FLOOR_LEFT)
            return TileSet.FLOOR_LEFT;
        if (blockSet == BlockSet.FLOOR_RIGHT)
            return TileSet.FLOOR_RIGHT;
        if (blockSet == BlockSet.FLOOR_TOP)
            return TileSet.FLOOR_TOP;
        if (blockSet == BlockSet.TALL_GRASS)
            return TileSet.TALL_GRASS;
        if (blockSet == BlockSet.TREE)
            return TileSet.TREE;
        return null;
    }
}
