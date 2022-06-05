package fr.sae.terraria.modele.entities.blocks;

import fr.sae.terraria.modele.Environment;

public class Grass extends Block
{


    public Grass(int x, int y) { super(x, y); }

    @Override public void updates() { /* TODO document why this method is empty */ }

    @Override public void breaks()
    {
        Environment.playSound("sound/grassyStep.wav", false);
    }
}
