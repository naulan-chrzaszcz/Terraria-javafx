package fr.sae.terraria.modele.entities.entity;

import fr.sae.terraria.modele.Environment;

import java.util.List;


/**
 * <h1>Reproduction Object Type</h1>
 * <h2>Interface utile à un objet qui hérite d'Entity</h2>
 * <h3><u>Description:</u></h3>
 * <p>Il permet de savoir si l'entité qui implementer cette interface est une entité qui peut se reproduire entre eux.</p>
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
     * @see fr.sae.terraria.modele.entities.blocks.TallGrass
     */
    List<Entity> reproduction(Environment environment);
}
