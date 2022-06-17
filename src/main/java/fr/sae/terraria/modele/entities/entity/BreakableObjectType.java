package fr.sae.terraria.modele.entities.entity;


/**
 * <h1>Breakable Object Type</h1>
 * <h2>Interface utile à un objet qui hérite d'Entity</h2>
 * <h3><u>Description:</u></h3>
 * <p>Détermine un objet cassable</p>
 *
 * @see Entity
 * @author CHRZASCZCZ Naulan
 */
public interface BreakableObjectType
{


    /**
     * Fonction appelée lorsqu'une entité tente de casser une autre entité
     *  Permet de faire des actions personnalisées.
     */
    void breaks();
}
