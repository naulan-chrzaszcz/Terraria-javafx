package fr.sae.terraria.modele.entities.player;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.TileSet;
import fr.sae.terraria.modele.entities.Rabbit;
import fr.sae.terraria.modele.entities.blocks.Dirt;
import fr.sae.terraria.modele.entities.items.Meat;
import fr.sae.terraria.modele.entities.items.Vodka;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class PlayerTest
{
    private static Environment environment;
    private static Player player;


    public PlayerTest() { super(); }

    @BeforeAll public static void init()
    {
        environment = new Environment(1., 1.);
        player = environment.getPlayer();
    }

    @Test public final void moveTest()
    {
        double previousPosX;

        previousPosX = player.getX();
        player.moveRight();
        player.move();

        assertEquals(previousPosX + player.getVelocity(), player.getX(),
                "Test: déplacement vers la droite.");
        player.idleOnX();


        previousPosX = player.getX();
        player.moveLeft();
        player.move();

        assertEquals(previousPosX - player.getVelocity(), player.getX(),
                "Test: déplacement vers la gauche");
    }


    @Test public final void collideTest()
    {
        /*
        // Le TOP et le BOTTOM est un pet complex à tester dans un JUnit
        Dirt dirtLeft = new Dirt(environment, 1, 1);
        dirtLeft.setRect(1, 1);
        environment.getEntities().add(dirtLeft);
        Dirt dirtRight = new Dirt(environment, 5, 1);
        dirtRight.setRect(1, 1);
        environment.getEntities().add(dirtRight);
        player.setRect(1, 1);
        player.setVelocity(0);
        player.setY(1);

        player.setX(3);
        player.moveLeft();
        player.collide();

        assertTrue(player.isMovingLeft());

        player.setX(1);
        player.moveLeft();
        player.collide();

        assertTrue(player.isIDLEonX());

        player.setX(5);
        player.moveRight();
        player.collide();

        assertTrue(player.isIDLEonX());
         */
    }

    @Test public final void hitTest()
    {
        double previousPv = player.getPv();
        player.hit();

        assertEquals(previousPv - 1, player.getPv(),
                "Vérifie s'il y a bien subit un dégât");
    }

    @Test public final void spawnTest()
    {
        int spawnLocX = 100;
        int spawnLocY = 100;

        player.spawn(spawnLocX, spawnLocY);

        assertEquals(player.getX(), spawnLocX,
                "Regarde si la localisation en X est correcte");
        assertEquals(player.getY(), spawnLocY,
                "Regarde si la localisation en X est correcte");
        assertEquals(player.getGravity().xInit, spawnLocX,
                "Regarde si la localisation au niveau de la gestion de la gravité, en X est correcte");
        assertEquals(player.getGravity().yInit, spawnLocY,
                "Regarde si la localisation au niveau de la gestion de la gravité, en X est correcte");
    }

    @Test public final void worldLimitTest()
    {
        player.setX(2*TileMaps.TILE_DEFAULT_SIZE);
        player.moveLeft();
        player.worldLimit();

        assertTrue(player.isMovingLeft());

        player.setX(-TileMaps.TILE_DEFAULT_SIZE);
        player.moveLeft();
        player.worldLimit();

        assertTrue(player.isIDLEonX());

        player.setX(environment.getTileMaps().getWidth()*TileMaps.TILE_DEFAULT_SIZE + TileMaps.TILE_DEFAULT_SIZE);
        player.moveRight();
        player.worldLimit();

        assertTrue(player.isIDLEonX());
    }

    @Test public final void interactWithBlockTest()     // TODO: Le nom est probablement mal choisie dans player
    {
        Rectangle2D blockSelected;
        double beforePv;

        Dirt dirt = new Dirt(environment, 1, 1);
        dirt.setRect(1, 1);
        environment.getEntities().add(dirt);
        blockSelected = new Rectangle2D(1, 1, 1, 1);

        beforePv = dirt.getPv();
        player.interactWithBlock(blockSelected);
        assertEquals(beforePv - 1, dirt.getPv());
        environment.getEntities().remove(dirt);


        Rabbit rabbit = new Rabbit(environment, 10, 10);
        rabbit.setRect(1, 1);
        environment.getEntities().add(rabbit);
        blockSelected = new Rectangle2D(10, 10, 1, 1);

        beforePv = rabbit.getPv();
        player.interactWithBlock(blockSelected);
        assertEquals(beforePv - 1, rabbit.getPv());
    }

    @Test public final void placeBlockTest()
    {
        TileMaps tileMaps = environment.getTileMaps();
        player.pickup(new Dirt(environment, 1, 1));

        assertEquals(tileMaps.getTile(0, 0), TileSet.SKY.ordinal());
        player.placeBlock(0, 0);
        assertEquals(tileMaps.getTile(0, 0), TileSet.DIRT.ordinal());

        assertNull(player.getStackSelected());
    }

    @Test public final void drunkTest()
    {
        player.pickup(new Vodka(environment));

        assertFalse(player.drunkProperty().get(),
                "Vérifie si il n'est pas bourré");
        ((Vodka) player.getStackSelected().getItem()).consumes();
        assertTrue(player.drunkProperty().get(),
                "Verife si il est bourré");
    }

    @Test public final void pickupTest()
    {
        player.getInventory().posCursorProperty().set(0);


        // Test avec de la viande
        player.pickup(new Meat(environment));
        assertTrue(player.getStackSelected().getItem() instanceof Meat,
                "Verifie si le stack est bien de la viande");
        assertEquals(player.getStackSelected().getNbItems(), 1,
                "Verifie s'il y a bien 1 objet dans le stack");
        ((Meat) player.getStackSelected().getItem()).consumes();
        player.getStackSelected().remove();

        assertNull(player.getStackSelected());


        // Test avec du charbon
        player.pickup(new Coal());
        assertTrue(player.getStackSelected().getItem() instanceof Coal,
                "Verifie si le stack est bien du charbon");
        assertEquals(player.getStackSelected().getNbItems(), 1,
                "Verifie s'il y a bien 1 objet dans le stack");
        player.getStackSelected().remove();

        assertNull(player.getStackSelected());
    }
}
