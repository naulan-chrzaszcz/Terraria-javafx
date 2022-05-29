package fr.sae.terraria.modele;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.blocks.TallGrass;
import fr.sae.terraria.modele.entities.Player;
import fr.sae.terraria.modele.entities.Rabbit;
import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.ReproductiveObjectType;
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
    private Clock gameTime;
    private final ObservableList<Entity> entities;

    private final TileMaps tileMaps;
    private final Player player;

    public double scaleMultiplicatorWidth;
    public double scaleMultiplicatorHeight;


    public int widthTile;
    public int heightTile;
    private int ticks = 0;


    public Environment(double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.gameTime = new Clock();
        this.tileMaps = new TileMaps();
        this.tileMaps.load(Terraria.srcPath + "maps/map_0.json");
        this.scaleMultiplicatorWidth = scaleMultiplicatorWidth;
        this.scaleMultiplicatorHeight = scaleMultiplicatorHeight;

        this.entities = FXCollections.observableArrayList();

        this.widthTile = (int) (scaleMultiplicatorWidth * TileMaps.TILE_DEFAULT_SIZE);
        this.heightTile = (int) (scaleMultiplicatorHeight * TileMaps.TILE_DEFAULT_SIZE);
        Image image = View.loadAnImage("sprites/player/player_idle.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        int widthPlayer = (int) image.getWidth();
        int heightPlayer = (int) image.getHeight();
        image.cancel();

        this.player = new Player(this, (5*widthTile), (3*heightTile));
        this.player.setVelocity(5);
        this.player.setPv(4);
        this.player.setRect(widthPlayer, heightPlayer);

        gameLoop();
    }

    private void gameLoop()
    {
        Timeline loop = new Timeline();
        loop.setCycleCount(Animation.INDEFINITE);

        List<Entity> entitiesAtAdded = new ArrayList<>();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(Terraria.TARGET_FPS), (ev -> {
            this.player.offset[0] = Entity.IDLE;
            this.player.eventInput();
            this.worldLimit(this.player);

            // Ajoute les entités qui ont été ajouté par des ReproductiveObjectType
            for (Entity entity : entitiesAtAdded) this.entities.add(0, entity);
            entitiesAtAdded.clear();

            GenerateEntity.tree(this);
            GenerateEntity.tallGrass(this);
            GenerateEntity.rabbit(this);

            for (Entity entity : entities) {
                if (entity instanceof Rabbit) {
                    boolean mustJump = ticks%Rabbit.JUMP_FREQUENCY == 0;
                    if (mustJump) {
                        boolean jumpOrNot = Math.random() < Rabbit.LUCK_OF_JUMPING;
                        if (jumpOrNot && entity.offset[1] != Entity.IS_FALLING)
                            entity.jump();
                    }
                }

                if (entity instanceof CollideObjectType) {
                    this.worldLimit(entity);
                    ((CollideObjectType) entity).collide();
                }

                if (entity instanceof ReproductiveObjectType) {
                    if (entity instanceof TallGrass && ticks%TallGrass.REPRODUCTION_RATE == 0) {
                        ((ReproductiveObjectType) entity).reproduction(this);
                        entitiesAtAdded.addAll(((ReproductiveObjectType) entity).getChildren());
                    }
                }
                entity.updates();
            }

            this.player.collide();
            this.player.updates();

            this.gameTime.updates(ticks);
            this.ticks++;
        }));

        loop.getKeyFrames().add(keyFrame);
        loop.play();
    }

    /** Evite que l'entité sort de la fenêtre. */
    private void worldLimit(Entity entity)
    {
        double widthScreen = (scaleMultiplicatorWidth * Terraria.DISPLAY_RENDERING_WIDTH);

        boolean exceedsScreenOnLeft = entity.offset[0] == Entity.IS_MOVING_LEFT && entity.getX() < 0;
        boolean exceedsScreenOnRight = entity.offset[0] == Entity.IS_MOVING_RIGHT && entity.getX() > (widthScreen - entity.getRect().getWidth());
        if (exceedsScreenOnLeft || exceedsScreenOnRight)
            entity.offset[0] = (entity instanceof Rabbit) ? ((-1) * entity.offset[0]) : Entity.IDLE;
    }

    public ObservableList<Entity> getEntities() { return this.entities; }
    public TileMaps getTileMaps() { return this.tileMaps; }
    public Player getPlayer() { return this.player; }
    public Clock getGameTime() {return this.gameTime;}
    public int getTicks() { return this.ticks; }
}
