package fr.sae.terraria.modele;

import fr.sae.terraria.modele.entities.entity.Rect;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.geometry.Rectangle2D;

import javafx.scene.input.KeyCode;

import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.sae.terraria.modele.entities.*;
import fr.sae.terraria.modele.entities.entity.Entity;

import static javafx.scene.input.KeyCode.*;


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

    boolean yInitGetted = false;
    double yInit = 0;
    int timeJump = 0;


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
        player.setX(widthTile);
        player.setY(8*heightTile);

        gameLoop();
    }

    private void gameLoop()
    {
        Timeline loop = new Timeline();
        loop.setCycleCount(Animation.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.017), (ev -> {
            this.collide();
            this.eventInput();

            if (this.getPlayer().getFall())
                this.getPlayer().setY(getPlayer().getY() + 2);
            this.worldLimit();

            this.ticks++;
            this.getPlayer().updates();
        }));

        loop.getKeyFrames().add(keyFrame);
        loop.play();
    }

    /** Lie les inputs au clavier à une ou des actions. */
    private void eventInput()
    {
        int[] countKeys = new int[1];   // Tableau de 1 de longueur pour qu'on y ait access dans le forEach
        keysInput.forEach((key, value) -> {
            if (!yInitGetted) {
                this.timeJump = 0;
                yInit = getPlayer().getY();
                yInitGetted = true;
            }

            if (key == D && Boolean.TRUE.equals(value)) getPlayer().moveRight();
            if (key == Q && Boolean.TRUE.equals(value)) getPlayer().moveLeft();
            if ((key == Z || key == SPACE) && Boolean.TRUE.equals(value)) {
                getPlayer().jump(timeJump, yInit);
                timeJump += .001;
            } else { yInitGetted = false; }

            if (Boolean.FALSE.equals(value))            countKeys[0]++;
            if (countKeys[0] == keysInput.size())       getPlayer().idle();
        });
    }

    private void collide()
    {
        int tileX = (int) (this.player.getX()/widthTile);
        int tileY = (int) (this.player.getY()/heightTile);
        int tileUnderFootstep = this.tileMaps.getTile(tileX, tileY+1);

        for (Entity e : entities)
            if (this.player.getRect().collideRect(e.getRect()) != null)
            {
                int tileLeft = this.tileMaps.getTile(tileX, tileY);
                int tileRight = this.tileMaps.getTile(tileX+1, tileY);

                // Revient en arriere
                if (tileLeft != 0 || tileRight != 0)
                    getPlayer().rollback();
            }

        // Tombe vers le bas
        getPlayer().setFall(tileUnderFootstep == 0);
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
