package fr.sae.terraria.modele.entities.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


/**
 * <h1><b>La classes Entity</b></h1>
 * <h2><u>Description:</u></h2>
 *
 * <p>Correspond à un element physique qui doit avoir des coordonnées sur un environment.</p>
 * <p>contient des fonctions privates qui peuvent êtres implémenter grâce au interface mise à disposition au sain du même package</p>
 * <p></p>
 * <p>C'est interfaces agissent comme une sorte de pâte à modelé, ils permettent surtout de
 * rendre public les fonctions de Entity qui sont eux protected</p>
 *
 * <h2><u>Comment l'utiliser ?</u></h2>
 * <p>Il suffit d'extend des objects qui vous souhaitez êtres des entitées</p>
 * <p><u>Exemple de code qui hérite de Entity:</u></p>
 * <img src="https://raw.githubusercontent.com/NaulaN/SAE-Terraria-Like/develop/src/main/resources/fr/sae/terraria/docs/ExampleExtendEntity.PNG"/>
 * <p>Donc sur cette exemple dans le jeu, le lapin est considèrer comme une entités.</p>
 * <p></p>
 * <p>Toutes les méthodes/fonctions protected sont appelable vers l'extérieur d'un classes qui l'hérite grâce à des interfaces.</p>
 * <p>Donc pour avoir correctement les fonctions:</p>
 *
 * <p>Sont implementable grâce à l'interface MovableObjectType</p>
 * @see CollideObjectType
 * @see ReproductiveObjectType
 * @see StowableObjectType
 * @see BreakableObjectType
 * @see CollapsibleObjectType
 * @see PlaceableObjectType
 * @see EatableObjectType
 *
 * @author CHRZASZCZ Naulan
 */
public abstract class Entity
{
    public static final int IS_FALLING = -1;
    public static final int IS_JUMPING = 1;
    public static final int IS_MOVING_LEFT = -1;
    public static final int IS_MOVING_RIGHT = 1;
    public static final int IDLE = 0;

    protected final DoubleProperty pv;
    protected final DoubleProperty x;
    protected final DoubleProperty y;

    protected Animation animation = null;
    protected Rect rect = null;
    protected double pvMax;



    /**
     * @param x La localisation de l'entité a l'horizontal
     * @param y La localisation de l'entité a la verticale
     */
    protected Entity(int x, int y)
    {
        super();

        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.pv = new SimpleDoubleProperty(0);

        this.pvMax = this.pv.get();
    }

    protected Entity() { this(0, 0); }

    /**
     * Il permet à chaque passage de la boucle du jeu, de faire diverses fonctions liée à l'Entité.
     *  Certaine fonction qui sera implementé grâce à des interfaces sera probablement obligatoirement mise dans cette fonction.
     */
    public abstract void updates();

    public DoubleProperty getPvProperty() { return this.pv; }
    public DoubleProperty getXProperty() { return this.x; }
    public DoubleProperty getYProperty(){ return this.y; }
    public Animation getAnimation() { return this.animation; }
    public Rect getRect() { return this.rect; }
    public double getPvMax() { return this.pv.get(); }
    public double getPv() { return this.pv.get(); }
    public double getX() { return this.x.get(); }
    public double getY() { return this.y.get(); }

    public void setRect(int width, int height) { this.rect = new Rect(x.get(), y.get(), width, height); }
    public void setPv(double pv) { this.pv.set(pv); this.pvMax = pv;}
    public void setX(double x) { this.x.set(x); }
    public void setY(double y) { this.y.set(y); }
}


