package fr.sae.terraria.modele;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.blocks.Tree;
import fr.sae.terraria.modele.entities.entity.Rect;
import fr.sae.terraria.vue.View;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.lang.reflect.Array;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.sae.terraria.modele.entities.*;
import fr.sae.terraria.modele.entities.entity.Entity;


public class Environment
{
    private final ObservableList<Entity> entities;

    private final Random random;
    private final TileMaps tileMaps;
    private final Player player;

    private int widthTile;
    private int heightTile;
    private int heightPlayer;
    private int widthPlayer;
    private double scaleMultiplicatorWidth;
    private double scaleMultiplicatorHeight;

    private int ticks = 0;



    public Environment(TileMaps tileMaps, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.tileMaps = tileMaps;
        this.scaleMultiplicatorWidth = scaleMultiplicatorWidth;
        this.scaleMultiplicatorHeight = scaleMultiplicatorHeight;

        this.widthTile = (int) (scaleMultiplicatorWidth * TileMaps.TILE_DEFAULT_SIZE);
        this.heightTile = (int) (scaleMultiplicatorHeight * TileMaps.TILE_DEFAULT_SIZE);

        Image image = View.loadAnImage("sprites/player/player_idle.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        widthPlayer = (int)image.getWidth();
        heightPlayer = (int)image.getHeight();
        image.cancel();

        this.entities = FXCollections.observableArrayList();
        try {
            this.random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) { throw new RuntimeException(e); }

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

            this.generateTree();

            this.getPlayer().updates();
            this.ticks++;

        }));

        loop.getKeyFrames().add(keyFrame);
        loop.play();
    }

    private void collide() {
        if (player.offset[0] != 0 || player.offset[1] != 0) {
            boolean pTopLeft = tileMaps.getTile((int) (player.getX()+2+player.offset[0]*player.getVelocity())/widthTile,(int) player.getY()/heightTile) != 0;
            boolean pTopRight = tileMaps.getTile((int)(player.getX()-2+widthPlayer+ getPlayer().getVelocity()*player.offset[0])/widthTile, (int)player.getY()/heightTile) != 0;
            boolean pBotLeft = tileMaps.getTile((int) (player.getX()+2+ getPlayer().getVelocity()*player.offset[0])/widthTile,(int) (player.getY()+heightPlayer)/heightTile) != 0;
            boolean pBotRight = tileMaps.getTile((int) (player.getX()-2+widthPlayer+ player.getVelocity()*player.offset[0])/widthTile , (int) (player.getY()+heightPlayer)/heightTile) != 0;
            if ( pTopLeft|| pTopRight  || pBotLeft  || pBotRight )
                player.offset[0] = 0;

        }

        if (player.air) {
            double futurePosition = player.getGravity().formulaOfTrajectory();
            if (player.getGravity().timer < player.getGravity().flightTime){
                // TODO :  remettre bein la condition idem pour en bas
                if (tileMaps.getTile((int) (player.getX()/widthTile),(int)(futurePosition/heightTile)) ==0 && tileMaps.getTile((int) ((player.getX()+widthTile)/widthTile),(int)(futurePosition/heightTile)) ==0) {
                    player.setY(futurePosition);
                    player.offset[1] = 1;
                }
            }
            else {
                boolean pBotLeft = tileMaps.getTile((int) (player.getX()/widthTile),(int) ((futurePosition+heightPlayer+3)/heightTile)) == 0;
                boolean pBotRight = tileMaps.getTile((int) ((player.getX()+widthPlayer)/widthTile),(int) ((futurePosition+heightPlayer+3)/heightTile)) == 0;
                if (pBotLeft && pBotRight ){
                    player.setY(futurePosition);
                    player.offset[1] = -1;
                }
            }
        }

        if (player.offset[1] == 0)
            player.air = false;
       if (tileMaps.getTile((int)((player.getX()+2)/widthTile),(int)(player.getY()+heightPlayer+2)/heightTile) == 0 && tileMaps.getTile((int)((player.getX()+widthPlayer-2)/widthTile),(int)(player.getY()+heightPlayer+2)/heightTile) == 0) {
                player.fall();
        }
    }

    private void generateTree()
    {
        double spawnRate = .2;
        int whenSpawn = 5000;

        if (ticks%whenSpawn == 0)
            for (int y = 0; y < tileMaps.getHeight(); y++) {
                boolean spawnOrNot = Math.random() < spawnRate;

                if (spawnOrNot) {
                    ArrayList<Integer> posFloor = new ArrayList<>();
                    for (int x = 0; x < tileMaps.getWidth(); x++)
                        if (tileMaps.getTile(x, y) == TileMaps.FLOOR_TOP)
                            posFloor.add(x);

                    if (posFloor.size() > 0) {
                        int whereSpawn = random.nextInt(posFloor.size());
                        int xTree = posFloor.get((whereSpawn == 0) ? whereSpawn : whereSpawn-1);
                        if (tileMaps.getTile(xTree, y-1) == 0) {
                            Tree tree = new Tree(xTree * widthTile, y * heightTile);
                            entities.add(0, tree);
                        }
                        return;
                    }
                }
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
}
