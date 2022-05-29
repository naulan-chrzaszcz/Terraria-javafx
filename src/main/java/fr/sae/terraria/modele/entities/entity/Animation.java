package fr.sae.terraria.modele.entities.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public class Animation
{
    public static final double FRAME_SPEED = .1;

    private final DoubleProperty frame;
    private int endFrame;


    public Animation()
    {
        this.frame = new SimpleDoubleProperty();
        this.endFrame = 3;
    }

    /** Démarre l'animation de l'entité */
    public void loop()
    {
        if ((int) (this.getFrame()) < this.endFrame)
            this.frame.set(this.getFrame() + Animation.FRAME_SPEED);
        else this.reset();
    }

    /** Revient au début du Sprite Sheet */
    public void reset() { frame.set(0); }


    public double getFrame() { return this.frame.get(); }
    public DoubleProperty getFrameProperty() { return this.frame; }

    /** Spécifie quand l'animation sur le Sprite Sheet doit s'arrêter */
    public void setEndFrame(int newEndFrame) { this.endFrame = newEndFrame; }
}
