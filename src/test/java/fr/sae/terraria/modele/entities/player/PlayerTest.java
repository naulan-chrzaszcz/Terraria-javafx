package fr.sae.terraria.modele.entities.player;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.items.Coal;
import fr.sae.terraria.modele.entities.items.Meat;
import fr.sae.terraria.modele.entities.items.Vodka;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

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
}
