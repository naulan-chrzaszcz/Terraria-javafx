package fr.sae.terraria.modele;

import fr.sae.terraria.modele.entities.*;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.Rect;
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


public class Environment
{
    private Player player;
    private ArrayList<Entity> entities;
    private Map<KeyCode, Boolean> keysInput = new HashMap<>();
    private int[] elementsSize;

    private int ticks = 0;




    public Environment(int widthGame, int heightGame, int widthPlayer,int heightPlayer)
    {
        elementsSize = new int[]{widthGame,heightGame,widthPlayer,heightPlayer};
        entities = new ArrayList<>();

        player = new Player(0,0, 3, 1);
        player.setPv(20);
        player.setVelocity(2);

        gameLoop();
    }

    private void gameLoop()
    {
        Timeline loop = new Timeline();
        loop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.017), (ev -> {
            eventInput();

            player.setFall(false);

            int countBlocks = 0;
            for (Entity e : entities)
            {
                Rectangle2D rectEntity = player.getRect().collideRect(e.getRect());
                if (rectEntity != null) {
                    System.out.println(player.getRect().collideRect(e.getRect()));
                    if (player.getRect().get().getMinY() - e.getRect().get().getMinY() < 0)
                        player.setY(e.getRect().get().getMinY());
                }

                if (rectEntity == null)
                    countBlocks++;
            }

            if (countBlocks == entities.size())
                player.setFall(true);

            if (player.getFall())
                player.setY(player.getY() + 2);

            getPlayer().updates();
            if (player.getX() < 0){
                player.setX(0.0);
            }
            if (player.getX() > elementsSize[0]-(elementsSize[2]*1.8)){
                player.setX(elementsSize[0]-(elementsSize[2]*1.8));
            }
            ticks++;
        }));

        loop.getKeyFrames().add(keyFrame);
        loop.play();
    }

    public void eventInput()
    {
        int countKeys[] = new int[1];
        keysInput.forEach((key, value) -> {
            if (key == D && value)
                getPlayer().moveRight();
            if (key == Q && value)
                getPlayer().moveLeft();

            if (!value)
                countKeys[0]++;
            if (countKeys[0] == keysInput.size())
                getPlayer().idle();
        });
    }


    public Map<KeyCode, Boolean> getKeysInput() { return keysInput; }
    public ArrayList<Entity> getEntities() { return entities; }
    public Player getPlayer() { return player; }
}
