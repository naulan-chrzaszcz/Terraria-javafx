package fr.sae.terraria.modele.entities.player;

import fr.sae.terraria.modele.entities.blocks.Dirt;
import fr.sae.terraria.modele.entities.blocks.Stone;
import fr.sae.terraria.modele.entities.blocks.Torch;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

import java.util.Objects;


public class Inventory
{
    public static final int BLOCK_STACKING_MAX = 16;
    public static final int NB_CASES_MAX = 27;
    public static final int NB_LINES = 3;

    private final ObservableList<StowableObjectType>[][] value; // Tableau 2D qui correspond aux lignes de l'inventaire et ensuite le nombre de cases par ligene

    private final IntegerProperty posCursorHorizontallyInventoryBar;
    private final IntegerProperty posCursorVerticallyInventoryBar;


    public Inventory(Player player)
    {
        this.value = new ObservableList[NB_LINES][NB_CASES_MAX / NB_LINES];
        for (int i = 0; i < NB_LINES; i++)
            for (int j = 0; j < NB_CASES_MAX/NB_LINES; j++)
                this.value[i][j] = FXCollections.observableArrayList();

        this.posCursorHorizontallyInventoryBar = new SimpleIntegerProperty(0);
        this.posCursorVerticallyInventoryBar = new SimpleIntegerProperty(0);
        posCursorHorizontallyInventoryBarProperty().addListener((obs, oldV, newV) -> {
            if (newV.intValue() >= 0 && newV.intValue() < (NB_CASES_MAX /NB_LINES)) {
                ObservableList<StowableObjectType> stack = value[getPosCursorVerticallyInventoryBar()][newV.intValue()];
                player.setItemSelected((!stack.isEmpty()) ? stack.get(0) : null);
            }
        });
    }

    public int nbStacksIntoInventory()
    {
        int counter = 0;
        for (int i = 0; i < NB_LINES; i++)
            for (int j = 0; j < NB_CASES_MAX/NB_LINES; j++)
                counter += (!this.value[i][j].isEmpty() && !Objects.isNull(this.value[i][j].get(0))) ? 1 : 0;

        return counter;
    }

    public void put(StowableObjectType object)
    {
        int nbStacksInventory = nbStacksIntoInventory();

        if (nbStacksInventory < NB_CASES_MAX) {
            for (int i = 0; i < NB_LINES; i++) {
                boolean isFull = false;
                for (int j = 0; j < NB_CASES_MAX / NB_LINES; j++) {
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

                    // Quand un objet a été mise dans l'inventaire, il arrête la fonction
                    if (beforeSize != this.value[i][j].size())
                        return;

                    if (isFull)
                        this.value[i][nbStacksInventory].add(object);
                }
            }
        }
    }

    public void eventFilter(Stage stage)
    {
        stage.addEventFilter(ScrollEvent.SCROLL, scroll -> {
            boolean scrollUp = scroll.getDeltaY() > 0;
            boolean scrollDown = scroll.getDeltaY() < 0;

            if (scrollUp)
                posCursorHorizontallyInventoryBar.set(getPosCursorHorizontallyInventoryBar() + 1);
            if (scrollDown)
                posCursorHorizontallyInventoryBar.set(getPosCursorHorizontallyInventoryBar() - 1);

            if (getPosCursorHorizontallyInventoryBar() > (NB_CASES_MAX / NB_LINES)-1)
                posCursorHorizontallyInventoryBar.set(0);
            if (getPosCursorHorizontallyInventoryBar() < 0)
                posCursorHorizontallyInventoryBar.set((NB_CASES_MAX / NB_LINES)-1);
        });

        stage.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode().equals(KeyCode.DIGIT1))
                posCursorHorizontallyInventoryBar.set(0);
            else if (key.getCode().equals(KeyCode.DIGIT2))
                posCursorHorizontallyInventoryBar.set(1);
            else if (key.getCode().equals(KeyCode.DIGIT3))
                posCursorHorizontallyInventoryBar.set(2);
            else if (key.getCode().equals(KeyCode.DIGIT4))
                posCursorHorizontallyInventoryBar.set(3);
            else if (key.getCode().equals(KeyCode.DIGIT5))
                posCursorHorizontallyInventoryBar.set(4);
            else if (key.getCode().equals(KeyCode.DIGIT6))
                posCursorHorizontallyInventoryBar.set(5);
            else if (key.getCode().equals(KeyCode.DIGIT7))
                posCursorHorizontallyInventoryBar.set(6);
            else if (key.getCode().equals(KeyCode.DIGIT8))
                posCursorHorizontallyInventoryBar.set(7);
            else if (key.getCode().equals(KeyCode.DIGIT9))
                posCursorHorizontallyInventoryBar.set(8);
        });
    }


    public IntegerProperty posCursorHorizontallyInventoryBarProperty() { return posCursorHorizontallyInventoryBar; }
    public IntegerProperty posCursorVerticallyInventoryBarProperty() { return posCursorVerticallyInventoryBar; }

    public ObservableList<StowableObjectType>[][] get() { return value; }
    public int getPosCursorHorizontallyInventoryBar() { return posCursorHorizontallyInventoryBar.get(); }
    public int getPosCursorVerticallyInventoryBar() { return posCursorVerticallyInventoryBar.get(); }
}
