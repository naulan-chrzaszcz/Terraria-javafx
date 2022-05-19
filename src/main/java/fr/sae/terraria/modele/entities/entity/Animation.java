package fr.sae.terraria.modele.entities.entity;


public class Animation
{
    private Entity entity;

    private double frame = 0; // TODO faire une animation


    public Animation(Entity entity)
    {
        this.entity = entity;
    }

    public void loop()
    {
        if(frame == 4)
            frame = 0;
    }
}
