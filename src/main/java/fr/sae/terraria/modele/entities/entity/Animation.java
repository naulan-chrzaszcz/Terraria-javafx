package fr.sae.terraria.modele.entities.entity;

import fr.sae.terraria.vue.entities.PlayerView;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


/**
 * <h1>Animation</h1>
 * <h2><u>Description:</u></h2>
 * <p>Gère uniquement la valeur du frame pour que la vue sache sur quel frame elle doit se placer</p>
 * @see PlayerView
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

    /** Spécifie à quelle frame l'animation sur le Sprite Sheet doit s'arrêter */
    public void setEndFrame(int newEndFrame) { this.endFrame = newEndFrame; }
}
