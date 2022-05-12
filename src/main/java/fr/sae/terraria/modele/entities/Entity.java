package fr.sae.terraria.modele.entities;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public abstract class Entity
{
    protected Image img = null;
    protected ImageView imgView = null;
    protected Rectangle rect;

    protected DoubleProperty x;
    protected DoubleProperty y;

    protected int[] offset;
    private int pv;
    protected double velocity;


    public Entity(int x, int y, int pv, double velocity)
    {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);

        this.rect = new Rectangle();
        rect.translateXProperty().bind(this.x);
        rect.translateYProperty().bind(this.y);

        this.pv=pv;
        this.velocity =velocity;
    }

    public Entity(int x, int y, double velocity){
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.velocity=velocity;
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
    public void setImage(Image img)
    {
        this.img = img;
        this.rect.heightProperty().bind(img.heightProperty());
        this.rect.widthProperty().bind(img.widthProperty());

        this.imgView = new ImageView(img);
        this.imgView.translateXProperty().bind(this.x);
        this.imgView.translateYProperty().bind(this.y);
    }
}


