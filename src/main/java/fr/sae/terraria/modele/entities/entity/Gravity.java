package fr.sae.terraria.modele.entities.entity;


public class Gravity
{
    public static final double VALUE = 9.81;
    public static final double SPEED = .3;

    public double timer = .0;
    public double degInit = -90;
    public double vInit;
    public double xInit;
    public double yInit;
    public double flightTime;

    public int amplitude = 10;


    public Gravity() { super(); }

    public double formulaOfTrajectory()
    {
        this.flightTime = this.calcFlightTime();
        // Formule de type ax²+bx+c ( c = yInit )
        double yValue = (((Gravity.VALUE/2) * (timer * timer)) + ((Math.sin(degInit) * (vInit * amplitude)) * timer)) + yInit;
        this.timer += Gravity.SPEED;

        return yValue;
    }

    private double calcFlightTime()
    {
        // résolution de l'équation f'(x) = 0
        if (this.degInit < 0)
            return ((this.vInit * this.amplitude) * Math.sin(-this.degInit)) / Gravity.VALUE;
        return ((this.vInit * this.amplitude) * Math.sin(this.degInit)) / Gravity.VALUE;
    }

    /** Modifie le xInit et le yInit pour modifier le point de départ du saut ou de l'endroit où il tombe */
    protected void setJumpPosInit(final Entity entity) { this.xInit = entity.getX(); this.yInit = entity.getY(); }


    public void setXInit(double newXInit) { xInit = newXInit; }
    public void setYInit(double newYInit) { yInit = newYInit; }
}
