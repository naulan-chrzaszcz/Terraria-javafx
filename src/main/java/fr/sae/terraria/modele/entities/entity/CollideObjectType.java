package fr.sae.terraria.modele.entities.entity;


/**
 * Permet de regrouper et de connaitre les entités qui doivent avoir une collision entre eux
 *  et avec leurs environment
 */
public interface CollideObjectType
{
    int COLLISION_TOLERANCE = 3;


    /** Permet à chaque passage de boucle, d'appliquer une collision entre les entités et de son environment */
    void collide();
}
