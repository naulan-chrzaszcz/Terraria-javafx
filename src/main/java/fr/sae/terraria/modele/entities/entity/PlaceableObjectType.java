package fr.sae.terraria.modele.entities.entity;


/**
 * <h1>Placeable Object Type</h1>
 * <h2>Interface utile sur les objets qui hérite de block</h2>
 * <h3><u>Description:</u></h3>
 * <p>Permet de savoir si un block sera possable sur un sol ou non</p>
 *
 * @see fr.sae.terraria.modele.entities.blocks.Stone
 * @author CHRZASZCZ Naulan
 */
public interface PlaceableObjectType
{


    /**
     * Permet d'appliquer des actions lorsque qu'il tante de poser un block
     *
     * @param x Les coordonnées du block en x lorsqu'il va êtres poser au sol
     * @param y Les coordonnées du block en y lorsqu'il va êtres poser au sol
     * @see fr.sae.terraria.modele.entities.blocks.Stone
     */
    void place(final int x, final int y);
}
