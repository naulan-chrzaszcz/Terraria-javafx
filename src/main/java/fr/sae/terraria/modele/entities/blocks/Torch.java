package fr.sae.terraria.modele.entities.blocks;

import fr.sae.terraria.modele.entities.entity.StowableObjectType;

public class Torch extends Block implements StowableObjectType
{
    public Torch(int x, int y){ super(x, y); }

    @Override
    public void updates() { }

    public void breaks() { }
}