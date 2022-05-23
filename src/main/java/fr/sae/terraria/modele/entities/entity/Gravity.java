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
    public double vInit = 5;
    public double xInit;
    public double yInit;
    public double flightTime = 1;

    // Formule de type ax²+bx+c ( c = yInit )
    public double a = Gravity.VALUE/2;


    public double formulaOfTrajectory()
    {
        double yValue;
        flightTime = calcFlightTime();
        //double trajX = -entity.offset[0] * (Math.cos(degInit) * (vInit*10) * xTimer) + xInit;
        yValue = ((a* (timer * timer)) + (b() * timer)) + yInit;
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

    public void setFall(double actualY){
        // change le timer afin que le joueur tombe
        System.out.println("ACTUAL Y"+actualY);
        System.out.println("Yinit : " + yInit);
        double delt = (b()*b()) - (4*a*(yInit-actualY));
        timer = (-b() + Math.sqrt(delt))/(2*a);
    }

    private double b(){
        return (Math.sin(degInit) * (vInit * 10));
    }
}
