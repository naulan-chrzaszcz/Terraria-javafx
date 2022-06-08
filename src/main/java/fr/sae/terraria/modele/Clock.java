package fr.sae.terraria.modele;

import javafx.beans.property.SimpleIntegerProperty;


public class Clock
{
    public static final int ONE_MINUTE_INGAME = 37;
    public static final int ONE_DAY_INGAME = 1440;
    public static final int MIDNIGHT_INGAME =0;

    public static final int MINUTES_IN_A_DAY = 1440;
    public static final int FOUR_PM_INGAME = 960;
    public static final int EIGHT_AM_INGAME = 480;

    private final SimpleIntegerProperty minutes;
    private final SimpleIntegerProperty days;


    public Clock()
    {
        super();

        this.minutes = new SimpleIntegerProperty(Clock.EIGHT_AM_INGAME);
        this.days = new SimpleIntegerProperty(1);
    }

    public void updates(int ticks)
    {
        // si environ 1 minute passe irl, le timer dans le jeu augmente de 10 minutes
        if (ticks%Clock.MINUTES_IN_A_DAY == 0) {
            if (this.getMinutes()+1 == Clock.ONE_DAY_INGAME) {
                this.days.setValue(getDays()+1);
                this.minutes.setValue(0);
            } else this.minutes.setValue(getMinutes()+1);
        }
    }

    public SimpleIntegerProperty minutesProperty() { return this.minutes; }
    public SimpleIntegerProperty daysProperty() { return this.days; }


    public int getMinutes() { return this.minutes.get(); }
    public int getDays() { return this.days.get(); }

    public void setMinutes(int newMinutes) { this.minutes.set(newMinutes); }
}
