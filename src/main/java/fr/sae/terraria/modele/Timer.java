package fr.sae.terraria.modele;

import javafx.beans.property.SimpleIntegerProperty;

public class Timer {
    private SimpleIntegerProperty minuts;
    private SimpleIntegerProperty hours;
    private SimpleIntegerProperty days;

    public Timer(){
        minuts = new SimpleIntegerProperty(0);
        hours = new SimpleIntegerProperty(0);
        days = new SimpleIntegerProperty(0);
    }

    public void update(){
        System.out.println(getDays());
        if (getMinuts()+1 == 60){
            if (getHours()+1 == 24){
                days.setValue(getDays()+1);
                hours.setValue(0);
            }else {
                hours.setValue(getHours()+1);
                minuts.setValue(0);
            }
        }else minuts.setValue(getMinuts()+1);
        System.out.println("Time : " + getDays() + "h :" + getHours() + "m : " + getMinuts());
    }

    public int getMinuts() {return minuts.get();}
    public SimpleIntegerProperty minutsProperty() {return minuts;}
    public int getHours() {return hours.get();}
    public SimpleIntegerProperty hoursProperty() {return hours;}
    public int getDays() {return days.get();}
    public SimpleIntegerProperty daysProperty() {return days;}
}
