package fr.sae.terraria.modele.entities;


public abstract class Entity
{

    private int pv;
    protected int[] offset;
    protected int x;
    protected int y;
    protected double velocity;

    public Entity(int x, int y, int pv, double velocity)
    {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
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
    public double getX() { return x.get(); }
    public double getY() { return y.get(); }
    public int getPv() { return pv; }
    public double getVelocity() { return velocity; }

    public void setPv(int pv) { this.pv = pv; }
    public void setVelocity(double velocity) { this.velocity = velocity; }
    public void setX(double x) { this.x.setValue(x); }
    public void setY(double y) { this.y.setValue(y); }
}


