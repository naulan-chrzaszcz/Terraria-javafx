package fr.sae.terraria.modele.entities.blocks;


import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;

public class Stone extends Block implements StowableObjectType, CollideObjectType
{


    public Stone(int x, int y){
        super(x, y);

    }

    public void updates() { /* TODO document why this method is empty */ }


    public void collide() { /* NE RIEN REMPLIR */ }
}
