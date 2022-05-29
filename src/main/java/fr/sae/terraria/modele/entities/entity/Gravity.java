package fr.sae.terraria.modele.entities.entity;


public class Gravity
{
    // Constantes
    public static final double VALUE = 9.81;

    // Horloges
    public double timer = .0;
    public double speed = .3;

    // Depart initial
    public double degInit = -90;
    public double vInit;
    public double xInit;
    public double yInit;
    public double flightTime;

    // Formule de type ax²+bx+c ( c = yInit )
    private double a = Gravity.VALUE/2;

    public int amplitude = 10;


    public double formulaOfTrajectory()
    {
        flightTime = calcFlightTime();
        //double trajX = -entity.offset[0] * (Math.cos(degInit) * (vInit*10) * xTimer) + xInit;
        double yValue = ((a* (timer * timer)) + ((Math.sin(degInit) * (vInit * amplitude)) * timer)) + yInit;
        this.timer += this.speed;

        return yValue;
    }

    private double calcFlightTime()
    {
        // résolution de l'équation f'(x) = 0
        double res;
        if (degInit < 0)
            res = ((vInit * amplitude) * Math.sin(-degInit)) / Gravity.VALUE;
        else res = ((vInit * amplitude) * Math.sin(degInit)) / Gravity.VALUE;

        return res;
    }

    public void setXInit(double newXInit) { xInit = newXInit; }
    public void setYInit(double newYInit) { yInit = newYInit; }
}
