package fr.sae.terraria.modele.entities.player.inventory;

import fr.sae.terraria.modele.entities.blocks.Dirt;
import fr.sae.terraria.modele.entities.blocks.Stone;
import fr.sae.terraria.modele.entities.blocks.Torch;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.modele.entities.items.Fiber;
import fr.sae.terraria.modele.entities.items.Wood;
import fr.sae.terraria.modele.entities.player.Player;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;


public class Inventory
{
    public static final int BLOCK_STACKING_MAX = 16;
    public static final int NB_BOXES_MAX = 27;
    public static final int NB_LINES = 3;

    private final EnumMap<KeyCode, Boolean> keysInput;
    private final ObservableList<StowableObjectType>[][] value; // Tableau 2D qui correspond aux lignes de l'inventaire et ensuite le nombre de cases par ligne

    private final IntegerProperty posCursorHorizontallyInventoryBar;
    private final IntegerProperty posCursorVerticallyInventoryBar;
    private int scroll;


    public Inventory(final Player player)
    {
        super();
        int nbElementOnOneLineOfInventory = (Inventory.NB_BOXES_MAX / Inventory.NB_LINES);

        this.keysInput = new EnumMap<>(KeyCode.class);
        this.value = new ObservableList[Inventory.NB_LINES][nbElementOnOneLineOfInventory];
        for (int i = 0; i < Inventory.NB_LINES; i++)
            for (int j = 0; j < nbElementOnOneLineOfInventory; j++)
                this.value[i][j] = FXCollections.observableArrayList();

        this.posCursorHorizontallyInventoryBar = new SimpleIntegerProperty(0);
        this.posCursorVerticallyInventoryBar = new SimpleIntegerProperty(0);

        // Change l'item de la main du joueur
        posCursorHorizontallyInventoryBarProperty().addListener((obs, oldV, newV) -> {
            boolean isntOutOfInventoryBar = newV.intValue() >= 0 && newV.intValue() < nbElementOnOneLineOfInventory;

            if (isntOutOfInventoryBar) {
                ObservableList<StowableObjectType> stack = this.get()[getPosCursorVerticallyInventoryBar()][newV.intValue()];
                player.setItemSelected((!stack.isEmpty()) ? stack.get(0) : null);
            }
        });
    }

    private int nbStacksIntoInventory()
    {
        int counter = 0;
        for (int i = 0; i < Inventory.NB_LINES; i++)
            for (int j = 0; j < Inventory.NB_BOXES_MAX / Inventory.NB_LINES; j++)
                counter += (!this.value[i][j].isEmpty() && !Objects.isNull(this.value[i][j].get(0))) ? 1 : 0;

        return counter;
    }

    /**
     * Place des objets de type rangeable dans l'inventaire.
     * @param object Un objet de type rangeable à mettre dans l'inventaire.
     */
    public void put(StowableObjectType object)
    {
        int nbStacksInventory = nbStacksIntoInventory();

        /* TODO MES YEUX */
        if (nbStacksInventory < NB_BOXES_MAX) for (int i = 0; i < NB_LINES; i++) {
            boolean isFull = false; // TODO Il n'est pas utilisé
            for (int j = 0; j < NB_BOXES_MAX / NB_LINES; j++) {
                isFull = false;     // TODO Il n'est pas utilisé, et pourquoi le mettre en false deux fois de suite ?.
                int beforeSize = this.value[i][j].size();

                // TODO: des conditions qui peuvent êtres simplifié.
                if (this.value[i][j].isEmpty())
                    this.value[i][j].add(object);
                else if (this.value[i][j].size() == BLOCK_STACKING_MAX)
                    isFull = true;  // TODO unused
                else if (this.value[i][j].get(0) instanceof Dirt && object instanceof Dirt) {
                    this.value[i][j].add(object);
                    isFull = false; // TODO unused
                } else if (this.value[i][j].get(0) instanceof Stone && object instanceof Stone) {
                    this.value[i][j].add(object);
                    isFull = false; // TODO unused
                } else if (this.value[i][j].get(0) instanceof Torch && object instanceof Torch) {
                    this.value[i][j].add(object);
                    isFull = false; // TODO unused
                } else if (this.value[i][j].get(0) instanceof Fiber && object instanceof Fiber) {
                    this.value[i][j].add(object);
                    isFull = false; // TODO unused
                } else if (this.value[i][j].get(0) instanceof Wood && object instanceof Wood) {
                    this.value[i][j].add(object);
                    isFull = false; // TODO unused
                }

                // Quand un objet a été mise dans l'inventaire, il arrête la fonction
                if (beforeSize != this.value[i][j].size())
                    return;
            }
        }
    }

    /** Les évènements du clavier qui sont liée à l'inventaire. */
    public void eventInput()
    {
        boolean scrollUp = this.scroll > 0;
        boolean scrollDown = this.scroll < 0;
        if (scrollUp)
            this.posCursorHorizontallyInventoryBar.set(this.getPosCursorHorizontallyInventoryBar() + 1);
        else if (scrollDown)
            this.posCursorHorizontallyInventoryBar.set(this.getPosCursorHorizontallyInventoryBar() - 1);

        boolean outOfInventoryBarOnRight = this.getPosCursorHorizontallyInventoryBar() > (NB_BOXES_MAX / NB_LINES)-1;
        boolean outOfInventoryBarOnLeft = this.getPosCursorHorizontallyInventoryBar() < 0;
        if (outOfInventoryBarOnRight)
            this.posCursorHorizontallyInventoryBar.set(0);
        else if (outOfInventoryBarOnLeft)
            this.posCursorHorizontallyInventoryBar.set((NB_BOXES_MAX / NB_LINES)-1);

        this.keysInput.forEach((key, value) -> {
            if (value.equals(Boolean.TRUE)) {
                if (key.equals(KeyCode.DIGIT1))
                    this.posCursorHorizontallyInventoryBar.set(0);
                else if (key.equals(KeyCode.DIGIT2))
                    this.posCursorHorizontallyInventoryBar.set(1);
                else if (key.equals(KeyCode.DIGIT3))
                    this.posCursorHorizontallyInventoryBar.set(2);
                else if (key.equals(KeyCode.DIGIT4))
                    this.posCursorHorizontallyInventoryBar.set(3);
                else if (key.equals(KeyCode.DIGIT5))
                    this.posCursorHorizontallyInventoryBar.set(4);
                else if (key.equals(KeyCode.DIGIT6))
                    this.posCursorHorizontallyInventoryBar.set(5);
                else if (key.equals(KeyCode.DIGIT7))
                    this.posCursorHorizontallyInventoryBar.set(6);
                else if (key.equals(KeyCode.DIGIT8))
                    this.posCursorHorizontallyInventoryBar.set(7);
                else if (key.equals(KeyCode.DIGIT9))
                    this.posCursorHorizontallyInventoryBar.set(8);
            }
        });

        this.scroll = 0;
    }


    public IntegerProperty posCursorHorizontallyInventoryBarProperty() { return this.posCursorHorizontallyInventoryBar; }
    public IntegerProperty posCursorVerticallyInventoryBarProperty() { return this.posCursorVerticallyInventoryBar; }
    public int getPosCursorHorizontallyInventoryBar() { return this.posCursorHorizontallyInventoryBar.get(); }
    public int getPosCursorVerticallyInventoryBar() { return this.posCursorVerticallyInventoryBar.get(); }
    public ObservableList<StowableObjectType>[][] get() { return this.value; }
    public Map<KeyCode, Boolean> getKeysInput() { return this.keysInput; }

    public void setScroll(int newScroll) { this.scroll = newScroll; }
}
