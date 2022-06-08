package fr.sae.terraria.modele.entities.tools;

import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public abstract class Tool implements StowableObjectType
{
    public static final int DEFAULT_DURABILITY = 100;

    protected final IntegerProperty durability;



    protected Tool(final int durability)
    {
        super();
        this.durability = new SimpleIntegerProperty(durability);
    }

    /** Use l'outil */
    public abstract void use();
}
