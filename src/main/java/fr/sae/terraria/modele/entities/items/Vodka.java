package fr.sae.terraria.modele.entities.items;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.EatableObjectType;


/**
 * <h1>EasterEgg</h1>
 * <h2><u>Description:</u></h2>
 * <p>Ce drop lorsque le joueur casse des hautes herbes et une fois bu, l'écran sera troublé</p>
 */
public class Vodka extends Item implements EatableObjectType
{
    public static final double DROP_RATE = .1;

    private final Environment environment;


    public Vodka(final Environment environment)
    {
        super();
        this.environment = environment;
    }

    @Override public void eat()
    {

    }
}