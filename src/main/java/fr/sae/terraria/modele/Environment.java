package fr.sae.terraria.modele;

import fr.sae.terraria.modele.entities.entity.Rect;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.scene.input.KeyCode;

import javafx.util.Duration;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;

import fr.sae.terraria.modele.entities.*;
import fr.sae.terraria.modele.entities.entity.Entity;

import static javafx.scene.input.KeyCode.*;


public class Environment
{
    private final List<Entity> entities;

    private final TileMaps tileMaps;
    private final Player player;

    private final int[] elementsSize;
    private int widthTile;
    private int heightTile;
    private int ticks = 0;


    public Environment(TileMaps tileMaps, int widthTile, int heightTile, int widthPlayer,int heightPlayer)
    {
        this.tileMaps = tileMaps;

        this.widthTile = widthTile;
        this.heightTile = heightTile;
        elementsSize = new int[] {widthTile*tileMaps.getWidth(), heightTile*tileMaps.getWidth(), widthPlayer, heightPlayer};
        entities = new ArrayList<>();

        player = new Player(0,0);
        player.setVelocity(5);
        player.setPv(20);
        player.setX(widthTile);
        player.setY(7*heightTile);

        gameLoop();
    }

    private void gameLoop()
    {
        Timeline loop = new Timeline();
        loop.setCycleCount(Animation.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.017), (ev -> {
            this.getPlayer().idle();     // Reset
            this.collide();

            this.worldLimit();

            this.ticks++;
            this.getPlayer().updates();
        }));

        loop.getKeyFrames().add(keyFrame);
        loop.play();
    }

    private void collide()
    {
        List<Rect> allRectEntities = new ArrayList<>();
        for (Entity e : entities)
            if (e.getRect().collideRect(player.getRect()) != null)
                allRectEntities.add(e.getRect());

        for (Rect r : allRectEntities) {
            if (player.getX() + widthTile >= r.get().getMinX())
                player.setX(r.get().getMinX());

            if (player.getY() + heightTile >= r.get().getMinY()) {
                player.rollbackY();
            } else if (player.getY() <= r.get().getMinY()) {
                player.rollbackY();
            } else player.air = true;
        }

        if (allRectEntities.isEmpty())
            player.fall();
    }

    /** Evite que le joueur sort de la carte. */
    private void worldLimit()
    {
        if (player.getX() < 0)
            player.setX(.0);
        if (player.getX() > elementsSize[0]-(elementsSize[2]*1.92))
            player.setX(elementsSize[0]-(elementsSize[2]*1.92));
    }


    public List<Entity> getEntities() { return entities; }
    public Player getPlayer() { return player; }
}
