package fr.sae.terraria.modele.entities.blocks;


import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.CollideObjectType;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;

public class Stone extends Block implements StowableObjectType, CollideObjectType
{
    private Environment environment;


    public Stone(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;
    }

    public void updates() { /* TODO document why this method is empty */ }

    public void collide() { /* NE RIEN REMPLIR */ }

    public void breaks()
    {
        Environment.playSound("sound/brick" + ((int) (Math.random()*2)+1) + ".wav", false);
        this.environment.getPlayer().pickup(this);
    }
}
