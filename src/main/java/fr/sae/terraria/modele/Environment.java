package fr.sae.terraria.modele;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.geometry.Rectangle2D;

import javafx.scene.input.KeyCode;

import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.Q;

import fr.sae.terraria.modele.entities.*;
import fr.sae.terraria.modele.entities.entity.Entity;


public class Environment
{
    private final Map<KeyCode, Boolean> keysInput;
    private final ArrayList<Entity> entities;

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
        keysInput = new HashMap<>();
        entities = new ArrayList<>();

        player = new Player(0,0);
        player.setVelocity(5);
        player.setPv(20);

        gameLoop();
    }

    private void gameLoop()
    {
        Timeline loop = new Timeline();
        loop.setCycleCount(Animation.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.017), (ev -> {
            eventInput();

            int tileUnderFootstepX = (((int) (player.getX()/widthTile)+TileMaps.TILE_DEFAULT_SIZE/2)%TileMaps.TILE_DEFAULT_SIZE)+1;
            int tileUnderFootstepY = (((int) (player.getY()/heightTile)+TileMaps.TILE_DEFAULT_SIZE/2)%TileMaps.TILE_DEFAULT_SIZE)+1;
            System.out.println("x: " + tileUnderFootstepX + " | y: " + tileUnderFootstepY);
            System.out.println(tileMaps.getTile(tileUnderFootstepX, tileUnderFootstepY));

            player.setX(100);
            player.setY(500);

            this.worldLimit();
            ticks++;

            getPlayer().updates();
        }));

        loop.getKeyFrames().add(keyFrame);
        loop.play();
    }

    /** Lie les inputs au clavier à une ou des actions. */
    private void eventInput()
    {
        int[] countKeys = new int[1];   // Tableau de 1 de longueur pour qu'on y ait access dans le forEach
        keysInput.forEach((key, value) -> {
            if (key == D && Boolean.TRUE.equals(value)) getPlayer().moveRight();
            if (key == Q && Boolean.TRUE.equals(value)) getPlayer().moveLeft();

            if (Boolean.FALSE.equals(value))            countKeys[0]++;
            if (countKeys[0] == keysInput.size())       getPlayer().idle();
        });
    }

    /** Evite que le joueur sort de la carte. */
    private void worldLimit()
    {
        if (player.getX() < 0)
            player.setX(0.0);
        if (player.getX() > elementsSize[0]-(elementsSize[2]*1.92))
            player.setX(elementsSize[0]-(elementsSize[2]*1.92));
    }


    public Map<KeyCode, Boolean> getKeysInput() { return keysInput; }
    public ArrayList<Entity> getEntities() { return entities; }
    public Player getPlayer() { return player; }
}
