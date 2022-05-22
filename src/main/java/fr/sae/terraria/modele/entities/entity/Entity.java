package fr.sae.terraria.modele.entities.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public abstract class Entity
{
    // Property variables
    protected DoubleProperty x;
    protected DoubleProperty y;

    protected Animation animation = null;
    protected Rect rect = null;
    protected Gravity gravity;

    protected double velocity = 1;
    // Permet d'avoir des cœurs et des demi-cœurs sur la barre de vie
    protected double pvMax;
    protected DoubleProperty pv;    // 0 >= pv && 5 >= pv
    // Permet de savoir dans quelle direction se dirige le joueur.
    public int[] offset;     // offset[0] >= -1 && offset[0] <= 1 et offset[1] >= -1 && offset[1] <= 1


    /**
     * @param x La localisation de l'entité a l'horizontal
     * @param y La localisation de l'entité a la verticale
     */
    protected Entity(int x, int y)
    {
        this.gravity = new Gravity();

        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.pv = new SimpleDoubleProperty(0);
        this.pvMax = pv.get();

        this.offset = new int[2];
    }


    /** Permet de mettre à jour les valeurs qui concerne l'entité
     *   et qui doit rester au sain de l'objet */
    public abstract void updates();

    /** Modifie l'offset qui permet de le déplacer vers la droite */
    public void moveRight() { this.offset[0] = 1; }
    /** Modifie l'offset qui permet de le déplacer vers la gauche */
    public void moveLeft() { this.offset[0] = -1; }
    /** Permet de ne plus faire bouger l'entité */
    public void idle() { this.offset[0] = 0; this.offset[1] = 0; }
    /** Modifie l'offset qui permet de le faire sauter */
    public void jump() { this.offset[1] = 1; }
    /** Modifie l'offset qui permet de tomber */
    public void fall() { this.offset[1] = -1; }

    public DoubleProperty getPvProperty() { return this.pv; }
    public DoubleProperty getXProperty() { return this.x; }
    public DoubleProperty getYProperty(){ return this.y; }
    public Animation getAnimation() { return this.animation; }
    public Rect getRect() { return this.rect; }
    public double getPvMax() { return this.pv.get(); }
    public double getPv() { return this.pv.get(); }
    public double getX() { return this.x.get(); }
    public double getY() { return this.y.get(); }
    public double getVelocity() { return this.velocity; }
    public Gravity getGravity() { return gravity; }

    public void setPv(double pv) { this.pv.setValue(pv); this.pvMax = pv;}
    public void setX(double x) { this.x.setValue(x); }
    public void setY(double y) { this.y.setValue(y); }
    public void setVelocity(double velocity) { this.velocity = velocity; }
    public void setRect(int width, int height) { this.rect = new Rect(x.get(), y.get(), width, height); }
}


