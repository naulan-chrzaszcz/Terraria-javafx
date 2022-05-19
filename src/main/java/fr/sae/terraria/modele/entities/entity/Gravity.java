package fr.sae.terraria.modele.entities.entity;


public class Gravity
{
    // Constantes
    public static final double VALUE = 9.81;

    // Horloges
    public double timer = .0;
    public double speed = .3;

    // Depart initial
    public double degInit;
    public double vInit;
    public double xInit;
    public double yInit;


    public void formulaOfTrajectory(Entity entity)
    {
        this.degInit = -90;

        // double trajX = -entity.offset[0] * (Math.cos(degInit) * (vInit*10) * xTimer) + xInit;
        entity.setY(((4.905 * (timer * timer)) + ((Math.sin(degInit) * (vInit * 7)) * timer)) + yInit);
        this.clock();
    }

    public void clock() { this.timer += this.speed; }
}
