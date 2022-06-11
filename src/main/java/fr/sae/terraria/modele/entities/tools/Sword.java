package fr.sae.terraria.modele.entities.tools;


public class Sword extends Tool
{
    public static final int DEFAULT_DAMAGE = 2;


    public Sword(double material)
    {
        super(Tool.DEFAULT_DURABILITY);

        this.material = material;
    }

    @Override public void use() { Tool.DEFAULT_WEAR(this); }
}
