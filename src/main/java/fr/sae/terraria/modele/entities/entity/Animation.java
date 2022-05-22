package fr.sae.terraria.modele.entities.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public class Animation
{
    private DoubleProperty frame;
    private int endFrame = 3;


    public Animation() { this.frame = new SimpleDoubleProperty(); }

    public void loop()
    {
        if ((int) (frame.get()) < endFrame)
            frame.setValue(frame.getValue() + .1);
        else frame.setValue(0);
    }

    public void reset() { frame.setValue(0); }


    public DoubleProperty getFrame() { return this.frame; }

    public void setEndFrame(int newEndFrame) { this.endFrame = newEndFrame; }
}
