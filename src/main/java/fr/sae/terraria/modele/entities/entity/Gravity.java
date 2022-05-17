package fr.sae.terraria.modele.entities.entity;


import fr.sae.terraria.modele.entities.Player;

public class Gravity
{
    // Constantes
    public static final double VALUE = 9.81;

    // Horloges
    protected double xTimer = .0;
    protected double yTimer = .0;
    protected double speed = .3;

    // Depart initial
    protected double flightTime;
    protected double degInit;
    protected double vInit;
    protected double xInit;
    protected double yInit;


    public void formulaOfTrajectory(Entity entity)
    {
        this.degInit = -90;

        if (entity.offset[1] == 0) {
            this.xInit = entity.getX(); this.yInit = entity.getY();
            this.vInit = entity.getVelocity();
            this.xTimer = .0; this.yTimer = .0;
            this.flightTime = (vInit*Math.sin(degInit) + Math.sqrt(Math.pow(vInit*Math.sin(degInit), 2) + 2*Gravity.VALUE*yInit))/Gravity.VALUE;

        }
        //TODO nettoyer
        if (((Player) entity).air) {
            //double trajX = -entity.offset[0] * (Math.cos(degInit) * (vInit*10) * xTimer) + xInit;
            entity.setY(((4.905 * (yTimer * yTimer)) + ((Math.sin(degInit) * (vInit * 10)) * yTimer)) + yInit);

            if (this.flightTime / 2 <= this.yTimer)
                System.out.println("ehhiuhzihiehzi");

            this.xTimer += this.speed;
            this.yTimer += this.speed;
        }
    }

    public boolean burn() {
        return this.flightTime / 2 <= this.yTimer;
    }
}
