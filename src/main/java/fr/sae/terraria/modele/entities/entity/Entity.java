package fr.sae.terraria.modele.entities.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public abstract class Entity
{
    // Property variables
    protected DoubleProperty x;
    protected DoubleProperty y;

    protected Rect rect = null;         // Permet d'avoir une hitbox

    protected boolean fall = false;     // Savoir si le joueur est dans le vide
    protected double velocity = 1;
    // Permet d'avoir des cœurs et des demi-cœurs sur la barre de vie
    protected double pv = 3;    // 0 >= pv && 5 >= pv
    // Permet de savoir dans quelle direction se dirige le joueur.
    protected int[] offset;     // offset[0] >= -1 && offset[0] <= 1 et offset[1] >= -1 && offset[1] <= 1


    /**
     * @param x La localisation de l'entité a l'horizontal
     * @param y La localisation de l'entité a la verticale
     */
    protected Entity(int x, int y)
    {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);

        this.offset = new int[2];
    }

    /**
     * Permet de mettre à jour les valeurs qui concerne l'entité
     *   et qui doit rester au sain de l'objet
     */
    public abstract void updates();

    public DoubleProperty getXProperty() { return x; }
    public DoubleProperty getYProperty(){ return y; }
    public Rect getRect() { return rect; }
    public double getX() { return x.get(); }
    public double getY() { return y.get(); }
    public double getPv() { return pv; }
    public double getVelocity() { return velocity; }
    public boolean getFall() { return fall; }

    public void setPv(int pv) { this.pv = pv; }
    public void setVelocity(double velocity) { this.velocity = velocity; }
    public void setX(double x) { this.x.setValue(x); }
    public void setY(double y) { this.y.setValue(y); }
    public void setRect(int width, int height) { this.rect = new Rect(x.get(), y.get(), width, height); }
    public void setFall(boolean v) { fall = v; }
}


