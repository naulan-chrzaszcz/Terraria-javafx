package fr.sae.terraria.modele.entities.entity;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.blocks.TallGrass;

import java.util.List;


/**
 * Permet de regrouper les entités qui peuvent se reproduire et d'implémenter cette fonctionnalité.
 *
 * @see fr.sae.terraria.modele.entities.Rabbit
 * @see TallGrass
 */
public interface ReproductiveObjectType
{

    /**
     * Reproduit les entités concernées.
     * @return Les enfants de l'entité
     */
    List<Entity> reproduction(Environment environment);
}
