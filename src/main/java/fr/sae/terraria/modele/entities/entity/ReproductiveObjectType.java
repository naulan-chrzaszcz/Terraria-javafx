package fr.sae.terraria.modele.entities.entity;

import fr.sae.terraria.modele.Environment;

import java.util.List;


/**
 * <h1>ReproductionObjectType</h1>
 * <h2><u>Description:</u></h2>
 * <p>Il permet de savoir si l'entité qui implemente cette interface est une entité qui peut se reproduire entre eux.</p>
 *
 * @see fr.sae.terraria.modele.entities.Rabbit
 * @see fr.sae.terraria.modele.entities.blocks.TallGrass
 * @author CHRZASZCZ Naulan
 */
public interface ReproductiveObjectType
{

    /**
     * Reproduit les entités concernées.
     * @return Les enfants de l'entité
     */
    List<Entity> reproduction(Environment environment);
}
