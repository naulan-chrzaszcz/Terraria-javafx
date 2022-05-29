package fr.sae.terraria.modele.entities.entity;

import fr.sae.terraria.modele.Environment;

import java.util.List;


public interface ReproductiveObjectType
{

    void reproduction(Environment environment);
    List<Entity> getChildren();
}
