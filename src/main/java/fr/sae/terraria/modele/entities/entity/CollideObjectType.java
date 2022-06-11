package fr.sae.terraria.modele.entities.entity;


/**
 * <h1>CollideObjectType</h1>
 * <h2>Interface utile à un objet qui hérite d'Entity</h2>
 * <h3><u>Description:</u></h3>
 * <p>Permet de discerner les entités qui ne sont pas des objects et qui ont des collisions avec d'autres entités</p>
 * <p>Cela peut être utile sur les elements qui doivent êtres au premier plan et d'autre element qui doit êtres à l'arrière plan</p>
 *
 * @see Entity
 * @author CHRZASZCZ Naulan
 */
public interface CollideObjectType
{
    int COLLISION_TOLERANCE = 3;    // Permet d'avoir une certaine tolerance avec le bloc qui va rencontrer, évite des bugs


    /**
     * Permet d'appliquer des collisions à l'entité qui l'utilise sur son environment.
     * Pour pouvoir être fonctionnelle, elle doit faire appel à "super.collide()" et de ranger son return dans une variable
     * de type Map pour permettre d'avoir des données relatives sur les endroit ou l'entité entre en collision avec une autre
     *
     * @see Entity
     */
    void collide();
}
