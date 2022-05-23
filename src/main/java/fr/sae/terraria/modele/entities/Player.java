package fr.sae.terraria.modele.entities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;

import java.util.*;

import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.Animation;
import fr.sae.terraria.modele.blocks.Dirt;
import fr.sae.terraria.modele.blocks.Stone;


public class Player extends Entity
{
    private final EnumMap<KeyCode, Boolean> keysInput;
    private final Map<Integer, ObservableList<Entity>> inventory;

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
        for (int i =0; i<26; i++){
            this.inventory.put(i, FXCollections.observableArrayList());
        }
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

    public int nombreObjetsDansInventaire()
        {
            int compteur = 0;
            for (int i = 0; i< inventory.size(); i++) {
                if (this.inventory.get(i).size() != 0) {
                    if (this.inventory.get(i).get(0) != null)
                        compteur++;
                }
            }
            return compteur;
        }

    public void ramasser(Entity objetRamassé)
    {
        if (nombreObjetsDansInventaire() < 27){
            for(int integer =0 ; integer < inventory.size(); integer++){
                if (this.inventory.get(integer).size() == 0) {
                    this.inventory.get(integer).add(objetRamassé);
                    break;
                }
                else if (this.inventory.get(integer).get(0) instanceof Dirt && objetRamassé instanceof Dirt){
                    this.inventory.get(integer).add(objetRamassé);
                    System.out.println("babajei");
                    break;
                }
                else if (this.inventory.get(integer).get(0) instanceof Stone && objetRamassé instanceof Stone){
                    this.inventory.get(integer).add(objetRamassé);;
                    System.out.println("ekesekes");
                    break;
                }
            }
        }
    }

    public Map<Integer, ObservableList<Entity>> getInventory() { return inventory; }
    public Map<KeyCode, Boolean> getKeysInput() { return keysInput; }
}
