package fr.sae.terraria.modele.entities.entity;


import fr.sae.terraria.modele.entities.blocks.Rock;

/**
 * <h1>Placeable Object Type</h1>
 * <h2>Interface utile sur les objets qui héritent de block</h2>
 * <h3><u>Description:</u></h3>
 * <p>Permet de savoir si un block sera posable sur un sol ou non</p>
 *
 * @see Rock
 * @author CHRZASZCZ Naulan
 */
public interface PlaceableObjectType
{


    /**
     * Permet d'appliquer des actions lorsque qu'il tente de poser un block
     *
     * @param x Les coordonnées du block en x lorsqu'il va êtres poser au sol
     * @param y Les coordonnées du block en y lorsqu'il va êtres poser au sol
     * @see Rock
     */
    void place(final int x, final int y);
}
