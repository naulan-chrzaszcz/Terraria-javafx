package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.blocks.Dirt;
import fr.sae.terraria.modele.entities.blocks.Stone;
import fr.sae.terraria.modele.entities.entity.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


public class Player extends Entity implements CollideObjectType, MovableObjectType
{
    public static final int BREAK_BLOCK_DISTANCE = 1;
    public static final int BLOCK_STACKING_MAX = 16;
    public static final int NB_CASES_MAX_INVENTORY = 27;
    public static final int NB_LINES_INVENTORY = 3;

    private final Map<Integer, ObservableList<StowableObjectType>> inventory;
    private final EnumMap<KeyCode, Boolean> keysInput;

    public IntegerProperty positionOfCursorInventoryBar;

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
        this.inventory = new HashMap<>();
        this.positionOfCursorInventoryBar = new SimpleIntegerProperty(0);

        for (int i = 0; i < NB_CASES_MAX_INVENTORY; i++)
            this.inventory.put(i, FXCollections.observableArrayList());
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
                this.offset[0] = 0;
        }
    }
    public void moveRight() { super.moveRight(); }
    public void moveLeft() { super.moveLeft(); }
    public void jump() { super.jump(); }
    public void fall() { super.fall(); }

    public void worldLimit()
    {
        if (super.worldLimit(environment))
            offset[0] = 0;
    }

    /** Lie les inputs au clavier à une ou des actions. */
    public void eventInput()
    {
        this.keysInput.forEach((key, value) -> {
            if (Boolean.TRUE.equals(value)) {
                if (key == KeyCode.Z || key == KeyCode.SPACE)
                    if (this.offset[1] != -1) this.jump();

                if (key == KeyCode.D)
                    this.moveRight();
                else if (key == KeyCode.Q)
                    this.moveLeft();

                if (key == KeyCode.DIGIT1)
                    this.positionOfCursorInventoryBar.set(0);
                else if (key == KeyCode.DIGIT2)
                    this.positionOfCursorInventoryBar.set(1);
                else if (key == KeyCode.DIGIT3)
                    this.positionOfCursorInventoryBar.set(2);
                else if (key == KeyCode.DIGIT4)
                    this.positionOfCursorInventoryBar.set(3);
                else if (key == KeyCode.DIGIT5)
                    this.positionOfCursorInventoryBar.set(4);
                else if (key == KeyCode.DIGIT6)
                    this.positionOfCursorInventoryBar.set(5);
                else if (key == KeyCode.DIGIT7)
                    this.positionOfCursorInventoryBar.set(6);
                else if (key == KeyCode.DIGIT8)
                    this.positionOfCursorInventoryBar.set(7);
                else if (key == KeyCode.DIGIT9)
                    this.positionOfCursorInventoryBar.set(8);
            }
        });
    }

    public int nbStacksIntoInventory()
    {
        int counter = 0;
        for (int i = 0; i < inventory.size(); i++)
            counter += (!this.inventory.get(i).isEmpty() && this.inventory.get(i).get(0) != null) ? 1 : 0;

        return counter;
    }

    public void pickup(StowableObjectType pickupObject)
    {
        int nbStacksInventory = nbStacksIntoInventory();
        boolean estComplet = false;

        if (nbStacksInventory < NB_CASES_MAX_INVENTORY) {
            int i = 0;
            while (i < this.inventory.size()) {
                int beforeSize = this.inventory.get(i).size();
                if (this.inventory.get(i).isEmpty()) {
                    this.inventory.get(i).add(pickupObject);
                    this.setItemSelected(pickupObject);
                }
                else if (this.inventory.get(i).size() == BLOCK_STACKING_MAX)
                    estComplet = true;
                else if (this.inventory.get(i).get(0) instanceof Dirt && pickupObject instanceof Dirt) {
                    this.inventory.get(i).add(pickupObject);
                    estComplet = false;
                } else if (this.inventory.get(i).get(0) instanceof Stone && pickupObject instanceof Stone) {
                    this.inventory.get(i).add(pickupObject);
                    estComplet = false;
                }

                // Quand un objet a étais mise dans l'inventaire, il arrête la fonction
                if (beforeSize != this.inventory.get(i).size()) return;
                i++;
            }

            if (estComplet)
                this.inventory.get(nbStacksInventory).add(pickupObject);
        }
    }


    public Map<Integer, ObservableList<StowableObjectType>> getInventory() { return inventory; }
    public Map<KeyCode, Boolean> getKeysInput() { return keysInput; }
    public StowableObjectType getItemSelected() { return itemSelected; }

    public void setItemSelected(StowableObjectType itemSelected) { this.itemSelected = itemSelected; }
}
