package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.entities.entity.Entity;


public class Player extends Entity
{
    /**
     * @param x La position du joueur en X
     * @param y La position du joueur en Y
     */
    public Player(int x, int y) { super(x, y); }

    public void updates()
    {
        super.updates();

        // Applique les déplacements selon les valeurs de l'offset
        this.setX(this.x.get() + this.offset[0] * this.velocity);
        // this.setY(this.y.get() + this.offset[1] * this.velocity);
        if (offset[1] >= 0)
            this.setY(this.gravity.formulaOfTrajectory(this));
        else this.setY(getY() + 2);

        this.rect.update(x.get(), y.get());
    }
}
