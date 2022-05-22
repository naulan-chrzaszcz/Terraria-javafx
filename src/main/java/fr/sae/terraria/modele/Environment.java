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
        this.player.setPv(4);
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

    private void collide() {
        if (player.offset[0] != 0 || player.offset[1] != 0) {

            if (tileMaps.getTile((int) ((player.getX() + player.offset[0] * player.getVelocity()) / widthTile), (int) ((player.getY() + player.offset[1] * player.getVelocity()) / heightTile)) != 0 || tileMaps.getTile((int) ((player.getX() + player.offset[0] * player.getVelocity()) / widthTile), (int) ((player.getY() + player.offset[1] * player.getVelocity() + heightTile) / heightTile)) != 0 || tileMaps.getTile((int) ((player.getX() + player.offset[0] * player.getVelocity() + widthTile) / widthTile), (int) ((player.getY() + player.offset[1] * player.getVelocity()) / heightTile)) != 0 || tileMaps.getTile((int) ((player.getX() + player.offset[0] * player.getVelocity() + widthTile) / widthTile), (int) ((player.getY() + player.offset[1] * player.getVelocity() + heightTile) / heightTile)) != 0)
                player.offset[0] = 0;

        }

        if (player.air) {
            double futurePosition = player.getGravity().formulaOfTrajectory();

            if (player.getGravity().timer < (player.getGravity().flightTime/2)){
                if (tileMaps.getTile((int) (player.getX()/widthTile),(int)(futurePosition/heightTile)) ==0 && tileMaps.getTile((int) ((player.getX()+widthTile)/widthTile),(int)(futurePosition/heightTile)) ==0) {
                    player.setY(futurePosition);
                    player.offset[1] = 1;
                }
            }else
                if (Math.abs((player.getY()) - (((int)player.getY()/heightTile+1)*heightTile)) > 3)
                    player.fall();
        }
        if (player.offset[1] == 0)
            player.air = false;
       if (tileMaps.getTile(((int) player.getX() / widthTile), ((int) (player.getY()+heightTile) / heightTile) + 1) == 0 && tileMaps.getTile((int) player.getX() / widthTile + 1, (int) (player.getY()+heightTile) / heightTile + 1) == 0 ) {
                player.fall();
        }else {
            if (Math.abs((player.getY()) - (((int)player.getY()/heightTile+1)*heightTile)) > 3)
                player.fall();
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
    public TileMaps getTileMaps() { return tileMaps; }
    public Player getPlayer() { return player; }
}
