package fr.sae.terraria.modele;


import fr.sae.terraria.modele.entities.entity.Gravity;

public interface CollideObjectType
{
    int COLLISION_TOLERANCE = 3;


    void collide();
}
