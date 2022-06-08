package fr.sae.terraria.modele.entities.entity;


/**
 * <h1>Eatable Object Type</h1>
 * <h2>Interface utile sur les objets de type rangeable dans l'inventaire</h2>
 * <h3><u>Description:</u></h3>
 * <p>Permet de discerner les objets qui sont mangeable par une entité ou ceux qui ne le sont pas</p>
 *
 * @see fr.sae.terraria.modele.entities.items.Item
 * @author CHRZASZCZ Naulan
 */
public interface EatableObjectType
{


    /**
     * Permet d'appliquer des actions lorsque qu'il mange l'aliment qui est dans la main de l'entité.
     *  Permet de faire des actions personnalisées lorsque l'action manger est activé.
     *
     * @see fr.sae.terraria.modele.entities.items.Meat
     */
    void eat();
}
