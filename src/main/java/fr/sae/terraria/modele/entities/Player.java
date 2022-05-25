package fr.sae.terraria.modele.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;

import java.util.*;

import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.Animation;
import fr.sae.terraria.modele.blocks.Dirt;
import fr.sae.terraria.modele.blocks.Stone;
import javafx.scene.input.ScrollEvent;


public class Player extends Entity
{
    private static final int NB_CASES_MAX_INVENTORY = 27;

    private final EnumMap<KeyCode, Boolean> keysInput;
    private final Map<Integer, ObservableList<Entity>> inventory;

    private Entity itemSelected;

    public IntegerProperty deplacementScroll;
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
        this.deplacementScroll = new SimpleIntegerProperty(0);

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
        });
    }

    public int lengthOfInventoryFull()
    {
        int counter = 0;
        for (int i = 0; i < inventory.size(); i++)
            counter += (!this.inventory.get(i).isEmpty() && this.inventory.get(i).get(0) != null) ? 1 : 0;

        return counter;
    }

    public void ramasser(Entity pickupObject)
    {
        boolean estComplet = false;
        if (lengthOfInventoryFull() < NB_CASES_MAX_INVENTORY) {
            int i = 0;
            while (i < this.inventory.size()) {
                int beforeSize = this.inventory.get(i).size();
                if (this.inventory.get(i).isEmpty())
                    this.inventory.get(i).add(pickupObject);
                else if (this.inventory.get(i).size() == 16) {
                    estComplet = true;
                }
                else if (this.inventory.get(i).get(0) instanceof Dirt && pickupObject instanceof Dirt) {
                    this.inventory.get(i).add(pickupObject);
                    estComplet = false;
                }
                else if (this.inventory.get(i).get(0) instanceof Stone && pickupObject instanceof Stone)
                    this.inventory.get(i).add(pickupObject);
                    estComplet = false;

                if (beforeSize != this.inventory.get(i).size()) return;
                i++;
                if (estComplet)
                    this.inventory.get(lengthOfInventoryFull()).add(pickupObject);
            }
        }
    }

    public Map<Integer, ObservableList<Entity>> getInventory() { return inventory; }
    public Map<KeyCode, Boolean> getKeysInput() { return keysInput; }
    public Entity getItemSelected() { return itemSelected; }
    public void setItemSelected(Entity itemSelected) { this.itemSelected = itemSelected; }
}
