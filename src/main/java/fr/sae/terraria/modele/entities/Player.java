package fr.sae.terraria.modele.entities;

import javafx.scene.input.KeyCode;

import java.util.*;

import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.Animation;
import fr.sae.terraria.modele.blocks.Dirt;
import fr.sae.terraria.modele.blocks.Stone;


public class Player extends Entity
{
    private final EnumMap<KeyCode, Boolean> keysInput;
    private final Map<Integer, ArrayList<Entity>> inventory;

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
            this.inventory.put(i, new ArrayList<>());
        }
    }

    public void updates()
    {
        // Applique les déplacements selon les valeurs de l'offset
        // this.setX(this.x.get() + this.offset[0] * this.velocity);

        if (this.offset[1] == 0) {
            this.gravity.xInit = this.x.get();
            this.gravity.yInit = this.y.get();
            this.gravity.vInit = this.velocity;

            this.gravity.timer = .0;
        }

        if (this.air)
            this.gravity.formulaOfTrajectory();
        if (this.offset[1] == 0)
            this.air = false;

        if (this.offset[1] == -1)
            this.setY(this.getY() + 2);
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

    public int nombreObjetsDansInventaire() { return this.inventory.size(); }

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

    public Map<Integer, ArrayList<Entity>> getInventory() { return inventory; }
    public Map<KeyCode, Boolean> getKeysInput() { return keysInput; }
}
