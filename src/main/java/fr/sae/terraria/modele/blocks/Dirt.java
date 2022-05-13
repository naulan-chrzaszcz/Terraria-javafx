package fr.sae.terraria.modele.blocks;


public class Dirt extends Block
{
    public Dirt(int x, int y)
    {
        super(x, y);
    }

    public void updates()
    {
        this.rect.update(x.get(), y.get());
    }
}
