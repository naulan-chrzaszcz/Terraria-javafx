package fr.sae.terraria.modele.entities;


import fr.sae.terraria.modele.entities.entity.Entity;

public class Player extends Entity
{
    private double frame; // TODO faire une animation


    public Player(int x, int y)
    {
        super(x, y);

        this.pv = pv;
        this.velocity = velocity;
        this.frame = 1;
    }

    public void moveRight() { this.offset[0] = 1; }

    public void moveLeft() { this.offset[0] = -1; }

    public void idle() { this.offset[0] = 0; this.offset[1] =0;}

    public void updates()
    {
        this.setX(this.x.get() + this.offset[0] * this.velocity);
        this.setY(this.y.get() + this.offset[1] * this.velocity);

        // TODO faire les frame
        if(frame == 4) {
            frame = 0;
        }

        this.rect.update(x.get(), y.get());
    }
}
