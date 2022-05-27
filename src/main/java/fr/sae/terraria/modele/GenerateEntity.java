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
    private static final double TALLGRASS_SPAWN_RATE = .3;
    private static final double RABBIT_SPAWN_RATE = .5;
    private static final int WHEN_SPAWN_A_TREE = 5_000;
    private static final int WHEN_SPAWN_A_TALLGRASS = 2_500;
    private static final int WHEN_SPAWN_A_RABBIT = 2_500;

    private static final Random random = new Random();


    /** À un certain moment, grace au tick, il va générer des arbres +/- grand uniquement sur un sol */
    public static void tree(Environment environment)
    {
        List<Entity> entities = environment.getEntities();
        TileMaps maps = environment.getTileMaps();
        int widthTile = environment.widthTile;
        int heightTile = environment.heightTile;
        int ticks = environment.getTicks();

        // Fréquence d'apparition
        if (ticks%WHEN_SPAWN_A_TREE == 0)
            for (int y = 0; y < maps.getHeight(); y++)
                // Est-ce que l'arbre doit spawn sur ce 'y'
                if (Math.random() < TREE_SPAWN_RATE) {
                    List<Integer> locFloorsOnAxisX = findFloors(maps, y);
                    // Si il y a du sol sur la ligne
                    if (!locFloorsOnAxisX.isEmpty()) {
                        int onWhichFloor = random.nextInt(locFloorsOnAxisX.size());
                        int targetFloor = locFloorsOnAxisX.get((onWhichFloor == 0) ? onWhichFloor : onWhichFloor - 1);
                        int xTree = targetFloor * widthTile;
                        int yTree = ((y == 0) ? y : (y - 1)) * heightTile;
                        // Verifies au cas où si le tile au-dessus de lui est bien une casse vide (Du ciel)
                        if (maps.getTile(targetFloor, y - 1) == TileMaps.SKY) {
                            for (Entity entity : entities)
                                if (entity instanceof Tree && xTree == entity.getX() && yTree == entity.getY())
                                    // Un arbre est déjà present ? Il ne le génère pas et arrête complétement la fonction
                                    return;
                            // Une fois une position trouvée, on l'ajoute en tant qu'entité pour qu'il puisse ensuite l'affiché
                            Tree tree = new Tree(xTree, yTree);
                            entities.add(0, tree);
                        }
                        // Une fois l'arbre généré, il arrête complétement toute la fonction
                        return;
                    }
                    // Sinon on retourne vers la premiere boucle 'for'
                }
    }

    public static void tallGrass(Environment environment)
    {
        List<Entity> entities = environment.getEntities();
        TileMaps maps = environment.getTileMaps();
        int widthTile = environment.widthTile;
        int heightTile = environment.heightTile;
        int ticks = environment.getTicks();

        // Fréquence d'apparition
        if (ticks%WHEN_SPAWN_A_TALLGRASS == 0)
            for (int y = 0; y < maps.getHeight(); y++)
                // Est-ce que l'arbre doit spawn sur ce 'y'
                if (Math.random() < TALLGRASS_SPAWN_RATE) {
                    List<Integer> locFloorsOnAxisX = findFloors(maps, y);
                    // Si il y a du sol sur la ligne
                    if (!locFloorsOnAxisX.isEmpty()) {
                        int onWhichFloor = random.nextInt(locFloorsOnAxisX.size());
                        int targetFloor = locFloorsOnAxisX.get((onWhichFloor == 0) ? onWhichFloor : onWhichFloor - 1);
                        int xTallGrass = targetFloor * widthTile;
                        int yTallGrass = ((y == 0) ? y : (y - 1)) * heightTile;
                        // Verifies au cas où si le tile au-dessus de lui est bien une casse vide (Du ciel)
                        if (maps.getTile(targetFloor, y - 1) == TileMaps.SKY) {
                            for (Entity entity : entities)
                                if (entity instanceof TallGrass && xTallGrass == entity.getX() && yTallGrass == entity.getY())
                                    // Un arbre est déjà present ? Il ne le génère pas et arrête complétement la fonction
                                    return;
                            // Une fois une position trouvée, on l'ajoute en tant qu'entité pour qu'il puisse ensuite l'affiché
                            TallGrass tallGrass = new TallGrass(xTallGrass, yTallGrass);
                            tallGrass.setRect(widthTile, heightTile);
                            entities.add(0, tallGrass);
                        }
                        // Une fois l'arbre généré, il arrête complétement toute la fonction
                        return;
                    }
                    // Sinon on retourne vers la premiere boucle 'for'
                }
    }

    public static void rabbit(Environment environment)
    {
        List<Entity> entities = environment.getEntities();
        TileMaps maps = environment.getTileMaps();
        int widthTile = environment.widthTile;
        int heightTile = environment.heightTile;
        int ticks = environment.getTicks();

        // Fréquence d'apparition
        if (ticks%WHEN_SPAWN_A_RABBIT == 0)
            for (int y = 0; y < maps.getHeight(); y++)
                // Est-ce que l'arbre doit spawn sur ce 'y'
                if (Math.random() < RABBIT_SPAWN_RATE) {
                    List<Integer> locFloorsOnAxisX = findFloors(maps, y);
                    // Si il y a du sol sur la ligne
                    if (!locFloorsOnAxisX.isEmpty()) {
                        int onWhichFloor = random.nextInt(locFloorsOnAxisX.size());
                        int targetFloor = locFloorsOnAxisX.get((onWhichFloor == 0) ? onWhichFloor : onWhichFloor - 1);
                        int xRabbit = targetFloor * widthTile;
                        int yRabbit = ((y == 0) ? y : (y - 1)) * heightTile;
                        // Verifies au cas où si le tile au-dessus de lui est bien une casse vide (Du ciel)
                        if (maps.getTile(targetFloor, y - 1) == TileMaps.SKY) {
                            for (Entity entity : entities)
                                if (entity instanceof Rabbit && xRabbit == entity.getX() && yRabbit == entity.getY())
                                    // Un arbre est déjà present ? Il ne le génère pas et arrête complétement la fonction
                                    return;
                            // Une fois une position trouvée, on l'ajoute en tant qu'entité pour qu'il puisse ensuite l'affiché
                            Rabbit rabbit = new Rabbit(environment, xRabbit, yRabbit);
                            rabbit.setRect(widthTile, heightTile);
                            entities.add(0, rabbit);
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

}
