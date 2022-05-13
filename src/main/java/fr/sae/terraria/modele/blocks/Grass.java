package fr.sae.terraria.modele.blocks;

public class Grass extends Block
{
    public Grass(int x, int y){
        super(x, y);

    }

    @Override
    public void updates()
    {
        this.rect.update(x.get(), y.get());
    }
}
