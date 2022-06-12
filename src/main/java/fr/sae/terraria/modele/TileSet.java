package fr.sae.terraria.modele;


public enum TileSet
{
    SKY,
    ROCK,
    DIRT,
    FLOOR_TOP,
    FLOOR_LEFT,
    FLOOR_RIGHT;


    public static boolean isSkyTile(TileSet item) { return item == TileSet.SKY; }
    public static boolean isRockTile(TileSet item) { return item == TileSet.ROCK; }
    public static boolean isDirtTile(TileSet item) { return item == TileSet.DIRT; }
    public static boolean isFloorTopTile(TileSet item) { return item == TileSet.FLOOR_TOP; }
    public static boolean isFloorRightTile(TileSet item) { return item == TileSet.FLOOR_RIGHT; }
    public static boolean isFloorLeftTile(TileSet item) { return item == TileSet.FLOOR_LEFT; }
}
