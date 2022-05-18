package fr.sae.terraria.modele.entities.entity;


import fr.sae.terraria.modele.entities.Player;

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
    public double flightTime = 1;



    public double formulaOfTrajectory()
    {
        this.degInit = -90;
        double yValue;
        flightTime = (vInit*Math.sin(degInit) + Math.sqrt(Math.pow(vInit*Math.sin(degInit), 2) + 2*Gravity.VALUE*yInit))/Gravity.VALUE;

        //double trajX = -entity.offset[0] * (Math.cos(degInit) * (vInit*10) * xTimer) + xInit;
        yValue = ((4.905 * (timer * timer)) + ((Math.sin(degInit) * (vInit * 10)) * timer)) + yInit;

        this.timer += this.speed;
        return yValue;
    }
}
