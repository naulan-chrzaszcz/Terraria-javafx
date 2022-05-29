package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.Entity;
import javafx.beans.property.SimpleDoubleProperty;


public class Slime extends Entity implements CollideObjectType
{


    public Slime(int x, int y, double pv, double velocity)
    {
        super(x, y);

        this.pv = new SimpleDoubleProperty(pv);
        this.velocity = velocity;
    }

    public void updates() { }

    public void collide() { }
}
