package fr.sae.terraria.modele.entities.blocks;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.BreakableObjectType;
import fr.sae.terraria.modele.entities.entity.Entity;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


public abstract class  Block extends Entity implements BreakableObjectType
{


    protected Block(int x, int y) { super(x, y); }

    public static void breakAnimation(Environment environment, Block block)
    {
        Timeline timeline = new Timeline();
        double xInit = block.getX();
        double yInit = block.getY();
        int animationCycleCount = 5;
        int[] time = new int[1];

        // Animation de cassure du bloc
        timeline.setCycleCount(animationCycleCount);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(Terraria.TARGET_FPS), (ev -> {
            block.setX(xInit + (Math.cos(time[0])*environment.scaleMultiplicatorWidth));
            block.setY(yInit + (-Math.sin(time[0]*environment.scaleMultiplicatorHeight)));

            time[0]++;
        }));
        timeline.getKeyFrames().add(keyFrame);
        // Faire revenir le bloc à sa position initiale lorsque l'animation est arrêté
        timeline.statusProperty().addListener(c -> {
            block.setX(xInit);
            block.setY(yInit);
        });
        timeline.play();
    }

    @Override public abstract void updates();
}
