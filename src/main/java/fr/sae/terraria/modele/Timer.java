package fr.sae.terraria.modele;

import javafx.beans.property.SimpleIntegerProperty;

public class Timer {
    private SimpleIntegerProperty minutes;
    private SimpleIntegerProperty days;

    public Timer(){
        minutes = new SimpleIntegerProperty(0);
        days = new SimpleIntegerProperty(0);
    }

    public void update(){
        if (getMinutes()+1 == 1440){
            days.setValue(getDays()+1);
            minutes.setValue(0);
        }else minutes.setValue(getMinutes()+1);
    }

    public int getMinutes() {return minutes.get();}
    public SimpleIntegerProperty minutesProperty() {return minutes;}
    public int getDays() {return days.get();}
    public SimpleIntegerProperty daysProperty() {return days;}
}
