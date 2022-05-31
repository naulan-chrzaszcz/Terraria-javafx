package fr.sae.terraria.modele.entities.entity;


public interface MovableObjectType
{
    /** Modifie l'offset qui permet de le déplacer vers la droite */
    void moveRight();
    /** Modifie l'offset qui permet de le déplacer vers la gauche */
    void moveLeft();
    /** Modifie l'offset qui permet de le faire sauter */
    void jump();
    /** Modifie l'offset qui permet de tomber */
    void fall();
    /** Fait bouger le joueur selon les instructions mise dans move() */
    void move();
}
