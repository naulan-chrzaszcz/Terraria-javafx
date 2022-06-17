package fr.sae.terraria.modele;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class Clock
{
    public static final int ONE_MINUTE_INGAME = 37;
    public static final int MINUTES_IN_A_DAY = 1440;
    public static final int EIGHT_AM_INGAME = 480;

    private final SimpleIntegerProperty minutes;
    private final SimpleIntegerProperty days;
    private final SimpleDoubleProperty opacityNightFilter;


    public Clock()
    {
        super();
        opacityNightFilter = new SimpleDoubleProperty(.0);
        this.minutes = new SimpleIntegerProperty(Clock.EIGHT_AM_INGAME);
        this.days = new SimpleIntegerProperty(1);
    }

    public void updates(int ticks)
    {
        // si environ 1 minute passe irl, le timer dans le jeu augmente de 10 minutes
        if (ticks%Clock.ONE_MINUTE_INGAME == 0) {
            if (this.getMinutes()+1 == Clock.MINUTES_IN_A_DAY) {
                this.days.setValue(getDays()+1);
                this.minutes.setValue(0);
            } else this.minutes.setValue(getMinutes()+1);
            updateOpacity();
        }
    }

    public SimpleIntegerProperty minutesProperty() { return this.minutes; }
    public SimpleIntegerProperty daysProperty() { return this.days; }

    private void updateOpacity()
    {
        if (this.getMinutes() > Clock.MINUTES_IN_A_DAY/2)
            this.opacityNightFilter.set(((this.getMinutes()*(2. /* Compensation temps */))/Clock.MINUTES_IN_A_DAY) - (1.1 /* Décallage */));
        else this.opacityNightFilter.set(((Clock.MINUTES_IN_A_DAY - (this.getMinutes()*(4. /* Compensation temps */)))/Clock.MINUTES_IN_A_DAY) - (.1 /* Décallage */));
    }

    public int getMinutes() { return this.minutes.get(); }
    public int getDays() { return this.days.get(); }

    public double getOpacityNightFilter() {
        return opacityNightFilter.get();
    }

    public SimpleDoubleProperty opacityNightFilterProperty() {
        return opacityNightFilter;
    }

    public void setMinutes(int newMinutes) { this.minutes.set(newMinutes); }
}
