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
}