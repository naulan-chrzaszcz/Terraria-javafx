package fr.sae.terraria.modele.entities.player.inventory;

import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.modele.entities.player.Player;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;

import java.util.EnumMap;
import java.util.Map;


public class Inventory
{
    public static final int NB_BOXES_MAX = 27;
    public static final int NB_LINES = 3;

    private final EnumMap<KeyCode, Boolean> keysInput;
    private final ObservableList<Stack> value;

    private final IntegerProperty posCursor;
    private final Player player;
    private int scroll;


    public Inventory(final Player player)
    {
        super();
        this.player = player;

        int nbElementOnOneLineOfInventory = (Inventory.NB_BOXES_MAX / Inventory.NB_LINES);
        this.keysInput = new EnumMap<>(KeyCode.class);
        this.value = FXCollections.observableArrayList();

        this.posCursor = new SimpleIntegerProperty(0);

        // Change l'item qui se trouve dans la main du joueur
        this.posCursorProperty().addListener((obs, oldPos, newPos) -> {
            boolean isntOutOfInventoryBar = newPos.intValue() >= 0 && newPos.intValue() < nbElementOnOneLineOfInventory;

            if (isntOutOfInventoryBar)
                refreshStack();
        });
    }

    public void refreshStack()
    {
        Stack stack = null;
        if (this.getPosCursor() < this.get().size())
            stack = this.get().get(this.getPosCursor());
        this.player.setStackSelected(stack);
    }

    private void createStack(StowableObjectType item)
    {
        Stack stack = new Stack();
        stack.nbItemsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() <= 0) {
                this.value.remove(stack);
                this.player.setStackSelected(null);
            }
        });
        stack.setItem(item);
        this.value.add(stack);
        this.player.setStackSelected(stack);
    }

    public int nbStacksIntoInventory() { return this.value.size(); }

    /**
     * Place des objets de type rangeable dans l'inventaire.
     * @param item Un objet de type rangeable à mettre dans l'inventaire.
     */
    public void put(StowableObjectType item)
    {
        int nbStacksInventory = this.nbStacksIntoInventory();

        if (nbStacksInventory < NB_BOXES_MAX) {
            if (nbStacksInventory == 0) {
                this.createStack(item);
            } else {
                for (Stack stack : this.value) {
                    int beforeSize = stack.getNbItems();
                    if (!stack.isFull() && stack.isSameItem(item))
                        stack.add();

                    // Quand un objet a été mise dans l'inventaire, il arrête la fonction
                    int afterSize = stack.getNbItems();
                    if (beforeSize != afterSize)
                        return;
                }
                // Si tous les stacks present sont pleins ou aucune ne correspond à l'objet qui à étais pris, il crée un nouveau stack
                if (this.value.size() < (Inventory.NB_BOXES_MAX / Inventory.NB_LINES))
                    this.createStack(item);
            }
        }
    }

    /** Les évènements du clavier qui sont liée à l'inventaire. */
    public void eventInput()
    {
        boolean scrollUp = this.scroll > 0;
        boolean scrollDown = this.scroll < 0;
        if (scrollUp)
            this.posCursor.set(this.getPosCursor() + 1);
        else if (scrollDown)
            this.posCursor.set(this.getPosCursor() - 1);

        boolean outOfInventoryBarOnRight = this.getPosCursor() > (NB_BOXES_MAX / NB_LINES)-1;
        boolean outOfInventoryBarOnLeft = this.getPosCursor() < 0;
        if (outOfInventoryBarOnRight)
            this.posCursor.set(0);
        else if (outOfInventoryBarOnLeft)
            this.posCursor.set((NB_BOXES_MAX / NB_LINES)-1);

        this.keysInput.forEach((key, value) -> {
            if (value.equals(Boolean.TRUE)) {
                if (key.equals(KeyCode.DIGIT1))
                    this.posCursor.set(0);
                else if (key.equals(KeyCode.DIGIT2))
                    this.posCursor.set(1);
                else if (key.equals(KeyCode.DIGIT3))
                    this.posCursor.set(2);
                else if (key.equals(KeyCode.DIGIT4))
                    this.posCursor.set(3);
                else if (key.equals(KeyCode.DIGIT5))
                    this.posCursor.set(4);
                else if (key.equals(KeyCode.DIGIT6))
                    this.posCursor.set(5);
                else if (key.equals(KeyCode.DIGIT7))
                    this.posCursor.set(6);
                else if (key.equals(KeyCode.DIGIT8))
                    this.posCursor.set(7);
                else if (key.equals(KeyCode.DIGIT9))
                    this.posCursor.set(8);
            }
        });

        this.scroll = 0;
    }
    public IntegerProperty posCursorProperty() { return this.posCursor; }


    public Stack getStack() { return this.value.get(this.getPosCursor()); }
    public int getPosCursor() { return this.posCursor.get(); }
    public ObservableList<Stack> get() { return this.value; }
    public Map<KeyCode, Boolean> getKeysInput() { return this.keysInput; }

    public void setScroll(int newScroll) { this.scroll = newScroll; }
}
