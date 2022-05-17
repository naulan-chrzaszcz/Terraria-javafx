package fr.sae.terraria.modele;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.scene.input.KeyCode;

import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        player.setY(8*heightTile);

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
        getPlayer().jump();
        int tileX = (int) (this.player.getX()/widthTile);
        int tileY = (int) (this.player.getY()/heightTile);
        int tileLeft = 0;
        int tileRight = 0;
        int tileBottom = 0;
        int tileTop = 0;

        for (Entity e : this.entities) if (this.player.getRect().collideRect(e.getRect()) != null)
        {
             tileLeft =  this.tileMaps.getTile(tileX, tileY);
             tileRight = this.tileMaps.getTile(tileX+1, tileY);
             tileBottom = this.tileMaps.getTile(tileX, tileY+1);
             tileTop =    this.tileMaps.getTile(tileX, tileY-1);
            break;
            // Revient en arriere
        }
        if (tileLeft != 0 || tileRight != 0)
            this.getPlayer().rollback();
        else if (tileBottom == 0)
            this.getPlayer().fall();
        else if (tileTop != 0)
            this.getPlayer().rollback();
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
