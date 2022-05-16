package fr.sae.terraria.modele.entities.entity;


public class Gravity
{
    // Constantes
    public static final double VALUE = 9.81;

    // Horloges
    protected double xTimer = .0;
    protected double yTimer = .0;
    protected double speed = .3;

    // Depart initial
    protected double degInit;
    protected double vInit;
    protected double xInit;
    protected double yInit;


    public double formulaOfTrajectory(Entity entity)
    {
        this.degInit = -90;

        if (entity.offset[1] == 0) {
            this.xTimer = 0; this.yTimer = 0;
            this.xInit = entity.getX();
            this.yInit = entity.getY();
            this.vInit = entity.getVelocity();

            return yInit;
        }
        double traj = (4.905 * (yTimer*yTimer)) + ((Math.sin(degInit) * (vInit*10)) * yTimer) + yInit;
        System.out.println(yTimer);
        System.out.println(degInit);
        System.out.println(vInit);
        this.xTimer += this.speed;
        this.yTimer += this.speed;

        return traj;
    }
}
