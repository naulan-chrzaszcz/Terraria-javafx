package fr.sae.terraria.modele.entities.player;

import fr.sae.terraria.modele.entities.blocks.Dirt;
import fr.sae.terraria.modele.entities.blocks.Stone;
import fr.sae.terraria.modele.entities.blocks.Torch;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.modele.entities.items.Fiber;
import fr.sae.terraria.modele.entities.items.Wood;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;

import java.util.EnumMap;
import java.util.Objects;


public class Inventory
{
    public static final int BLOCK_STACKING_MAX = 16;
    public static final int NB_BOXES_MAX = 27;
    public static final int NB_LINES = 3;

    private final EnumMap<KeyCode, Boolean> keysInput;
    private final ObservableList<StowableObjectType>[][] value; // Tableau 2D qui correspond aux lignes de l'inventaire et ensuite le nombre de cases par ligene

    private final IntegerProperty posCursorHorizontallyInventoryBar;
    private final IntegerProperty posCursorVerticallyInventoryBar;
    private int scroll;


    public Inventory(Player player)
    {
        keysInput = new EnumMap<>(KeyCode.class);
        this.value = new ObservableList[NB_LINES][NB_BOXES_MAX / NB_LINES];
        for (int i = 0; i < NB_LINES; i++)
            for (int j = 0; j < NB_BOXES_MAX /NB_LINES; j++)
                this.value[i][j] = FXCollections.observableArrayList();

        this.posCursorHorizontallyInventoryBar = new SimpleIntegerProperty(0);
        this.posCursorVerticallyInventoryBar = new SimpleIntegerProperty(0);

        // Change l'item de la main du joueur
        posCursorHorizontallyInventoryBarProperty().addListener((obs, oldV, newV) -> {
            if (newV.intValue() >= 0 && newV.intValue() < (NB_BOXES_MAX /NB_LINES)) {
                ObservableList<StowableObjectType> stack = value[getPosCursorVerticallyInventoryBar()][newV.intValue()];
                player.setItemSelected((!stack.isEmpty()) ? stack.get(0) : null);
            }
        });
    }

    private int nbStacksIntoInventory()
    {
        int counter = 0;
        for (int i = 0; i < NB_LINES; i++)
            for (int j = 0; j < NB_BOXES_MAX /NB_LINES; j++)
                counter += (!this.value[i][j].isEmpty() && !Objects.isNull(this.value[i][j].get(0))) ? 1 : 0;

        return counter;
    }

    public void put(StowableObjectType object)
    {
        int nbStacksInventory = nbStacksIntoInventory();

        if (nbStacksInventory < NB_BOXES_MAX) {
            for (int i = 0; i < NB_LINES; i++) {
                boolean isFull = false;
                for (int j = 0; j < NB_BOXES_MAX / NB_LINES; j++) {
                    int beforeSize = this.value[i][j].size();
                    if (this.value[i][j].isEmpty())
                        this.value[i][j].add(object);
                    else if (this.value[i][j].size() == BLOCK_STACKING_MAX)
                        isFull = true;
                    else if (this.value[i][j].get(0) instanceof Dirt && object instanceof Dirt)
                        this.value[i][j].add(object);
                    else if (this.value[i][j].get(0) instanceof Stone && object instanceof Stone)
                        this.value[i][j].add(object);
                    else if (this.value[i][j].get(0) instanceof Torch && object instanceof Torch)
                        this.value[i][j].add(object);
                    else if (this.value[i][j].get(0) instanceof Fiber && object instanceof Fiber)
                        this.value[i][j].add(object);
                    else if (this.value[i][j].get(0) instanceof Wood && object instanceof Wood)
                        this.value[i][j].add(object);

                    // Quand un objet a été mise dans l'inventaire, il arrête la fonction
                    if (beforeSize != this.value[i][j].size())
                        return;

                    if (isFull)
                        this.value[i][nbStacksInventory].add(object);
                }
            }
        }
    }

    /** Les évènements du clavier qui sont liée à l'inventaire. */
    public void eventInput()
    {
        boolean scrollUp = scroll > 0;
        boolean scrollDown = scroll < 0;

        if (scrollUp)
            posCursorHorizontallyInventoryBar.set(getPosCursorHorizontallyInventoryBar() + 1);
        else if (scrollDown)
            posCursorHorizontallyInventoryBar.set(getPosCursorHorizontallyInventoryBar() - 1);

        if (getPosCursorHorizontallyInventoryBar() > (NB_BOXES_MAX / NB_LINES)-1)
            posCursorHorizontallyInventoryBar.set(0);
        else if (getPosCursorHorizontallyInventoryBar() < 0)
            posCursorHorizontallyInventoryBar.set((NB_BOXES_MAX / NB_LINES)-1);


        keysInput.forEach((key, value) -> {
            if (value.equals(Boolean.TRUE)) {
                if (key.equals(KeyCode.DIGIT1))
                    posCursorHorizontallyInventoryBar.set(0);
                else if (key.equals(KeyCode.DIGIT2))
                    posCursorHorizontallyInventoryBar.set(1);
                else if (key.equals(KeyCode.DIGIT3))
                    posCursorHorizontallyInventoryBar.set(2);
                else if (key.equals(KeyCode.DIGIT4))
                    posCursorHorizontallyInventoryBar.set(3);
                else if (key.equals(KeyCode.DIGIT5))
                    posCursorHorizontallyInventoryBar.set(4);
                else if (key.equals(KeyCode.DIGIT6))
                    posCursorHorizontallyInventoryBar.set(5);
                else if (key.equals(KeyCode.DIGIT7))
                    posCursorHorizontallyInventoryBar.set(6);
                else if (key.equals(KeyCode.DIGIT8))
                    posCursorHorizontallyInventoryBar.set(7);
                else if (key.equals(KeyCode.DIGIT9))
                    posCursorHorizontallyInventoryBar.set(8);
            }
        });

        this.scroll = 0;
    }


    public IntegerProperty posCursorHorizontallyInventoryBarProperty() { return posCursorHorizontallyInventoryBar; }
    public IntegerProperty posCursorVerticallyInventoryBarProperty() { return posCursorVerticallyInventoryBar; }
    public void setScroll(int newScroll) { this.scroll = newScroll; }

    public ObservableList<StowableObjectType>[][] get() { return value; }
    public EnumMap<KeyCode, Boolean> getKeysInput() { return keysInput; }
    public int getPosCursorHorizontallyInventoryBar() { return posCursorHorizontallyInventoryBar.get(); }
    public int getPosCursorVerticallyInventoryBar() { return posCursorVerticallyInventoryBar.get(); }
}
