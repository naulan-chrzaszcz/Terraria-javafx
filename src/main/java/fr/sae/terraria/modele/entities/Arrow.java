package fr.sae.terraria.modele.entities;


public class Arrow extends Entity
{
    public Arrow(int x, int y, int velocity){
        super(x, y, velocity);
    }

    @Override
    public void updates() {
        this.setX(this.x.get() + this.offset[0] * this.velocity);
        this.setY(this.y.get() + this.offset[1] * this.velocity);
    }
}
