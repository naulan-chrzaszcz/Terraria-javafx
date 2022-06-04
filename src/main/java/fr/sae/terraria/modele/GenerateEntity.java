package fr.sae.terraria.modele;

import fr.sae.terraria.modele.entities.Rabbit;
import fr.sae.terraria.modele.entities.blocks.TallGrass;
import fr.sae.terraria.modele.entities.blocks.Tree;
import fr.sae.terraria.modele.entities.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static fr.sae.terraria.modele.entities.Rabbit.RABBIT_SPAWN_RATE;
import static fr.sae.terraria.modele.entities.Rabbit.WHEN_SPAWN_A_RABBIT;
import static fr.sae.terraria.modele.entities.blocks.TallGrass.TALL_GRASS_SPAWN_RATE;
import static fr.sae.terraria.modele.entities.blocks.TallGrass.WHEN_SPAWN_A_TALL_GRASS;
import static fr.sae.terraria.modele.entities.blocks.Tree.TREE_SPAWN_RATE;
import static fr.sae.terraria.modele.entities.blocks.Tree.WHEN_SPAWN_A_TREE;


public class GenerateEntity
{
    public static final int MAX_SPAWN_RABBIT = 100;
    private static final Random random = new Random();


    /**
     * Génère une entité selon de quand il spawn et du pourcent de change qu'il spawn réellement.
     *
     * @param e L'entité concernée
     * @param whenSpawn Le nombre qui determine quand il doit spawn sur la carte
     * @param spawnRate Le pourcentage de chance qu'il spawn réellement à l'endroit qu'on souhaite le placer
     */
    private static Entity generateAnEntity(Environment environment, Entity e, int whenSpawn, double spawnRate)
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
                                    return null;
                            e.setX(xEntity);
                            e.setY(yEntity);
                            e.getGravity().setXInit(xEntity);
                            e.getGravity().setYInit(yEntity);
                            e.setRect(widthTile, heightTile);
                            // Une fois une position trouvée, on l'ajoute en tant qu'entité pour qu'il puisse ensuite l'affiché
                            entities.add(0, e);
                        }
                        // Une fois l'arbre généré, il arrête complétement toute la fonction
                        return e;
                    }
                    // Sinon on retourne vers la premiere boucle 'for'
                }
        return null;
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
    public static void tree(Environment environment) { generateAnEntity(environment, new Tree(), WHEN_SPAWN_A_TREE, TREE_SPAWN_RATE); }
    /** À un certain moment, grace au tick, il va générer des hautes herbes sur un sol */
    public static void tallGrass(Environment environment) { generateAnEntity(environment, new TallGrass(), WHEN_SPAWN_A_TALL_GRASS, TALL_GRASS_SPAWN_RATE); }
    /** À un certain moment, grace au tick, il va générer des lapins sur un sol */
    public static void rabbit(Environment environment) {
        if (environment.getRabbits().size() < MAX_SPAWN_RABBIT) {
            Rabbit rabbit = (Rabbit) generateAnEntity(environment, new Rabbit(environment), WHEN_SPAWN_A_RABBIT, RABBIT_SPAWN_RATE);
            if (!Objects.isNull(rabbit))
                environment.getRabbits().add(rabbit);
        }
    }
}
