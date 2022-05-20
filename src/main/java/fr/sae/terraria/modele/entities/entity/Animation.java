package fr.sae.terraria.modele.entities.entity;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Animation
{
    private Entity entity;

    private DoubleProperty frame; // TODO faire une animation


    public Animation(Entity entity)
    {
        this.entity = entity;

        this.frame = new SimpleDoubleProperty();
    }

    public void loop()
    {
        if ((int) (frame.get()) < 3)
            frame.setValue(frame.getValue() + .1);
        else frame.setValue(0);
    }


    public DoubleProperty getFrame() { return this.frame; }
}
