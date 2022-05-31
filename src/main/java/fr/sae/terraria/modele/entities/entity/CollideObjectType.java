package fr.sae.terraria.modele.entities.entity;


/**
 * <h1>CollideObjectType</h1>
 * <h2><u>Description:</u></h2>
 * <p>Permet de discerner les entités qui ne sont pas des objects qui doit rentré en collisions avec d'autre entité</p>
 * <p>Cela peut êtres utile sur les elements qui doit êtres au premier plan et d'autre element qui doit êtres à l'arrière plan</p>
 *
 * @see Entity
 * @author CHRZASZCZ Naulan
 */
public interface CollideObjectType
{
    int COLLISION_TOLERANCE = 3;    // Permet d'avoir une certaine tolerance avec le bloc qui va rencontrer, évite des bugs


    /**
     * Permet d'appliquer des collisions à l'entité qui l'utilise sur son environment.
     * Pour pouvoir être fonctionnel, il doit faire appelle à "super.collide()" est de la rangé dans une variable
     * de type Map pour permettre d'avoir des données relatif sur où il entre en collision
     *
     * @see Entity
     */
    void collide();
}
