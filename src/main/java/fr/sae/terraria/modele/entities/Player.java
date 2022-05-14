package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.entities.entity.Entity;


public class Player extends Entity
{
    private double prevX;
    private double prevY;
    private double frame = 0; // TODO faire une animation


    /**
     * @param x La position du joueur en X
     * @param y La position du joueur en Y
     */
    public Player(int x, int y) { super(x, y); }

    /** Modifie l'offset qui permet de le déplacer vers la droite */
    public void moveRight() { this.offset[0] = 1; }

    /** Modifie l'offset qui permet de le déplacer vers la gauche */
    public void moveLeft() { this.offset[0] = -1; }

    /** Permet de ne plus faire bouger le joueur */
    public void idle() { this.offset[0] = 0; this.offset[1] =0; }

    public void jump(int time, double yInit)
    {
        this.y.setValue(-1/2 * 9.81 * (time*time) + Math.sin(90) * velocity*10 * time + yInit);
        System.out.println(-1/2 * 9.81 * (time*time));
        System.out.println(Math.sin(90)* (velocity*10) * time + yInit);
    }

    public void rollback() { setX(prevX); setY(prevY); }

    public void updates()
    {
        this.prevX = x.get();
        this.prevY = y.get();
        // Applique les déplacements selon les valeurs de l'offset
        this.setX(this.x.get() + this.offset[0] * this.velocity);
        this.setY(this.y.get() + this.offset[1] * this.velocity);

        if(frame == 4) frame = 0;

        this.rect.update(x.get(), y.get());
    }
}
