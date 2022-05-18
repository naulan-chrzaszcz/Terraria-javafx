package fr.sae.terraria.modele;

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
            this.player.idle();
            this.player.eventInput();
            this.collide();
            this.worldLimit();

            System.out.println(this.player.offset[0]);
            this.getPlayer().updates();
            this.ticks++;
        }));

        loop.getKeyFrames().add(keyFrame);
        loop.play();
    }

    private void collide()
    {
        for (Entity e : entities) {
            if (player.offset[0] != 0 && player.offset[0] <= 1 && player.offset[0] >= -1) {
                double futureLocation = player.getX()+(player.offset[0] * player.getVelocity());
                // Right
                if (player.offset[0] == 1 && (futureLocation + widthTile) >= e.getX())
                    if (tileMaps.getTile((int) (player.getX()/widthTile)+1, (int) (player.getY()/heightTile)) != 0)
                        player.offset[0] = 0;
                // Left
                if (player.offset[0] == -1 && futureLocation <= e.getX())
                    if (tileMaps.getTile((int) (player.getX()/widthTile), (int) (player.getY()/heightTile)) != 0)
                        player.offset[0] = 0;
            }

            if (player.offset[1] != 0 && player.offset[1] <= 1 && player.offset[1] >= -1) {
                double futureLocation = player.getY()+(player.offset[1] * player.getVelocity());
                if (player.offset[1] == 1 && (futureLocation + heightTile) >= e.getY())
                    player.offset[1] = 0;
                if (player.offset[1] == -1 && futureLocation <= e.getY())
                    player.offset[1] = 0;
            }
        }

        if (tileMaps.getTile(((int) player.getX()/widthTile), ((int) player.getY()/heightTile) + 1) == 0)
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
