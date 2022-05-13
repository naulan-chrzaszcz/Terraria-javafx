package fr.sae.terraria.modele;

import fr.sae.terraria.modele.entities.*;
import fr.sae.terraria.modele.entities.entity.Entity;
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

    private int ticks = 0;


    public Environment()
    {
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

            getPlayer().setY(getPlayer().getY() + 1);
            for (Entity e : entities)
            {
                if (player.getRect().collideRect(e.getRect()) != null) {
                    System.out.println(player.getRect().collideRect(e.getRect()));
                }
            }
            getPlayer().updates();

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
