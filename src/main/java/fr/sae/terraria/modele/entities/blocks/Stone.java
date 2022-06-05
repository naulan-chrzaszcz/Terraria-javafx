package fr.sae.terraria.modele.entities.blocks;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.PlaceableObjectType;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.util.Duration;

import javax.swing.*;


public class Stone extends Block implements StowableObjectType, CollideObjectType, PlaceableObjectType
{
    private final Environment environment;


    public Stone(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;

        this.pv.set(3);
    }

    @Override public void updates() { /* TODO document why this method is empty */ }

    @Override public void collide() { /* NE RIEN REMPLIR */ }

    @Override public void breaks()
    {
        Environment.playSound("sound/brick" + ((int) (Math.random()*2)+1) + ".wav", false);
        this.environment.getPlayer().pickup(this);

        // Animation de cassure du bloc
        Timeline timeline = new Timeline();
        timeline.setCycleCount(5);
        int[] time = new int[1];
        double xInit = this.getX(); double yInit = this.getY();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(Terraria.TARGET_FPS), (ev -> {
            this.setX(xInit + (Math.cos(time[0])*this.environment.scaleMultiplicatorWidth));
            this.setY(yInit + (-Math.sin(time[0]*this.environment.scaleMultiplicatorHeight)));

            time[0]++;
        }));
        timeline.getKeyFrames().add(keyFrame);
        // Faire revenir le bloc à sa position initiale
        timeline.statusProperty().addListener(c -> {
            this.setX(xInit);
            this.setY(yInit);
        });
        timeline.play();

        if (this.getPv() <= 0) {
            int yIndexTile = (int) (getY()/environment.heightTile);
            int xIndexTile = (int) (getX()/environment.widthTile);
            this.environment.getTileMaps().setTile(TileMaps.SKY, yIndexTile, xIndexTile);
            this.environment.getEntities().remove(this);
        }
        this.setPv(this.getPv() - 1);
    }

    @Override public void place(int x, int y)
    {
        Environment.playSound("sound/axchop.wav", false);
        Entity entity = new Stone(this.environment, x*environment.widthTile, y*environment.heightTile);
        entity.setRect(environment.widthTile, environment.heightTile);

        environment.getTileMaps().setTile(TileMaps.STONE, y, x);
        environment.getEntities().add(0, entity);
    }
}
