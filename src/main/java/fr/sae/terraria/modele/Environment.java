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

    private final int[] elementsSize;
    private final int widthTile;
    private final int heightTile;
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
        System.out.println(widthTile + "" + heightTile);
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
        System.out.println(player.air);
        if (player.air) {
            double futurePosition = player.gravity.formulaOfTrajectory();


            if (player.gravity.timer < (player.gravity.flightTime/2)){
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
        if (player.getX() < 0)
            player.setX(.0);
        if (player.getX() > elementsSize[0]-(elementsSize[2]*1.92))
            player.setX(elementsSize[0]-(elementsSize[2]*1.92));
    }


    public List<Entity> getEntities() { return entities; }
    public Player getPlayer() { return player; }
}
