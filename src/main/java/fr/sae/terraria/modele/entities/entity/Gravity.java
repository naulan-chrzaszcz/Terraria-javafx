package fr.sae.terraria.modele.entities.entity;


public class Gravity
{
    // Constantes
    public static final double VALUE = 9.81;

    // Horloges
    public double timer = .0;
    public double speed = .15;

    // Depart initial
    public double degInit;
    public double vInit;
    public double xInit;
    public double yInit;
    public double flightTime = 1;



    public double formulaOfTrajectory()
    {
        //TODO : exploser la formule en a b c pour résoudre l'équation ... = x pour ne plus avoir le problème avec le saut
        this.degInit = -90;
        double yValue;
        flightTime = calcFlightTime();
        //double trajX = -entity.offset[0] * (Math.cos(degInit) * (vInit*10) * xTimer) + xInit;
        yValue = ((4.905 * (timer * timer)) + ((Math.sin(degInit) * (vInit * 10)) * timer)) + yInit;
        this.timer += this.speed;

        return yValue;
    }

    private double calcFlightTime() {
        // résolution de l'équation f'(x) = 0
        double res;
        if (degInit < 0)
            res = ((vInit * 10) * Math.sin(-degInit)) / Gravity.VALUE;
        else
            res = ((vInit * 10) * Math.sin(degInit)) / Gravity.VALUE;
        return res;
    }
}
