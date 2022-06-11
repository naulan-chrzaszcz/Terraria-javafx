package fr.sae.terraria.modele.entities.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


/**
 * <h1><b>La classes Entity</b></h1>
 * <h2><u>Description:</u></h2>
 * <p>Correspond à un element physique qui doit avoir des coordonnées sur un environment.</p>
 *
 * @see CollideObjectType
 * @see ReproductiveObjectType
 * @see StowableObjectType
 * @see BreakableObjectType
 * @see CollapsibleObjectType
 * @see PlaceableObjectType
 * @see ConsumableObjectType
 *
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
     *  Certaine fonction qui seront implementés grâce à des interfaces seront obligatoirement mise dans cette fonction.
     */
    public abstract void updates();

    public DoubleProperty pvProperty() { return this.pv; }
    public DoubleProperty xProperty() { return this.x; }
    public DoubleProperty yProperty(){ return this.y; }


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


