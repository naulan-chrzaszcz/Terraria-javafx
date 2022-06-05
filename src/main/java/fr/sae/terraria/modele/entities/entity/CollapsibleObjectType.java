package fr.sae.terraria.modele.entities.entity;


/**
 * <h1>Collapsible Object Type</h1>
 * <h2>Interface utile à un objet qui hérite d'Entity</h2>
 * <h3><u>Description:</u></h3>
 * <p>Determine les objets qui sont touchable par une autre entité</p>
 * <p>Il détermine surtout les objets qui sont touchable avec des flèches ou des outils qui inflige des dégâts, qui peuvent en général avoir des dégâts à cause de ses éléments</p>
 *
 * @see Entity
 * @author CHRZASZCZ Naulan
 */
public interface CollapsibleObjectType
{


    /**
     * Fonction qui est appelée lorsqu'une entité touche un autre par n'importe quel manière en excluent les collisions entre collision
     *  Permet de faire des actions personnalisées par entité lorsque une entitée touche un autre
     *
     * @see fr.sae.terraria.modele.entities.Rabbit
     */
    void hit();
}
