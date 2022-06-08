package fr.sae.terraria.modele.entities.entity;


/**
 * <h1>Breakable Object Type</h1>
 * <h2>Interface utile à un objet qui hérite d'Entity</h2>
 * <h3><u>Description:</u></h3>
 * <p>Determine un objet qui est cassable</p>
 *
 * @see Entity
 * @author CHRZASCZCZ Naulan
 */
public interface BreakableObjectType
{


    /**
     * Fonction appelée lorsqu'une entité tante de casser une autre entité
     *  Permet de faire des actions personnalisées.
     *
     * @see fr.sae.terraria.modele.entities.blocks.Dirt
     * @see fr.sae.terraria.modele.entities.blocks.TallGrass
     * @see fr.sae.terraria.modele.entities.blocks.Stone
     */
    void breaks();
}
