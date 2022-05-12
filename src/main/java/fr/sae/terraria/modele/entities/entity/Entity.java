package fr.sae.terraria.modele.entities.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public abstract class Entity
{
    protected Rect rect;

    protected DoubleProperty x;
    protected DoubleProperty y;

    protected int[] offset;
    protected int pv = 3;
    protected double velocity = 1;


    public Entity(int x, int y)
    {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);

        this.offset = new int[2];
    }

    public abstract void updates();

    public DoubleProperty getXProperty() { return x; }
    public DoubleProperty getYProperty(){ return y; }
    public ImageView getImageView() { return imgView; }
    public Rectangle getRect() { return rect; }
    public double getX() { return x.get(); }
    public double getY() { return y.get(); }
    public int getPv() { return pv; }
    public double getVelocity() { return velocity; }

    public void setPv(int pv) { this.pv = pv; }
    public void setVelocity(double velocity) { this.velocity = velocity; }
    public void setX(double x) { this.x.setValue(x); }
    public void setY(double y) { this.y.setValue(y); }
    public void setRect(int width, int height) { this.rect = new Rect(x.get(), y.get(), width, height); }
}


