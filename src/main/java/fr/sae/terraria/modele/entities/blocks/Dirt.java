package fr.sae.terraria.modele.entities.blocks;


import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;

public class Dirt extends Block implements StowableObjectType, CollideObjectType
{


    public Dirt(int x, int y)
    {
        super(x, y);
    }

    public void updates() { /* TODO document why this method is empty */ }

    public void collide() { /* NE RIEN REMPLIR */ }
}
