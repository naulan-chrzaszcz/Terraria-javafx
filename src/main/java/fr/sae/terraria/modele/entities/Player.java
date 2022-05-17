package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.entities.entity.Entity;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.Q;


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
        this.eventInput();

        // Applique les déplacements selon les valeurs de l'offset
        this.setX(this.x.get() + this.offset[0] * this.velocity);
        // this.setY(this.y.get() + this.offset[1] * this.velocity);

        if (offset[1] >= 0) {
            this.gravity.formulaOfTrajectory(this);
        } else this.setY(getY() + 2);
        if (offset[1] == 0)
            air = false;

        this.rect.update(x.get(), y.get());
    }

    @Override
    public void jump(){
        if (!air)
            super.jump();
        air = true;
    }

    /** Lie les inputs au clavier à une ou des actions. */
    private void eventInput()
    {
        this.keysInput.forEach((key, value) -> {
            if ((key == Z || key == SPACE) && Boolean.TRUE.equals(value))
                this.jump();

            if (key == D && Boolean.TRUE.equals(value))
                this.moveRight();
            if (key == Q && Boolean.TRUE.equals(value))
                this.moveLeft();
        });
    }


    public Map<KeyCode, Boolean> getKeysInput() { return keysInput; }
}
