package fr.sae.terraria.modele.entities;


public class Arrow extends Entity
{
    public Arrow(int x, int y, int velocity){
        super(x, y, velocity);
    }

    @Override
    public void updates() {
        this.x += this.offset[0] * this.velocity;
        this.y += this.offset[1] * this.velocity;
    }
}
