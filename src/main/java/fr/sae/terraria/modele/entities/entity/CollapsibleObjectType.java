package fr.sae.terraria.modele.entities.entity;


/**
 * <h1>Collapsible Object Type</h1>
 * <h2>Interface utile à un objet qui hérite d'Entity</h2>
 * <h3><u>Description:</u></h3>
 * <p>Détermine les objets qui sont touchable par d'autres entités</p>
 * <p>Il détermine surtout les objets qui sont touchable avec des flèches ou des outils qui infligent des dégâts, qui peuvent en général avoir des dégâts à cause de ces éléments</p>
 *
 * @see Entity
 * @author CHRZASZCZ Naulan
 */
public interface CollapsibleObjectType
{


    /**
     * Fonction qui est appelée lorsqu'une entité est touchée par une autre entité par n'importe quel moyen en excluant les collisions entre collision
     *  Permet de faire des actions personnalisées par entité lorsque une entité en touche une autre
     *
     * @see fr.sae.terraria.modele.entities.Rabbit
     */
    void hit();
}
