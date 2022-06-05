package fr.sae.terraria.modele.entities.tools;


public class Pickaxe extends Tool
{


    public Pickaxe()
    {
        super(Tool.DEFAULT_DURABILITY);
    }

    @Override public void use()
    {
        if (this.durability.get() > 0)
            this.durability.set(this.durability.get() - 1);
    }
}
