package fr.sae.terraria.modele.entities;

public class Rabbit extends Entity{

    private int frame;
    public Rabbit(int x, int y, int pv, double velocity){
        super(x, y , pv, velocity);
    }
    public void updates(){
        this.x += this.offset[0] * this.velocity;
        this.y += this.offset[1] * this.velocity;

        if(frame == 4){ frame=0; }//TODO faire les frame
    }

}




