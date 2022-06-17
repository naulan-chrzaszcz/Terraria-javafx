package fr.sae.terraria.modele.entities.items;

import fr.sae.terraria.modele.entities.entity.StowableObjectType;


public enum Item implements StowableObjectType
{
    COAL,
    FIBER,
    IRON,
    SILEX,
    STONE,
    WOOD,
    STICK,
    MEAT;


    public static boolean isCoal(StowableObjectType obj) { return obj == Item.COAL; }
    public static boolean isFiber(StowableObjectType obj) { return obj == Item.FIBER; }
    public static boolean isIron(StowableObjectType obj) { return obj == Item.IRON; }
    public static boolean isStone(StowableObjectType obj) { return obj == Item.STONE; }
    public static boolean isSilex(StowableObjectType obj) { return obj == Item.SILEX; }
    public static boolean isWood(StowableObjectType obj) { return obj == Item.WOOD; }
    public static boolean isStick(StowableObjectType obj) { return obj == Item.STICK; }
    public static boolean isMeat(StowableObjectType obj) { return obj == Item.MEAT; }
}
