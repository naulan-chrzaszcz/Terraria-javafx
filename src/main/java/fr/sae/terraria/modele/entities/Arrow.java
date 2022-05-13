package fr.sae.terraria.modele.entities;


import fr.sae.terraria.modele.entities.entity.Entity;

public class Arrow extends Entity
{


    public Arrow(int x, int y, int velocity)
    {
        super(x, y);

        this.velocity = velocity;
    }

    public void updates()
    {
        this.setX(this.x.get() + this.offset[0] * this.velocity);
        this.setY(this.y.get() + this.offset[1] * this.velocity);

        this.rect.update(x.get(), y.get());
    }
}
