package fr.sae.terraria.modele;

import fr.sae.terraria.modele.entities.Rabbit;
import fr.sae.terraria.modele.entities.Slime;
import fr.sae.terraria.modele.entities.blocks.TallGrass;
import fr.sae.terraria.modele.entities.blocks.Tree;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.SpawnableObjectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static fr.sae.terraria.modele.entities.Rabbit.RABBIT_SPAWN_RATE;
import static fr.sae.terraria.modele.entities.Rabbit.WHEN_SPAWN_A_RABBIT;
import static fr.sae.terraria.modele.entities.Slime.SLIME_SPAWN_RATE;
import static fr.sae.terraria.modele.entities.Slime.WHEN_SPAWN_A_SLIME;
import static fr.sae.terraria.modele.entities.blocks.TallGrass.TALL_GRASS_SPAWN_RATE;
import static fr.sae.terraria.modele.entities.blocks.TallGrass.WHEN_SPAWN_A_TALL_GRASS;
import static fr.sae.terraria.modele.entities.blocks.Tree.TREE_SPAWN_RATE;


/**
 * <h1>Generate Entity</h1>
 * <h2>Une classe qui permet de générer aléatoirement des entités</h2>
 */
public class GenerateEntity
{
    public static final int MAX_SPAWN_RABBIT = 100;
    public static final int MAX_SPAWN_SLIME = 100;
    private static final Random random = new Random();


    public GenerateEntity() { super(); }

    /**
     * Génère une entité selon le momnt ou il est sensé apparaitre et son pourcentage de chances d'apparaitre.
     *
     * @param e L'entité concernée
     * @param whenSpawn Le nombre qui determine quand il doit spawn sur la carte
     * @param spawnRate Le pourcentage de chance qu'il spawn réellement à l'endroit où l'on souhaite le placer
     */
    private static void generateAnEntity(Environment environment, SpawnableObjectType e, int whenSpawn, double spawnRate)
    {
        List<Entity> entities = environment.getEntities();
        TileMaps maps = environment.getTileMaps();
        int widthTile = environment.widthTile;
        int heightTile = environment.heightTile;
        int ticks = environment.getTicks();

        // Fréquence d'apparition
        if (ticks%whenSpawn == 0)
            for (int y = 0; y < maps.getHeight(); y++)
                // Est-ce que l'arbre doit spawn sur ce 'y'
                if (Math.random() < spawnRate) {
                    List<Integer> locFloorsOnAxisX = findFloors(maps, y);
                    // Si il y a du sol sur la ligne
                    if (!locFloorsOnAxisX.isEmpty()) {
                        int onWhichFloor = random.nextInt(locFloorsOnAxisX.size());
                        int targetFloor = locFloorsOnAxisX.get((onWhichFloor == 0) ? onWhichFloor : onWhichFloor - 1);
                        int xEntity = targetFloor * widthTile;
                        int yEntity = ((y == 0) ? y : (y - 1)) * heightTile;
                        // Verifies au cas où si le tile au-dessus de lui est bien une casse vide (Du ciel)
                        if (maps.getTile(targetFloor, y - 1) == TileMaps.SKY) {
                            for (Entity entity : entities)
                                // Une entité est déjà present ? Il ne le génère pas et arrête complétement la fonction
                                if (xEntity == entity.getX() && yEntity == entity.getY())
                                    return;
                            e.spawn(xEntity, yEntity);
                        }
                        // Une fois l'entité générée, il arrête complétement toute la fonction
                        return;
                    }
                    // Sinon on retourne vers la premiere boucle 'for'
                }
    }

    /** Range les positions du sol sur la ligne 'y' */
    private static List<Integer> findFloors(TileMaps maps, int y)
    {
        List<Integer> localisation = new ArrayList<>();
        for (int x = 0; x < maps.getWidth(); x++) {
            int targetTile = maps.getTile(x, y);

            if (targetTile == TileMaps.FLOOR_TOP || targetTile == TileMaps.FLOOR_RIGHT || targetTile == TileMaps.FLOOR_LEFT)
                localisation.add(x);
        }

        return localisation;
    }

    /** À un certain moment, grace au tick, il va générer des arbres +/- grand uniquement sur un sol */
    public static void treeRandomly(Environment environment) { generateAnEntity(environment, new Tree(environment), 1, TREE_SPAWN_RATE); }
    /** À un certain moment, grace au tick, il va générer des hautes herbes sur un sol */
    public static void tallGrassRandomly(Environment environment) { generateAnEntity(environment, new TallGrass(environment), WHEN_SPAWN_A_TALL_GRASS, TALL_GRASS_SPAWN_RATE); }

    /** À un certain moment, grace au tick et à l'horloge du jeu, il va générer des lapins sur un sol */
    public static void rabbitRandomly(Environment environment)
    {
        if (environment.getRabbits().size() < MAX_SPAWN_RABBIT)
            generateAnEntity(environment, new Rabbit(environment), WHEN_SPAWN_A_RABBIT, RABBIT_SPAWN_RATE);
    }

    /** À un certain moment, grace au tick et à l'horloge du jeu, il va générer des slimes sur un sol  */
    public static void slimeRandomly(Environment environment)
    {
        if (environment.getSlimes().size() < MAX_SPAWN_SLIME)
            generateAnEntity(environment, new Slime(environment), WHEN_SPAWN_A_SLIME, SLIME_SPAWN_RATE);
    }

    public static void slime(Environment environment) { generateAnEntity(environment, new Slime(environment), 1, 1); }
}
