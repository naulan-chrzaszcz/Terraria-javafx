package fr.sae.terraria.modele.entities.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


/**
 * <h1>Animation</h1>
 * <h2><u>Description:</u></h2>
 * <p>Gére uniquement la valeurs du frame pour que la vue sachent sur quel frame doit afficher l'image.</p>
 * @see fr.sae.terraria.vue.PlayerView
 */
public class Animation
{
    public static final double FRAME_SPEED = .1;

    private final DoubleProperty frame;
    private int endFrame;


    public Animation()
    {
        super();

        this.frame = new SimpleDoubleProperty(0);
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
    public void reset() { this.frame.set(0); }


    public double getFrame() { return this.frame.get(); }
    public DoubleProperty getFrameProperty() { return this.frame; }

    /** Spécifie quand l'animation sur le Sprite Sheet doit s'arrêter */
    public void setEndFrame(int newEndFrame) { this.endFrame = newEndFrame; }
}
