package fr.sae.terraria.modele;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.ReproductiveObjectType;
import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.vue.View;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


public class Environment
{
    private Clock clock;
    private final ObservableList<Entity> entities;

    private final TileMaps tileMaps;
    private final Player player;

    public double scaleMultiplicatorWidth;
    public double scaleMultiplicatorHeight;

    public int widthTile;
    public int heightTile;
    private int ticks = 0;
    private Timeline loop;


    public Environment(double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.scaleMultiplicatorWidth = scaleMultiplicatorWidth;
        this.scaleMultiplicatorHeight = scaleMultiplicatorHeight;

        this.tileMaps = new TileMaps();
        this.tileMaps.load(Terraria.SRC_PATH + "maps/map_0.json");

        this.clock = new Clock();
        this.entities = FXCollections.observableArrayList();
        this.widthTile = (int) (scaleMultiplicatorWidth * TileMaps.TILE_DEFAULT_SIZE);
        this.heightTile = (int) (scaleMultiplicatorHeight * TileMaps.TILE_DEFAULT_SIZE);

        this.player = new Player(this, (5*widthTile), (3*heightTile));
        this.player.setVelocity(5);
        this.player.setPv(4);
        Image image = View.loadAnImage("sprites/player/player_idle.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.player.setRect((int) image.getWidth(), (int) image.getHeight());
        image.cancel();

        gameLoop();
    }

    /** La boucle principale du jeu  */
    private void gameLoop()
    {
        this.loop = new Timeline();
        this.loop.setCycleCount(Animation.INDEFINITE);

        List<Entity> entitiesAtAdded = new ArrayList<>();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(Terraria.TARGET_FPS), (ev -> {
            this.player.offset[0] = 0;
            this.player.eventInput();
            // Ajoute les entités ReproductiveObjectType
            for (Entity entity : entitiesAtAdded)
                this.entities.add(0, entity);
            entitiesAtAdded.clear();

            // Génère aléatoirement des entités
            GenerateEntity.tree(this);
            GenerateEntity.tallGrass(this);
            GenerateEntity.rabbit(this);

            for (Entity entity : entities) {
                if (entity instanceof CollideObjectType)
                    ((CollideObjectType) entity).collide();

                if (entity instanceof ReproductiveObjectType)
                    entitiesAtAdded.addAll(((ReproductiveObjectType) entity).reproduction(this));
                entity.updates();
            }

            this.player.collide();
            this.player.updates();

            this.clock.updates(ticks);
            this.ticks++;
        }));

        this.loop.getKeyFrames().add(keyFrame);
        this.loop.play();
    }


    public ObservableList<Entity> getEntities() { return this.entities; }
    public TileMaps getTileMaps() { return this.tileMaps; }
    public Player getPlayer() { return this.player; }
    public Clock getGameClock() { return this.clock; }
    public Timeline getLoop() { return this.loop; }
    public int getTicks() { return this.ticks; }
}