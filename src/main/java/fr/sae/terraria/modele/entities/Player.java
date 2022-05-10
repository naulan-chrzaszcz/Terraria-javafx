package fr.sae.terraria.modele.entities;

public class Player extends Entity
{

    private double frame;// TODO faire une animation


    public Player(int x, int y, int pv, double velocity){
        super(x, y, pv, velocity);
        this.offset = new int[2];
        this.frame=1;
    }

    public void moveRight() {
        this.offset[0] = 1;
    }

    public void moveLeft() {
        this.offset[1] = -1;
    }

    public void updates(){
        this.setX(this.x.get() + this.offset[0] * this.velocity);
        this.setY(this.y.get() + this.offset[1] * this.velocity);

        if(frame == 4) {
            frame = 0;
        } //TODO faire les frame
    }










}
