package fr.sae.terraria.modele.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.input.KeyCode;

import java.util.*;

import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.Animation;
import fr.sae.terraria.modele.blocks.Dirt;
import fr.sae.terraria.modele.blocks.Stone;


public class Player extends Entity
{
    public static final int BREAK_BLOCK_DISTANCE = 1;
    private static final int BLOCK_STACKING_MAX = 16;
    private static final int NB_CASES_MAX_INVENTORY = 27;

    private final Map<Integer, ObservableList<Entity>> inventory;
    private final EnumMap<KeyCode, Boolean> keysInput;

    private Entity itemSelected;

    public IntegerProperty positionOfCursorInventoryBar;

    public boolean air;


    /**
     * @param x La position du joueur en X
     * @param y La position du joueur en Y
     */
    public Player(int x, int y)
    {
        super(x, y);

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

        if (this.offset[1] == 0 && !air) {
            this.gravity.xInit = this.x.get();
            this.gravity.yInit = this.y.get();
            this.gravity.vInit = this.velocity;
            this.gravity.degInit =  -90;

            this.gravity.timer = .0;
        }

        this.setX(this.getX() + this.offset[0] * this.getVelocity());

        if (this.rect != null)
            this.rect.update(x.get(), y.get());
        animation.loop();
    }

    public void jump()
    {
        if (!this.air)
            super.jump();
        this.air = true;
    }

    /** Lie les inputs au clavier à une ou des actions. */
    public void eventInput()
    {
        this.keysInput.forEach((key, value) -> {
            if ((key == KeyCode.Z || key == KeyCode.SPACE) && Boolean.TRUE.equals(value))
                this.jump();

            if (key == KeyCode.D && Boolean.TRUE.equals(value))
                this.moveRight();
            if (key == KeyCode.Q && Boolean.TRUE.equals(value))
                this.moveLeft();

            if (key == KeyCode.DIGIT1 && Boolean.TRUE.equals(value))
                this.positionOfCursorInventoryBar.set(0);
            else if (key == KeyCode.DIGIT2 && Boolean.TRUE.equals(value))
                this.positionOfCursorInventoryBar.set(1);
            else if (key == KeyCode.DIGIT3 && Boolean.TRUE.equals(value))
                this.positionOfCursorInventoryBar.set(2);
            else if (key == KeyCode.DIGIT4 && Boolean.TRUE.equals(value))
                this.positionOfCursorInventoryBar.set(3);
            else if (key == KeyCode.DIGIT5 && Boolean.TRUE.equals(value))
                this.positionOfCursorInventoryBar.set(4);
            else if (key == KeyCode.DIGIT6 && Boolean.TRUE.equals(value))
                this.positionOfCursorInventoryBar.set(5);
            else if (key == KeyCode.DIGIT7 && Boolean.TRUE.equals(value))
                this.positionOfCursorInventoryBar.set(6);
            else if (key == KeyCode.DIGIT8 && Boolean.TRUE.equals(value))
                this.positionOfCursorInventoryBar.set(7);
            else if (key == KeyCode.DIGIT9 && Boolean.TRUE.equals(value))
                this.positionOfCursorInventoryBar.set(8);
        });
    }

    public int nbStacksIntoInventory()
    {
        int counter = 0;
        for (int i = 0; i < inventory.size(); i++)
            counter += (!this.inventory.get(i).isEmpty() && this.inventory.get(i).get(0) != null) ? 1 : 0;

        return counter;
    }

    public void pickup(Entity pickupObject)
    {
        int nbStacksInventory = nbStacksIntoInventory();
        boolean estComplet = false;

        if (nbStacksInventory < NB_CASES_MAX_INVENTORY) {
            int i = 0;
            while (i < this.inventory.size()) {
                int beforeSize = this.inventory.get(i).size();
                if (this.inventory.get(i).isEmpty())
                    this.inventory.get(i).add(pickupObject);
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

    public Map<Integer, ObservableList<Entity>> getInventory() { return inventory; }
    public Map<KeyCode, Boolean> getKeysInput() { return keysInput; }
    public Entity getItemSelected() { return itemSelected; }
    public void setItemSelected(Entity itemSelected) { this.itemSelected = itemSelected; }
}
