package fr.sae.terraria.modele;

import javafx.beans.property.SimpleIntegerProperty;


public class Clock
{
    public static final int ONE_MINUTE_INGAME = 37;
    public static final int ONE_DAY_INGAME = 1440;

    private final SimpleIntegerProperty minutes;
    private final SimpleIntegerProperty days;


    public Clock()
    {
        minutes = new SimpleIntegerProperty(0);
        days = new SimpleIntegerProperty(0);
    }

    public void updates(int ticks)
    {
        // si environ 1 minute passe irl, le timer dans le jeu augmente de 10 minutes
        if (ticks% Clock.ONE_MINUTE_INGAME == 0) {
            if (getMinutes()+1 == ONE_DAY_INGAME) {
                days.setValue(getDays()+1);
                minutes.setValue(0);
            } else minutes.setValue(getMinutes()+1);
        }
    }

    public SimpleIntegerProperty minutesProperty() { return minutes; }
    public SimpleIntegerProperty daysProperty() { return days; }


    public int getMinutes() { return minutes.get(); }
    public int getDays() { return days.get(); }
}
