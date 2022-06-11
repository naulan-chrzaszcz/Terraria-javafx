package fr.sae.terraria.modele.entities.tools;

import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public abstract class Tool implements StowableObjectType
{
    public static final double CRAFTED_WITH_WOOD = 1.;
    public static final double CRAFTED_WITH_IRON = 1.5;

    public static final int DEFAULT_DURABILITY = 100;

    protected final IntegerProperty durability;

    protected double material;


    protected Tool(final int durability)
    {
        super();
        this.durability = new SimpleIntegerProperty(durability);
    }

    /** Use l'outil */
    public abstract void use();

    public static void DEFAULT_WEAR(final Tool tool)
    {
        if (tool.durability.get() > 0)
            tool.durability.set(tool.durability.get() - 1);
    }


    public double getMaterial() { return this.material; }
}
