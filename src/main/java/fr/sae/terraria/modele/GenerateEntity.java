package fr.sae.terraria.modele;

import fr.sae.terraria.modele.blocks.TallGrass;
import fr.sae.terraria.modele.blocks.Tree;
import fr.sae.terraria.modele.entities.Rabbit;
import fr.sae.terraria.modele.entities.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GenerateEntity
{
    private static final double TREE_SPAWN_RATE = .2;
    private static final double TALL_GRASS_SPAWN_RATE = .3;
    private static final double RABBIT_SPAWN_RATE = .2;
    private static final int WHEN_SPAWN_A_TREE = 5_000;
    private static final int WHEN_SPAWN_A_TALL_GRASS = 2_500;
    private static final int WHEN_SPAWN_A_RABBIT = 2_500;

    private static final Random random = new Random();


    private static void generateAnEntity(Environment environment, Entity entity_1, int whenSpawn, double spawnRate)
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
                                if (entity instanceof Tree && xEntity == entity.getX() && yEntity == entity.getY())
                                    // Un arbre est déjà present ? Il ne le génère pas et arrête complétement la fonction
                                    return;
                            entity_1.setX(xEntity);
                            entity_1.setY(yEntity);
                            entity_1.getGravity().setXInit(xEntity);
                            entity_1.getGravity().setYInit(yEntity);
                            entity_1.setRect(widthTile, heightTile);
                            // Une fois une position trouvée, on l'ajoute en tant qu'entité pour qu'il puisse ensuite l'affiché
                            entities.add(0, entity_1);
                        }
                        // Une fois l'arbre généré, il arrête complétement toute la fonction
                        return;
                    }
                    // Sinon on retourne vers la premiere boucle 'for'
                }
    }

    /** Range les positions du sol sur la ligne 'y' pour tirer au sort l'un dans la liste */
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
    public static void tree(Environment environment) { generateAnEntity(environment, new Tree(), WHEN_SPAWN_A_TREE, TREE_SPAWN_RATE); }

    public static void tallGrass(Environment environment) { generateAnEntity(environment, new TallGrass(), WHEN_SPAWN_A_TALL_GRASS, TALL_GRASS_SPAWN_RATE); }

    public static void rabbit(Environment environment) { generateAnEntity(environment, new Rabbit(environment), WHEN_SPAWN_A_RABBIT, RABBIT_SPAWN_RATE); }
}
