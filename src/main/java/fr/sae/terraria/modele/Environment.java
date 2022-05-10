package fr.sae.terraria.modele;


import fr.sae.terraria.modele.entities.*;

import java.util.ArrayList;

public class Environment
{
    private ArrayList<Entity> entities;



    public Environment(){
        entities = new ArrayList<>();
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

}
