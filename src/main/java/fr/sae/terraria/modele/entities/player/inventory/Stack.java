package fr.sae.terraria.modele.entities.player.inventory;

import fr.sae.terraria.modele.entities.blocks.Block;
import fr.sae.terraria.modele.entities.blocks.TallGrass;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.modele.entities.items.Item;
import fr.sae.terraria.modele.entities.items.Meat;
import fr.sae.terraria.modele.entities.items.Vodka;
import fr.sae.terraria.modele.entities.tools.Tool;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


/**
 * <h1>Stack</h1>
 * <h2>Objet de données lié à l'objet <code>Inventaire</code>.</h2>
 * <h3><u>Description:</u></h3>
 * Cette dataclass est présente pour savoir combien d'objets du même type se trouvent sur une case d'inventaire
 */
public class Stack
{
    public static final int MAX = 16;

    private StowableObjectType item = null;
    private final IntegerProperty nbItems;


    public Stack()
    {
        super();

        this.nbItems = new SimpleIntegerProperty(1);
    }

    public boolean isSameItem(StowableObjectType obj)
    {
        if (obj instanceof Meat && this.item instanceof Meat)
            return true;
        if (obj instanceof Vodka && this.item instanceof Vodka)
            return true;

        if (obj instanceof Block && this.item instanceof Block) {
            if (Block.isDirt((Block) obj) && Block.isDirt((Block) this.item))
                return true;
            if (Block.isRock((Block) obj) && Block.isRock((Block) this.item))
                return true;
            if (Block.isTallGrass((Block) obj) && Block.isTallGrass((Block) this.item))
                return true;
            if (Block.isTorch((Block) obj) && Block.isTorch((Block) this.item))
                return true;
        }

        if (obj instanceof Tool && this.item instanceof Tool) {
            if (Tool.isAxe(((Tool) obj).getTypeOfTool()) && Tool.isAxe(((Tool) this.item).getTypeOfTool()))
                return true;
            if (Tool.isBow(((Tool) obj).getTypeOfTool()) && Tool.isBow(((Tool) this.item).getTypeOfTool()))
                return true;
            if (Tool.isPickaxe(((Tool) obj).getTypeOfTool()) && Tool.isPickaxe(((Tool) this.item).getTypeOfTool()))
                return true;
            if (Tool.isSword(((Tool) obj).getTypeOfTool()) && Tool.isSword(((Tool) this.item).getTypeOfTool()))
                return true;
            if (Tool.isArrow(((Tool) obj).getTypeOfTool()) && Tool.isArrow(((Tool) this.item).getTypeOfTool()))
                return true;
        }

        if (obj instanceof Item && this.item instanceof Item) {
            if (Item.isCoal(obj) && Item.isCoal(this.item))
                return true;
            if (Item.isFiber(obj) && Item.isFiber(this.item))
                return true;
            if (Item.isIron(obj) && Item.isIron(this.item))
                return true;
            if (Item.isStone(obj) && Item.isStone(this.item))
                return true;
            if (Item.isSilex(obj) && Item.isSilex(this.item))
                return true;
            if (Item.isWood(obj) && Item.isWood(this.item))
                return true;
        }

        return false;
    }

    public IntegerProperty nbItemsProperty() { return this.nbItems; }

    public boolean isFull() { return this.getNbItems() >= Stack.MAX; }
    public void add() { if (this.getNbItems() < Stack.MAX) this.nbItems.set(this.getNbItems() + 1); }
    public void remove() { if (this.getNbItems() > 0) this.nbItems.set(this.getNbItems() - 1); }


    public int getNbItems() { return this.nbItems.get(); }
    public StowableObjectType getItem() { return this.item; }

    public void setItem(StowableObjectType newItem) { this.item = newItem; }
}
