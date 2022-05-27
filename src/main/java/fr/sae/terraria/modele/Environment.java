package fr.sae.terraria.modele;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.entities.CollideObjectType;
import fr.sae.terraria.modele.entities.Player;
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

        this.player = new Player(this, 0, 0);
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
            this.worldLimit();

            GenerateEntity.tree(this);
            GenerateEntity.tallGrass(this);

            for (Entity entity : entities) {
                if (entity instanceof CollideObjectType)
                    ((CollideObjectType) entity).collide();
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
    private void worldLimit()
    {
        if (player.offset[0] == -1 && player.getX() < 0)
            player.offset[0] = 0;
        if (player.offset[0] == 1 && player.getX() > (scaleMultiplicatorWidth * Terraria.DISPLAY_RENDERING_WIDTH) - player.getRect().getWidth())
            player.offset[0] = 0;
    }

    private void updateGameTimer(){
        /** si environ 1 minute passe irl, le timer dans le jeu augmente de 10 minutes */
        if (ticks % 37 == 0){
            gameTime.update();
        }
    }


    public ObservableList<Entity> getEntities() { return this.entities; }
    public TileMaps getTileMaps() { return this.tileMaps; }
    public Player getPlayer() { return this.player; }
    public Timer getGameTime() {return this.gameTime;}
    public int getTicks() { return this.ticks; }
}
