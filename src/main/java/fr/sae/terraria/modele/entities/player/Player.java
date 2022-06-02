package fr.sae.terraria.modele.entities.player;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyCode;

import java.util.EnumMap;
import java.util.Map;


public class Player extends Entity implements CollideObjectType, MovableObjectType
{
    public static final int BREAK_BLOCK_DISTANCE = 1;

    private final EnumMap<KeyCode, Boolean> keysInput;

    private final ObjectProperty objectWasPickup;

    private StowableObjectType itemSelected;

    private Environment environment;

    private Inventory inventory;


    /**
     * @param x La position du joueur en X
     * @param y La position du joueur en Y
     */
    public Player(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;
        this.inventory = new Inventory(this);

        this.animation = new Animation();
        this.keysInput = new EnumMap<>(KeyCode.class);

        this.objectWasPickup = new SimpleObjectProperty(null);
        this.objectWasPickup.addListener((obs, oldObject, newObject) -> this.inventory.put((StowableObjectType) newObject));
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
            }
        });
    }

    public void pickup(StowableObjectType pickupObject) { objectWasPickup.set(pickupObject); }


    public Inventory getInventory() { return this.inventory; }
    public Map<KeyCode, Boolean> getKeysInput() { return keysInput; }
    public StowableObjectType getItemSelected() { return itemSelected; }

    public void setItemSelected(StowableObjectType itemSelected) { this.itemSelected = itemSelected; }
}
