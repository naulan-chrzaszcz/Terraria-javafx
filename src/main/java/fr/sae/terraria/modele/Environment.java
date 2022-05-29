package fr.sae.terraria.modele;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.entities.Player;
import fr.sae.terraria.modele.entities.Rabbit;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.vue.View;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.util.Duration;


public class Environment
{
    private Timer gameTime;
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
        this.gameTime = new Timer();
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

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(Terraria.TARGET_FPS), (ev -> {
            this.player.offset[0] = 0;
            this.player.eventInput();
            this.worldLimit(this.player);

            GenerateEntity.tree(this);
            GenerateEntity.tallGrass(this);
            GenerateEntity.rabbit(this);

            for (Entity entity : entities) {
                if (ticks%Rabbit.JUMP_FREQUENCY == 0)
                    if (entity instanceof Rabbit && Math.random() < Rabbit.LUCK_OF_JUMPING)
                        if (entity.offset[1] != -1) entity.jump();
                if (entity instanceof CollideObjectType) {
                    this.worldLimit(entity);
                    ((CollideObjectType) entity).collide();
                }
                entity.updates();
            }

            this.player.collide();
            this.player.updates();

            this.updateGameTimer();
            this.ticks++;
        }));

        loop.getKeyFrames().add(keyFrame);
        loop.play();
    }

    /** Evite que le joueur sort de la carte. */
    private void worldLimit(Entity entity)
    {
        if (entity.offset[0] == -1 && entity.getX() < 0)
            entity.offset[0] = (entity instanceof Rabbit) ? ((-1) * entity.offset[0]) : 0;
        if (entity.offset[0] == 1 && entity.getX() > (scaleMultiplicatorWidth * Terraria.DISPLAY_RENDERING_WIDTH) - entity.getRect().getWidth())
            entity.offset[0] = (entity instanceof Rabbit) ? ((-1) * entity.offset[0]) : 0;
    }

    /** si environ 1 minute passe irl, le timer dans le jeu augmente de 10 minutes */
    private void updateGameTimer()
    {
        if (ticks % 37 == 0)
            gameTime.updates();
    }


    public ObservableList<Entity> getEntities() { return this.entities; }
    public TileMaps getTileMaps() { return this.tileMaps; }
    public Player getPlayer() { return this.player; }
    public Timer getGameTime() {return this.gameTime;}
    public int getTicks() { return this.ticks; }
}
