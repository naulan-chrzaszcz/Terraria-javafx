package fr.sae.terraria.modele;


import fr.sae.terraria.modele.entities.entity.Gravity;

public interface CollideObjectType
{
    Gravity GRAVITY = new Gravity();

    int COLLISION_TOLERANCE = 3;


    void collide();
}
