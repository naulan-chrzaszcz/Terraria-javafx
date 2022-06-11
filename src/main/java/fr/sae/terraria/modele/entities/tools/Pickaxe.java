package fr.sae.terraria.modele.entities.tools;


public class Pickaxe extends Tool
{


    public Pickaxe()
    {
        super(Tool.DEFAULT_DURABILITY);
    }

    @Override public void use() { Tool.DEFAULT_WEAR(this); }
}
