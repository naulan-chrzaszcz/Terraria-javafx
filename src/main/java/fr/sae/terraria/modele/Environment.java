package fr.sae.terraria.modele;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.entities.Rabbit;
import fr.sae.terraria.modele.entities.Slime;
import fr.sae.terraria.modele.entities.blocks.Torch;
import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.ReproductiveObjectType;
import fr.sae.terraria.modele.entities.items.Meat;
import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.vue.View;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.util.Duration;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Environment
{
    // Permet d'update toutes les entités en une seule boucle.
    private final ObservableList<Entity> entities;
    // Range des entities en plus pour permettre facilement de savoir combien son t-il sur la carte pour limiter leur apparition
    private final ObservableList<Rabbit> rabbits;
    private final ObservableList<Slime> slimes;
    // Permet update facilement les lumieres des torches sur le filtre
    private final ObservableList<Torch> torches;

    private final TileMaps tileMaps;
    private final Player player;
    private final Clock clock;
    private Timeline loop;

    public double scaleMultiplicatorWidth;
    public double scaleMultiplicatorHeight;

    private int previousDays;
    public int widthTile;
    public int heightTile;
    private int ticks;


    public Environment(double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.scaleMultiplicatorWidth = scaleMultiplicatorWidth;
        this.scaleMultiplicatorHeight = scaleMultiplicatorHeight;
        this.widthTile = (int) (scaleMultiplicatorWidth * TileMaps.TILE_DEFAULT_SIZE);
        this.heightTile = (int) (scaleMultiplicatorHeight * TileMaps.TILE_DEFAULT_SIZE);

        this.tileMaps = new TileMaps();
        this.tileMaps.load(Terraria.SRC_PATH + "maps/map_0.json");

        this.clock = new Clock();
        this.previousDays = -1;
        this.ticks = 0;

        this.entities = FXCollections.observableArrayList();
        this.rabbits = FXCollections.observableArrayList();
        this.slimes = FXCollections.observableArrayList();
        this.torches = FXCollections.observableArrayList();

        this.player = new Player(this, (5*widthTile), (3*heightTile));
        this.player.setVelocity(5);
        this.player.setPv(4);
        Image image = View.loadAnImage("sprites/player/player_idle.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.player.setRect((int) image.getWidth(), (int) image.getHeight());
        image.cancel();

        Environment.playSound("sound/Bird_Sound.wav", true);

        gameLoop();
    }

    /** La boucle principale du jeu  */
    private void gameLoop()
    {
        // TODO TEST
        boolean[] caught = new boolean[] {false};

        this.loop = new Timeline();
        this.loop.setCycleCount(Animation.INDEFINITE);

        List<Entity> entitiesAtAdded = new ArrayList<>();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(Terraria.TARGET_FPS), (ev -> {
            this.player.offset[0] = Entity.IDLE;
            this.player.eventInput();

            // TODO TEST
            if (!caught[0]) {
                Torch torch = new Torch(this, 0, 0);
                player.pickup(torch);
                Meat meat = new Meat();
                player.pickup(meat);
                caught[0] = true;
            }

            // Ajoute les entités ReproductiveObjectType
            for (Entity entity : entitiesAtAdded)
                this.entities.add(0, entity);
            entitiesAtAdded.clear();

            // Génère aléatoirement des entités
            if (previousDays != clock.getDays())
                for (int i = 0; i < 3; i++) // Génère par jour, 3 arbres
                    GenerateEntity.tree(this);
            if (clock.getMinutes() < (Clock.MINUTES_IN_A_DAY/2)) {  // Génère des lapins uniquement pendant le jour
                GenerateEntity.rabbit(this);
                GenerateEntity.tallGrass(this);
            }
            if (clock.getMinutes() > (Clock.MINUTES_IN_A_DAY)/2)    // Génère des slimes uniquement pendant le soir
                GenerateEntity.slime(this);

            // Updates toutes les entités
            for (Entity entity : entities) {
                if (entity instanceof CollideObjectType)
                    ((CollideObjectType) entity).collide();

                if (entity instanceof ReproductiveObjectType)
                    entitiesAtAdded.addAll(((ReproductiveObjectType) entity).reproduction(this));
                entity.updates();
            }

            this.player.collide();
            this.player.updates();

            this.previousDays = this.clock.getDays();
            this.clock.updates(ticks);
            this.ticks++;
        }));

        this.loop.getKeyFrames().add(keyFrame);
        this.loop.play();
    }

    public static Clip playSound(String path, boolean loop)
    {
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(Terraria.SRC_PATH + path).toURI().toURL());
            clip.open(inputStream);
        } catch (Exception e) { e.printStackTrace(); }

        if (!Objects.isNull(clip)) {
            clip.loop((loop) ? Clip.LOOP_CONTINUOUSLY : 0);
            clip.start();
        }

        return clip;
    }


    public ObservableList<Entity> getEntities() { return this.entities; }
    public ObservableList<Rabbit> getRabbits() { return rabbits; }
    public ObservableList<Torch> getTorches() { return torches; }
    public ObservableList<Slime> getSlimes() { return slimes; }
    public TileMaps getTileMaps() { return this.tileMaps; }
    public Player getPlayer() { return this.player; }
    public Clock getGameClock() { return this.clock; }
    public Timeline getLoop() { return this.loop; }
    public int getTicks() { return this.ticks; }
}