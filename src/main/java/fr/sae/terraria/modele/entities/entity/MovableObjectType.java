package fr.sae.terraria.modele.entities.entity;


/**
 * <h1>MovableObjectType</h1>
 * <h2>Interface utile à un objet qui hérite d'Entity</h2>
 * <img src="https://raw.githubusercontent.com/NaulaN/SAE-Terraria-Like/develop/src/main/resources/fr/sae/terraria/docs/FonctionsMovable.PNG"/>
 * <h2><u>Description:</u></h2>
 * <p>Il permet d'implementer les fonctions qui correspond à une entité qui doit ou qui peut bouger.</p>
 * <p>Il permet surtout de rendre public les functions qui sont en protected donc pas accessible à l'extérieur de la classe</p>
 * @see Entity
 *
 * @author CHRZASZCZ Naulan
 */
public interface MovableObjectType
{


    /**
     * Permet que l'entité bouge grâce aux offset modifier à chaque passage de la boucle du jeu.
     * Donc, il doit être appelé dans "updates()" pour que le joueur puisse être déplacé.
     *
     * @see Entity
     */
    void move();

    /**
     * Permet que l'entité lors de ses déplacements, qui ne sortent pas de la carte et aussi nis de l'écran.
     * Pour l'utilisé dans une classe que vous avez récemment créé, vous devez appeler la fonction dans Entité grâce à "super.worldLimit()".
     *
     * @see Entity
     */
    void worldLimit();

    /**
     * Modifie l'indice 0 dans la variable offset qui permet de le déplacer l'entité vers la droite.
     * Pour l'utilisé dans une classe que vous avez récemment créé, vous devez appeler la fonction dans Entité grâce à "super.moveRight()".
     *
     * @see Entity
     */
    void moveRight();

    /**
     * Modifie l'indice 0 dans la variable offset qui permet de le déplacer l'entité vers la gauche.
     * Pour l'utilisé dans une classe que vous avez récemment créé, vous devez appeler la fonction dans Entité grâce à "super.moveLeft()".
     *
     * @see Entity
     */
    void moveLeft();

    /**
     * Modifie l'indice 1 dans la variable offset qui permet d'informer l'entité qui est en train de sauter
     * Pour l'utilisé dans une classe que vous avez récemment créé, vous devez appeler la fonction dans Entité grâce à "super.jump()".
     *
     * @see Entity
     */
    void jump();

    /**
     * Modifie l'indice 1 dans la variable offset qui permet d'informer l'entité qui est en train de tomber
     * Pour l'utilisé dans une classe que vous avez récemment créé, vous devez appeler la fonction dans Entité grâce à "super.fall()".
     *
     * @see Entity
     */
    void fall();
}
