package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.blocks.Dirt;
import fr.sae.terraria.modele.entities.blocks.Stone;
import fr.sae.terraria.modele.entities.blocks.Torch;
import fr.sae.terraria.modele.entities.entity.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;


public class Player extends Entity implements CollideObjectType, MovableObjectType
{
    public static final int BREAK_BLOCK_DISTANCE = 1;
    public static final int BLOCK_STACKING_MAX = 16;
    public static final int NB_CASES_MAX_INVENTORY = 27;
    public static final int NB_LINES_INVENTORY = 3;

    private final ObservableList<StowableObjectType>[][] inventory; // Tableau 2D qui correspond aux lignes de l'inventaire et ensuite le nombre de cases par ligene
    private final EnumMap<KeyCode, Boolean> keysInput;

    private final IntegerProperty posCursorHorizontallyInventoryBar;
    private final IntegerProperty posCursorVerticallyInventoryBar;

    private StowableObjectType itemSelected;

    private Environment environment;


    /**
     * @param x La position du joueur en X
     * @param y La position du joueur en Y
     */
    public Player(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;

        this.animation = new Animation();
        this.keysInput = new EnumMap<>(KeyCode.class);
        this.inventory = new ObservableList[NB_LINES_INVENTORY][NB_CASES_MAX_INVENTORY/NB_LINES_INVENTORY];
        this.posCursorHorizontallyInventoryBar = new SimpleIntegerProperty(0);
        this.posCursorVerticallyInventoryBar = new SimpleIntegerProperty(2);

        for (int i = 0; i < NB_LINES_INVENTORY; i++)
            for (int j = 0; j < NB_CASES_MAX_INVENTORY/NB_LINES_INVENTORY; j++)
                this.inventory[i][j] = FXCollections.observableArrayList();
    }

    public void updates()
    {
        // Applique les déplacements selon les valeurs de l'offset
        // this.setX(this.x.get() + this.offset[0] * this.velocity);

        if (this.offset[1] == Entity.IDLE && !air) {
            this.gravity.xInit = this.x.get();
            this.gravity.yInit = this.y.get();
            this.gravity.vInit = this.velocity;
            this.gravity.degInit =  -90;

            this.gravity.timer = .0;
        }

        this.worldLimit();
        this.move();

        if (this.rect != null)
            this.rect.updates(x.get(), y.get());
        animation.loop();
    }

    public void move() { this.setX(this.getX() + this.offset[0] * this.getVelocity()); }

    public void collide()
    {
        Map<String, Boolean> whereCollide = super.collide(this.environment);

        if (!whereCollide.isEmpty()) {
            if (whereCollide.get("left").equals(Boolean.TRUE) || whereCollide.get("right").equals(Boolean.TRUE))
                this.offset[0] = Entity.IDLE;
        }
    }

    public void moveRight() { super.moveRight(); }
    public void moveLeft() { super.moveLeft(); }
    public void jump() { super.jump(); }
    public void fall() { super.fall(); }

    public void worldLimit()
    {
        if (super.worldLimit(environment))
            offset[0] = Entity.IDLE;
    }

    /** Lie les inputs au clavier à une ou des actions. */
    public void eventInput()
    {
        this.keysInput.forEach((key, value) -> {
            if (Boolean.TRUE.equals(value)) {
                if (key == KeyCode.Z || key == KeyCode.SPACE)
                    if (this.offset[1] != Entity.IS_FALLING) this.jump();

                if (key == KeyCode.D)
                    this.moveRight();
                else if (key == KeyCode.Q)
                    this.moveLeft();

                if (key == KeyCode.DIGIT1)
                    this.posCursorHorizontallyInventoryBar.set(0);
                else if (key == KeyCode.DIGIT2)
                    this.posCursorHorizontallyInventoryBar.set(1);
                else if (key == KeyCode.DIGIT3)
                    this.posCursorHorizontallyInventoryBar.set(2);
                else if (key == KeyCode.DIGIT4)
                    this.posCursorHorizontallyInventoryBar.set(3);
                else if (key == KeyCode.DIGIT5)
                    this.posCursorHorizontallyInventoryBar.set(4);
                else if (key == KeyCode.DIGIT6)
                    this.posCursorHorizontallyInventoryBar.set(5);
                else if (key == KeyCode.DIGIT7)
                    this.posCursorHorizontallyInventoryBar.set(6);
                else if (key == KeyCode.DIGIT8)
                    this.posCursorHorizontallyInventoryBar.set(7);
                else if (key == KeyCode.DIGIT9)
                    this.posCursorHorizontallyInventoryBar.set(8);
            }
        });
    }

    public int nbStacksIntoInventory()
    {
        int counter = 0;
        for (int i = 0; i < NB_LINES_INVENTORY; i++)
            for (int j = 0; j < NB_CASES_MAX_INVENTORY/NB_LINES_INVENTORY; j++)
                counter += (!this.inventory[i][j].isEmpty() && !Objects.isNull(this.inventory[i][j].get(0))) ? 1 : 0;

        return counter;
    }

    public void pickup(StowableObjectType pickupObject)
    {
        int nbStacksInventory = nbStacksIntoInventory();

        if (nbStacksInventory < NB_CASES_MAX_INVENTORY) {
            for (int i = NB_LINES_INVENTORY-1; i >= 0; i--) {
                boolean isFull = false;
                int j = 0;
                while (j < NB_CASES_MAX_INVENTORY/NB_LINES_INVENTORY) {
                    int beforeSize = this.inventory[i][j].size();
                    if (this.inventory[i][j].isEmpty()) {
                        this.inventory[i][j].add(pickupObject);
                        this.setItemSelected(pickupObject);
                    } else if (this.inventory[i][j].size() == BLOCK_STACKING_MAX)
                        isFull = true;
                    else if (this.inventory[i][j].get(0) instanceof Dirt && pickupObject instanceof Dirt)
                        this.inventory[i][j].add(pickupObject);
                    else if (this.inventory[i][j].get(0) instanceof Stone && pickupObject instanceof Stone)
                        this.inventory[i][j].add(pickupObject);
                    else if (this.inventory[i][j].get(0) instanceof Torch && pickupObject instanceof Torch)
                        this.inventory[i][j].add(pickupObject);

                    // Quand un objet a été mise dans l'inventaire, il arrête la fonction
                    if (beforeSize != this.inventory[i][j].size())
                        return;
                    j++;
                }

                if (isFull)
                    this.inventory[i][nbStacksInventory].add(pickupObject);
            }
        }
    }


    public ObservableList<StowableObjectType>[][] getInventory() { return inventory; }
    public IntegerProperty getPosCursorVerticallyInventoryBarProperty() { return this.posCursorVerticallyInventoryBar; }
    public int getPosCursorVerticallyInventoryBar() { return this.posCursorVerticallyInventoryBar.get(); }
    public IntegerProperty getPosCursorHorizontallyInventoryBarProperty() { return this.posCursorHorizontallyInventoryBar; }
    public int getPosCursorHorizontallyInventoryBar() { return this.posCursorHorizontallyInventoryBar.get(); }
    public Map<KeyCode, Boolean> getKeysInput() { return keysInput; }
    public StowableObjectType getItemSelected() { return itemSelected; }

    public void setItemSelected(StowableObjectType itemSelected) { this.itemSelected = itemSelected; }
}
