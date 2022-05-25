package fr.sae.terraria.modele;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.sae.terraria.modele.entities.*;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.blocks.Tree;
import fr.sae.terraria.modele.blocks.TallGrass;
import fr.sae.terraria.Terraria;
import fr.sae.terraria.vue.View;


public class Environment
{
    private static final int COLLISION_TOLERANCE = 3;

    private final ObservableList<Entity> entities;

    private final TileMaps tileMaps;
    private final Player player;

    private double scaleMultiplicatorWidth;
    private double scaleMultiplicatorHeight;

    public int widthTile;
    public int heightTile;
    private int widthPlayer;
    private int heightPlayer;
    private int ticks = 0;



    public Environment(TileMaps tileMaps, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.tileMaps = tileMaps;
        this.scaleMultiplicatorWidth = scaleMultiplicatorWidth;
        this.scaleMultiplicatorHeight = scaleMultiplicatorHeight;

        this.widthTile = (int) (scaleMultiplicatorWidth * TileMaps.TILE_DEFAULT_SIZE);
        this.heightTile = (int) (scaleMultiplicatorHeight * TileMaps.TILE_DEFAULT_SIZE);

        Image image = View.loadAnImage("sprites/player/player_idle.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        widthPlayer = (int) image.getWidth();
        heightPlayer = (int) image.getHeight();
        image.cancel();

        this.entities = FXCollections.observableArrayList();

        this.player = new Player(0, 0);
        this.player.setVelocity(5);
        this.player.setPv(4);
        this.player.setX((5*widthTile));
        this.player.setY((3*heightTile));
        this.player.setRect(widthPlayer, heightPlayer);

        gameLoop();
    }

    private void gameLoop()
    {
        Timeline loop = new Timeline();
        loop.setCycleCount(Animation.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(Terraria.TARGET_FPS), (ev -> {
            this.player.idle();
            this.player.eventInput();
            this.collide();
            this.worldLimit();

            GenerateEntity.tree(this);
            GenerateEntity.tallGrass(this);

            this.getPlayer().updates();
            for (Entity entity : entities)
                entity.updates();
            this.ticks++;
        }));

        loop.getKeyFrames().add(keyFrame);
        loop.play();
    }

    // TODO: le déplacé dans Entity.
    private void collide()
    {
        if (player.offset[0] != 0 || player.offset[1] != 0) {
            boolean pTopLeft  = tileMaps.getTile((int) (player.getX()+COLLISION_TOLERANCE+player.offset[0]*player.getVelocity())/widthTile,(int) (player.getY()+COLLISION_TOLERANCE)/heightTile) != 0;
            boolean pTopRight = tileMaps.getTile((int) (player.getX()-COLLISION_TOLERANCE+widthPlayer+ getPlayer().getVelocity()*player.offset[0])/widthTile, (int) (player.getY()+COLLISION_TOLERANCE)/heightTile) != 0;
            boolean pBotLeft  = tileMaps.getTile((int) (player.getX()+COLLISION_TOLERANCE+getPlayer().getVelocity()*player.offset[0])/widthTile,(int) (player.getY()+heightPlayer-COLLISION_TOLERANCE)/heightTile) != 0;
            boolean pBotRight = tileMaps.getTile((int) (player.getX()-COLLISION_TOLERANCE+widthPlayer+ player.getVelocity()*player.offset[0])/widthTile , (int) (player.getY()+heightPlayer-COLLISION_TOLERANCE)/heightTile) != 0;

            if (pTopLeft|| pTopRight  || pBotLeft  || pBotRight)
                player.offset[0] = 0;
        }

        if (player.air) {
            double futurePosition = player.getGravity().formulaOfTrajectory();

            if (player.getGravity().timer < player.getGravity().flightTime){
                boolean pTopLeft  = tileMaps.getTile((int) ((player.getX()+COLLISION_TOLERANCE)/widthTile),(int)(futurePosition/heightTile)) ==0;
                boolean pTopRight = tileMaps.getTile((int) (((player.getX()-COLLISION_TOLERANCE)+widthTile)/widthTile),(int)(futurePosition/heightTile)) ==0;

                if ( pTopLeft && pTopRight ) {
                    player.setY(futurePosition);
                    player.offset[1] = 1;
                } else { player.getGravity().setFall(player.getY()); player.offset[1] = 1; }
            } else {
                boolean pBotLeft = tileMaps.getTile((int) ((player.getX()+COLLISION_TOLERANCE)/widthTile),(int) ((futurePosition+heightPlayer+COLLISION_TOLERANCE)/heightTile)) == 0;
                boolean pBotRight = tileMaps.getTile((int) ((player.getX()-COLLISION_TOLERANCE+widthPlayer)/widthTile),(int) ((futurePosition+heightPlayer+COLLISION_TOLERANCE)/heightTile)) == 0;

                if (pBotLeft && pBotRight ){
                    player.setY(futurePosition);
                    player.offset[1] = 1;
                }
            }
        }

        if (tileMaps.getTile((int) ((player.getX() + COLLISION_TOLERANCE) / widthTile), (int) (player.getY() + heightPlayer + COLLISION_TOLERANCE) / heightTile) == 0 && tileMaps.getTile((int) ((player.getX() + widthPlayer - COLLISION_TOLERANCE) / widthTile), (int) (player.getY() + heightPlayer + COLLISION_TOLERANCE) / heightTile) == 0 && !player.air)
            player.fall();
        if (player.offset[1] == 0)
            player.air = false;

        if (player.offset[1] == -1) {
            player.getGravity().degInit = 0;
            double futurY = player.getGravity().formulaOfTrajectory();
            boolean pBotLeft = tileMaps.getTile((int) ((player.getX() + COLLISION_TOLERANCE) / widthTile), (int) ((futurY + heightPlayer + COLLISION_TOLERANCE) / heightTile)) == 0;
            boolean pBotRight = tileMaps.getTile((int) ((player.getX() - COLLISION_TOLERANCE + widthPlayer) / widthTile), (int) ((futurY + heightPlayer + COLLISION_TOLERANCE) / heightTile)) == 0;

            if (pBotLeft && pBotRight) {
                player.setY(futurY);
                player.offset[1] = -1;
            }  else player.offset[1] = 0;
            // TODO : PROX OFFSET 0 RESET YINIT
        }
    }

    /** Evite que le joueur sort de la carte. */
    private void worldLimit()
    {
        if (player.offset[0] == -1 && player.getX() < 0)
            player.offset[0] = 0;
        if (player.offset[0] == 1 && player.getX() > (scaleMultiplicatorWidth * Terraria.DISPLAY_RENDERING_WIDTH) - widthPlayer)
            player.offset[0] = 0;
    }


    public ObservableList<Entity> getEntities() { return this.entities; }
    public TileMaps getTileMaps() { return this.tileMaps; }
    public Player getPlayer() { return this.player; }
    public int getTicks() { return this.ticks; }
}
