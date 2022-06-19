package fr.sae.terraria.modele.entities.tools;

import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class Tool implements StowableObjectType
{
    public static final double CRAFTED_WITH_WOOD = 1.;
    public static final double CRAFTED_WITH_IRON = 1.5;

    public static final int DEFAULT_DURABILITY = 100;
    public static final int DEFAULT_DAMAGE = 1;

    private final IntegerProperty durability;

    private final MaterialSet material;
    private final ToolSet tool;


    public Tool(final ToolSet tool, final MaterialSet material)
    {
        super();
        this.tool = tool;
        this.material = material;

        this.durability = new SimpleIntegerProperty((int) (Tool.DEFAULT_DURABILITY*material.getDurabilityMultiplicator()));
    }

    /** Utilise l'outil */
    public void use()
    {
        if (this.durability.get() > 0)
            this.durability.set(this.durability.get() - 1);
    }

    public double damage()
    {
        if (this.tool == ToolSet.SWORD) {
            if (this.material == MaterialSet.WOOD)
                return Tool.DEFAULT_DAMAGE * Tool.CRAFTED_WITH_WOOD;
            else if (this.material == MaterialSet.IRON)
                return Tool.DEFAULT_DAMAGE * Tool.CRAFTED_WITH_IRON;
        } else {
            if (this.material == MaterialSet.WOOD)
                return Tool.DEFAULT_DAMAGE * (Tool.CRAFTED_WITH_WOOD/2);
            else if (this.material == MaterialSet.IRON)
                return Tool.DEFAULT_DAMAGE * (Tool.CRAFTED_WITH_IRON/2);
        }
        return Tool.DEFAULT_DAMAGE;
    }

    public static boolean isAxe(Tool tool) { return tool.getTypeOfTool() == ToolSet.AXE; }
    public static boolean isBow(Tool tool) { return tool.getTypeOfTool() == ToolSet.BOW; }
    public static boolean isPickaxe(Tool tool) { return tool.getTypeOfTool() == ToolSet.PICKAXE; }
    public static boolean isSword(Tool tool) { return tool.getTypeOfTool() == ToolSet.SWORD; }
    public static boolean isArrow(Tool tool) { return tool.getTypeOfTool() == ToolSet.ARROW; }


    public ToolSet getTypeOfTool() { return this.tool; }
    public MaterialSet getMaterial() { return this.material; }
}
