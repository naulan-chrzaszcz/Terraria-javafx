package fr.sae.terraria.modele.entities;

import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;

import fr.sae.terraria.modele.entities.entity.Entity;


public class Player extends Entity
{
    private final Map<KeyCode, Boolean> keysInput;

    public boolean air;


    /**
     * @param x La position du joueur en X
     * @param y La position du joueur en Y
     */
    public Player(int x, int y)
    {
        super(x, y);

        this.keysInput = new HashMap<>();
    }

    public void updates()
    {
        super.updates();

        // Applique les déplacements selon les valeurs de l'offset
        // this.setX(this.x.get() + this.offset[0] * this.velocity);

        if (this.offset[1] == 0) {
            this.gravity.xInit = this.x.get();
            this.gravity.yInit = this.y.get();
            this.gravity.vInit = this.velocity;
            this.gravity.flightTime = 1;

            this.gravity.timer = .0;
        }



        if (this.offset[1] == -1)
            this.setY(this.getY() + 2);
        this.setX(this.getX() + this.offset[0] * this.getVelocity());
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


    public Map<KeyCode, Boolean> getKeysInput() { return keysInput; }

}
