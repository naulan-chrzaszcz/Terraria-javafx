package fr.sae.terraria.modele;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.sae.terraria.modele.entities.*;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.blocks.Tree;
import fr.sae.terraria.modele.blocks.TallGrass;
import fr.sae.terraria.Terraria;
import fr.sae.terraria.vue.View;


public class Environment
{
    private static final double TREE_SPAWN_RATE = .2;
    private static final int WHEN_SPAWN_A_TREE = 5_000;
    private static final int COLLISION_TOLERANCE = 3;

    private final ObservableList<Entity> entities;

    private final Random random;
    private final TileMaps tileMaps;
    private final Player player;

    private double scaleMultiplicatorWidth;
    private double scaleMultiplicatorHeight;

    private int widthTile;
    private int heightTile;
    private int widthPlayer;
    private int heightPlayer;
    private int ticks = 0;



    public Environment(TileMaps tileMaps, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.tileMaps = tileMaps;
        this.scaleMultiplicatorWidth = scaleMultiplicatorWidth;
        this.scaleMultiplicatorHeight = scaleMultiplicatorHeight;

        this.widthTile = (int) (scaleMultiplicatorWidth * TileMaps.TILE_DEFAULT_SIZE);
        this.heightTile = (int) (scaleMultiplicatorHeight * TileMaps.TILE_DEFAULT_SIZE);

        Image image = View.loadAnImage("sprites/player/player_idle.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        widthPlayer = (int) image.getWidth();
        heightPlayer = (int) image.getHeight();
        image.cancel();

        this.entities = FXCollections.observableArrayList();
        try {
            this.random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) { throw new RuntimeException(e); }

        this.player = new Player(0, 0);
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
            this.collide();
            this.worldLimit();

            this.generateTree();
            this.generateTallGrass();

            this.getPlayer().updates();
            for (Entity entity : entities)
                entity.updates();
            this.ticks++;
        }));

        loop.getKeyFrames().add(keyFrame);
        loop.play();
    }

    // TODO: le déplacé dans Entity.
    private void collide()
    {
        if (player.offset[0] != 0 || player.offset[1] != 0) {
            boolean pTopLeft  = tileMaps.getTile((int) (player.getX()+COLLISION_TOLERANCE+player.offset[0]*player.getVelocity())/widthTile,(int) (player.getY()+COLLISION_TOLERANCE)/heightTile) != 0;
            boolean pTopRight = tileMaps.getTile((int) (player.getX()-COLLISION_TOLERANCE+widthPlayer+ getPlayer().getVelocity()*player.offset[0])/widthTile, (int) (player.getY()+COLLISION_TOLERANCE)/heightTile) != 0;
            boolean pBotLeft  = tileMaps.getTile((int) (player.getX()+COLLISION_TOLERANCE+getPlayer().getVelocity()*player.offset[0])/widthTile,(int) (player.getY()+heightPlayer-COLLISION_TOLERANCE)/heightTile) != 0;
            boolean pBotRight = tileMaps.getTile((int) (player.getX()-COLLISION_TOLERANCE+widthPlayer+ player.getVelocity()*player.offset[0])/widthTile , (int) (player.getY()+heightPlayer-COLLISION_TOLERANCE)/heightTile) != 0;

            if (pTopLeft|| pTopRight  || pBotLeft  || pBotRight)
                player.offset[0] = 0;
        }

        if (player.air) {
            double futurePosition = player.getGravity().formulaOfTrajectory();

            if (player.getGravity().timer < player.getGravity().flightTime){
                boolean pTopLeft  = tileMaps.getTile((int) ((player.getX()+COLLISION_TOLERANCE)/widthTile),(int)(futurePosition/heightTile)) ==0;
                boolean pTopRight = tileMaps.getTile((int) (((player.getX()-COLLISION_TOLERANCE)+widthTile)/widthTile),(int)(futurePosition/heightTile)) ==0;

                if ( pTopLeft && pTopRight ) {
                    player.setY(futurePosition);
                    player.offset[1] = 1;
                } else { player.getGravity().setFall(player.getY()); player.offset[1] = 1; }
            } else {
                boolean pBotLeft = tileMaps.getTile((int) ((player.getX()+COLLISION_TOLERANCE)/widthTile),(int) ((futurePosition+heightPlayer+COLLISION_TOLERANCE)/heightTile)) == 0;
                boolean pBotRight = tileMaps.getTile((int) ((player.getX()-COLLISION_TOLERANCE+widthPlayer)/widthTile),(int) ((futurePosition+heightPlayer+COLLISION_TOLERANCE)/heightTile)) == 0;

                if (pBotLeft && pBotRight ){
                    player.setY(futurePosition);
                    player.offset[1] = 1;
                }
            }
        }

        if (tileMaps.getTile((int) ((player.getX() + COLLISION_TOLERANCE) / widthTile), (int) (player.getY() + heightPlayer + COLLISION_TOLERANCE) / heightTile) == 0 && tileMaps.getTile((int) ((player.getX() + widthPlayer - COLLISION_TOLERANCE) / widthTile), (int) (player.getY() + heightPlayer + COLLISION_TOLERANCE) / heightTile) == 0 && !player.air)
            player.fall();
        if (player.offset[1] == 0)
            player.air = false;

        if (player.offset[1] == -1) {
            player.getGravity().degInit = 0;
            double futurY = player.getGravity().formulaOfTrajectory();
            boolean pBotLeft = tileMaps.getTile((int) ((player.getX() + COLLISION_TOLERANCE) / widthTile), (int) ((futurY + heightPlayer + COLLISION_TOLERANCE) / heightTile)) == 0;
            boolean pBotRight = tileMaps.getTile((int) ((player.getX() - COLLISION_TOLERANCE + widthPlayer) / widthTile), (int) ((futurY + heightPlayer + COLLISION_TOLERANCE) / heightTile)) == 0;

            if (pBotLeft && pBotRight) {
                player.setY(futurY);
                player.offset[1] = -1;
            }  else player.offset[1] = 0;
            // TODO : PROX OFFSET 0 RESET YINIT
        }
    }

    /** À un certain moment, grace au tick, il va générer des arbres +/- grand uniquement sur un sol */
    private void generateTree()
    {
        // Fréquence d'apparition
        if (ticks%WHEN_SPAWN_A_TREE == 0)
            for (int y = 0; y < tileMaps.getHeight(); y++)
                // Est-ce que l'arbre doit spawn sur ce 'y'
                if (Math.random() < TREE_SPAWN_RATE) {
                    List<Integer> locFloorsOnAxisX = findFloors(y);
                    // Si il y a du sol sur la ligne
                    if (!locFloorsOnAxisX.isEmpty()) {
                        int onWhichFloor = random.nextInt(locFloorsOnAxisX.size());
                        int targetFloor = locFloorsOnAxisX.get((onWhichFloor == 0) ? onWhichFloor : onWhichFloor - 1);
                        int xTree = targetFloor * widthTile;
                        int yTree = ((y == 0) ? y : (y - 1)) * heightTile;
                        // Verifies au cas où si le tile au-dessus de lui est bien une casse vide (Du ciel)
                        if (tileMaps.getTile(targetFloor, y - 1) == TileMaps.SKY) {
                            for (Entity entity : this.entities)
                                if (entity instanceof Tree && xTree == entity.getX() && yTree == entity.getY())
                                    // Un arbre est déjà present ? Il ne le génère pas et arrête complétement la fonction
                                    return;
                            // Une fois une position trouvée, on l'ajoute en tant qu'entité pour qu'il puisse ensuite l'affiché
                            this.entities.add(0, new Tree(xTree, yTree));
                        }
                        // Une fois l'arbre généré, il arrête complétement toutes la fontion
                        return;
                    }
                    // Sinon on retourne vers la premiere boucle 'for'
                }
    }

    /** Range les positions du sol sur la ligne 'y' pour tirer au sort l'un dans la liste */
    private List<Integer> findFloors(int y)
    {
        List<Integer> localisation = new ArrayList<>();
        for (int x = 0; x < tileMaps.getWidth(); x++) {
            int targetTile = tileMaps.getTile(x, y);

            if (targetTile == TileMaps.FLOOR_TOP || targetTile == TileMaps.FLOOR_RIGHT || targetTile == TileMaps.FLOOR_LEFT)
                localisation.add(x);
        }

        return localisation;
    }

    private void generateTallGrass()
    {
        double spawnRate = .3;
        int whenSpawn = 2_000;

        // Frequence d'apparition
        if (ticks % whenSpawn == 0) for (int y = 0; y < tileMaps.getHeight(); y++) {
            // Est-ce que l'arbre doit spawn sur ce 'y'
            boolean spawnOrNot = Math.random() < spawnRate;

            if (spawnOrNot) {
                ArrayList<Integer> posFloor = new ArrayList<>();
                // Range les positions du sol sur la ligne 'y' pour tirer au sort l'un dans la liste
                for (int x = 0; x < tileMaps.getWidth(); x++) {
                    int targetTile = tileMaps.getTile(x, y);

                    if (targetTile == TileMaps.FLOOR_TOP || targetTile == TileMaps.FLOOR_RIGHT || targetTile == TileMaps.FLOOR_LEFT)
                        posFloor.add(x);
                }

                // Si il y a du sol sur la ligne
                if (!posFloor.isEmpty()) {
                    int whereSpawn = random.nextInt(posFloor.size());
                    int xTallGrass = posFloor.get((whereSpawn == 0) ? whereSpawn : whereSpawn - 1);

                    // Verifies au cas où si le tile au-dessus de lui est bien une casse vide (Du ciel)
                    if (tileMaps.getTile(xTallGrass, y - 1) == 0) {
                        TallGrass tallGrass = new TallGrass(xTallGrass * widthTile, (y-1) * heightTile);

                        for (Entity entity : entities)
                            if (entity instanceof TallGrass && tallGrass.getY() == entity.getY() && tallGrass.getX() == entity.getX())
                                // Un arbre est déjà present ? Il ne le génère pas et arrête complétement la fonction
                                return;
                        // Une fois une position trouvée, on l'ajoute en tant qu'entité pour qu'il puisse ensuite l'affiché
                        entities.add(0, tallGrass);
                    }
                    // Une fois l'arbre généré, il arrête complétement toutes la fontion
                    return;
                }
                // Sinon on retourne vers la premiere boucle 'for'
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
