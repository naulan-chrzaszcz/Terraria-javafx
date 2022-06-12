package fr.sae.terraria.modele.entities.player;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.Rabbit;
import fr.sae.terraria.modele.entities.blocks.Dirt;
import fr.sae.terraria.modele.entities.items.Coal;
import fr.sae.terraria.modele.entities.items.Meat;
import fr.sae.terraria.modele.entities.items.Vodka;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;


public class PlayerTest
{
    private static Environment environment;


    public PlayerTest() { super(); }

    @BeforeAll public static void init()
    {
        environment = new Environment(1., 1.);
    }

    @Test public final void moveTest()
    {
        double previousPosX;

        Player player = environment.getPlayer();
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

    }

    @Test public final void hitTest()
    {
        Player player = environment.getPlayer();
        double previousPv = player.getPv();
        player.hit();

        assertEquals(previousPv - 1, player.getPv(),
                "Vérifie s'il y a bien subit un dégât");
    }

    @Test public final void spawnTest()
    {
        Player player = environment.getPlayer();
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

    }

    @Test public final void interactWithBlockTest()     // TODO: Le nom est probablement mal choisie
    {
        Rectangle2D blockSelected;
        double beforePv;
        Player player = environment.getPlayer();

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
        Player player = environment.getPlayer();
        TileMaps tileMaps = environment.getTileMaps();
        player.pickup(new Dirt(environment, 1, 1));

        assertEquals(tileMaps.getTile(0, 0), TileMaps.SKY);
        player.placeBlock(0, 0);
        assertEquals(tileMaps.getTile(0, 0), TileMaps.DIRT);

        assertNull(player.getStackSelected());
    }

    @Test public final void drunkTest()
    {
        Player player = environment.getPlayer();
        player.pickup(new Vodka(environment));

        assertFalse(player.drunkProperty().get(),
                "Vérifie si il n'est pas bourré");
        ((Vodka) player.getStackSelected().getItem()).consumes();
        assertTrue(player.drunkProperty().get(),
                "Verife si il est bourré");
    }

    @Test public final void pickupTest()
    {
        Player player = environment.getPlayer();
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
