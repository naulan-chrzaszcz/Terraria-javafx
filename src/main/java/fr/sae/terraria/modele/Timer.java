package fr.sae.terraria.modele;

import javafx.beans.property.SimpleIntegerProperty;


public class Timer
{
    private SimpleIntegerProperty minutes;
    private SimpleIntegerProperty hours;
    private SimpleIntegerProperty days;


    public Timer()
    {
        minutes = new SimpleIntegerProperty(0);
        hours = new SimpleIntegerProperty(0);
        days = new SimpleIntegerProperty(0);
    }

    public void update()
    {
        System.out.println(getDays());   // TODO supp
        if (getMinutes()+1 == 60) {
            if (getHours()+1 == 24) {
                days.setValue(getDays()+1);
                hours.setValue(0);
            } else {
                hours.setValue(getHours()+1);
                minutes.setValue(0);
            }
        } else minutes.setValue(getMinutes()+1);
        System.out.println("Time : " + getDays() + "h :" + getHours() + "m : " + getMinutes());
    }

    public SimpleIntegerProperty daysProperty() { return days; }
    public SimpleIntegerProperty hoursProperty() { return hours; }
    public SimpleIntegerProperty minutesProperty() { return minutes; }


    public int getMinutes() { return minutes.get(); }
    public int getHours() {return hours.get();}
    public int getDays() { return days.get(); }
}
