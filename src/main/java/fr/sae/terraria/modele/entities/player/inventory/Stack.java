package fr.sae.terraria.modele.entities.player.inventory;

import fr.sae.terraria.modele.entities.Arrow;
import fr.sae.terraria.modele.entities.blocks.Dirt;
import fr.sae.terraria.modele.entities.blocks.Rock;
import fr.sae.terraria.modele.entities.blocks.TallGrass;
import fr.sae.terraria.modele.entities.blocks.Torch;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.modele.entities.items.*;
import fr.sae.terraria.modele.entities.tools.Axe;
import fr.sae.terraria.modele.entities.tools.Bow;
import fr.sae.terraria.modele.entities.tools.Pickaxe;
import fr.sae.terraria.modele.entities.tools.Sword;
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

    public boolean isSameItem(StowableObjectType object)
    {
        if (object instanceof Dirt && this.item instanceof Dirt)
            return true;
        else if (object instanceof Rock && this.item instanceof Rock)
            return true;
        else if (object instanceof TallGrass && this.item instanceof TallGrass)
            return true;
        else if (object instanceof Torch && this.item instanceof Torch)
            return true;
        else if (object instanceof Coal && this.item instanceof Coal)
            return true;
        else if (object instanceof Fiber && this.item instanceof Fiber)
            return true;
        else if (object instanceof Iron && this.item instanceof Iron)
            return true;
        else if (object instanceof Meat && this.item instanceof Meat)
            return true;
        else if (object instanceof Stone && this.item instanceof Stone)
            return true;
        else if (object instanceof Silex && this.item instanceof Silex)
            return true;
        else if (object instanceof Wood && this.item instanceof Wood)
            return true;
        else if (object instanceof Axe && this.item instanceof Axe)
            return true;
        else if (object instanceof Vodka && this.item instanceof Vodka)
            return true;
        else if (object instanceof Bow && this.item instanceof Bow)
            return true;
        else if (object instanceof Pickaxe && this.item instanceof Pickaxe)
            return true;
        else if (object instanceof Sword && this.item instanceof Sword)
            return true;
        else if (object instanceof Arrow && this.item instanceof Arrow)
            return true;
        else if (object instanceof Sword && this.item instanceof Sword)
            return true;
        else return false;
    }

    public IntegerProperty nbItemsProperty() { return this.nbItems; }

    public boolean isFull() { return this.getNbItems() >= Stack.MAX; }
    public void add() { if (this.getNbItems() < Stack.MAX) this.nbItems.set(this.getNbItems() + 1); }
    public void remove() { if (this.getNbItems() > 0) this.nbItems.set(this.getNbItems() - 1); }


    public int getNbItems() { return this.nbItems.get(); }
    public StowableObjectType getItem() { return this.item; }

    public void setItem(StowableObjectType newItem) { this.item = newItem; }
}
