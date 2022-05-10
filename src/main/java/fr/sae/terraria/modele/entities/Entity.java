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
        this.x = x;
        this.y = y;
        this.pv=pv;
        this.velocity =velocity;
    }

    public Entity(int x, int y, double velocity){
        this.x = x;
        this.y = y;
        this.velocity=velocity;
    }

    public abstract void updates();

    public int getX() { return x; }
    public int getY(){ return y; }
    public int getPv() { return pv; }
    public double getVelocity() { return velocity; }

    public void setPv(int pv) { this.pv = pv; }
    public void setVelocity(double velocity) { this.velocity = velocity; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}


