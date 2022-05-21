package fr.sae.terraria.modele;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import fr.sae.terraria.modele.entities.*;
import fr.sae.terraria.modele.entities.entity.Entity;


public class Environment
{
    private final List<Entity> entities;

    private final TileMaps tileMaps;
    private final Player player;

    private int widthTile;
    private int heightTile;
    private int ticks = 0;


    public Environment(TileMaps tileMaps, int widthTile, int heightTile)
    {
        this.tileMaps = tileMaps;

        this.widthTile = widthTile;
        this.heightTile = heightTile;
        this.entities = new ArrayList<>();

        this.player = new Player(0,0);
        this.player.setVelocity(5);
        this.player.setPv(20);
        this.player.setX(widthTile);
        this.player.setY(7*heightTile);
        this.player.setRect(widthTile, heightTile);
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

            this.getPlayer().updates();
            this.ticks++;
        }));

        loop.getKeyFrames().add(keyFrame);
        loop.play();
    }

    private void collide()
    {
        int defaultX = (int) ((this.player.getX() + ((this.player.offset[0] == 1) ? widthTile : 0))/widthTile);
        int defaultY = (int) ((this.player.getY() + ((this.player.offset[1] == -1) ? heightTile : 0))/heightTile);

        int topTile = (int) (this.player.getY()/heightTile) - 1;
        int bottomTile = (int) ((this.player.getY() + heightTile)/heightTile);
        int leftTile = (int) (this.player.getX()/widthTile);
        int rightTile = (int) ((this.player.getX() + widthTile)/widthTile);

        if (this.player.offset[1] == 0 && tileMaps.getTile(defaultX, topTile) != TileMaps.SKY)
            this.player.offset[1] = -1;
        else if (this.player.offset[1] == 0 && tileMaps.getTile(defaultX, bottomTile) != TileMaps.SKY)
            this.player.offset[1] = 0;

        if (this.player.offset[0] == -1 && tileMaps.getTile(leftTile, defaultY) != TileMaps.SKY)
            this.player.offset[0] = 0;
        else if (this.player.offset[0] == 1 && tileMaps.getTile(rightTile, defaultY) != TileMaps.SKY)
            this.player.offset[0] = 0;

        if (tileMaps.getTile(defaultX, bottomTile) == TileMaps.SKY)
            this.player.offset[1] = -1;
        if (tileMaps.getTile((int) (this.player.getX()/widthTile), ((int) ((this.player.getY() + heightTile)/heightTile))) != TileMaps.SKY ||
                tileMaps.getTile((int) ((this.player.getX() + widthTile)/widthTile), ((int) ((this.player.getY() + heightTile)/heightTile))) != TileMaps.SKY)
            for (Entity e : entities)
                if (this.player.getRect().collideRect(e.getRect())) {
                    int middlePoint = (int) ((this.player.getX() + (widthTile/2))/widthTile);
                    int bottomCornerLeft = ((int) ((this.player.getY() + heightTile)/heightTile));

                    if (tileMaps.getTile(middlePoint, bottomCornerLeft) == TileMaps.SKY)
                        this.player.offset[1] = -1;
                    if (this.player.offset[0] == -1 || this.player.offset[0] == 1)
                        if (tileMaps.getTile(middlePoint, bottomCornerLeft) == TileMaps.SKY) {
                            if (this.player.offset[0] == -1 && this.player.getX() > e.getX())
                                this.player.offset[0] = 0;
                            else if (this.player.offset[0] == 1 && this.player.getX() < e.getX())
                                this.player.offset[0] = 0;
                        }

                }
    }

    /** Evite que le joueur sort de la carte. */
    private void worldLimit()
    {
        if (player.offset[0] == -1 && player.getX() < 0)
            player.offset[0] = 0;
        if (player.offset[0] == 1 && player.getX() > widthTile*tileMaps.getWidth())
            player.offset[0] = 0;
    }


    public List<Entity> getEntities() { return entities; }
    public Player getPlayer() { return player; }
}
