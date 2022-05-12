package fr.sae.terraria.modele;

import fr.sae.terraria.modele.entities.*;
import java.util.ArrayList;


public class Environment
{
    private Player player;
    private ArrayList<Entity> entities;


    public Environment()
    {
        entities = new ArrayList<>();

        player = new Player(0,0);
        player.setPv(20);
        player.setVelocity(2);

    }

    public ArrayList<Entity> getEntities() { return entities; }
    public Player getPlayer() { return player; }
}
