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

        // TODO le mettre où il y a la vue du joueur
        this.frame.addListener((obs, oldV, newV) -> {
            System.out.println(oldV);
            System.out.println(newV);
        });
    }

    public void loop()
    {
        if ((int) (frame.get()) != 4)
            frame.setValue(frame.getValue() + .1);
        else frame.setValue(0);
    }
}
