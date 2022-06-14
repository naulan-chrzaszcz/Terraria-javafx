package fr.sae.terraria.modele.entities.player;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.entity.*;
import fr.sae.terraria.modele.entities.player.inventory.Inventory;
import fr.sae.terraria.modele.entities.player.inventory.Stack;
import fr.sae.terraria.vue.View;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;


public class Player extends EntityMovable implements CollideObjectType, CollapsibleObjectType, SpawnableObjectType
{
    public static final int BREAK_BLOCK_DISTANCE = 1;

    private final EnumMap<KeyCode, Boolean> keysInput;
    private final EnumMap<MouseButton, Boolean> mouseInput;

    private final SimpleBooleanProperty drunk;

    private final Inventory inventory;
    private Stack stackSelected;


    public Player(final Environment environment)
    {
        super(environment, 0, 0);

        this.drunk = new SimpleBooleanProperty(false);
        this.inventory = new Inventory(this);

        this.animation = new Animation();
        this.keysInput = new EnumMap<>(KeyCode.class);
        this.mouseInput = new EnumMap<>(MouseButton.class);
    }

    @Override public void updates()
    {
        // Applique les déplacements selon les valeurs de l'offset
        // this.setX(this.x.get() + this.offset[0] * this.velocity);

        if (this.isIDLEonY() && !air) {
            this.gravity.xInit = this.x.get();
            this.gravity.yInit = this.y.get();
            this.gravity.vInit = this.velocity;
            this.gravity.degInit =  -90;

            this.gravity.timer = .0;
        }

        this.idleOnX();
        this.eventInput();
        this.collide();
        this.worldLimit();
        this.move();

        if (!Objects.isNull(this.rect))
            this.rect.updates(x.get(), y.get());
        this.animation.loop();
    }

    @Override public void move() { this.setX(this.getX() + this.getOffsetMoveX() * this.getVelocity()); }

    @Override public void collide()
    {
        Map<String, Boolean> whereCollide = super.collide(this.environment);

        if (!whereCollide.isEmpty()) {
            if (whereCollide.get("left").equals(Boolean.TRUE) || whereCollide.get("right").equals(Boolean.TRUE))
                this.idleOnX();
        }
    }

    @Override public void hit()
    {
        this.setPv(this.getPv() - 1);
    }

    @Override public void spawn(int x, int y)
    {
        this.setX(x);
        this.setY(y);

        Image img = View.loadAnImage("sprites/player/player_idle.png", environment.scaleMultiplicatorWidth, environment.scaleMultiplicatorHeight);
        if (!Objects.isNull(img)) {
            this.setRect((int) img.getWidth(), (int) img.getHeight());
            img.cancel();
        } else this.setRect(10, 10);

        this.getGravity().setXInit(x);
        this.getGravity().setYInit(y);
    }

    public void worldLimit()
    {
        if (super.worldLimit(this.environment))
            this.idleOnX();
    }

    public void interactWithBlock(final Rectangle2D rectangle)
    {
        int i = 0;

        Entity entity = this.environment.getEntities().get(i);
        while (!entity.getRect().collideRect(rectangle) && i < this.environment.getEntities().size() ) {
            entity = this.environment.getEntities().get(i);
            i++;
        }

        if (entity.getRect().collideRect(rectangle)) {
            if (entity instanceof BreakableObjectType)
                ((BreakableObjectType) entity).breaks();
            if (entity instanceof CollapsibleObjectType)
                ((CollapsibleObjectType) entity).hit();
        }
    }

    public void placeBlock(int xBlock, int yBlock)
    {
        TileMaps tileMaps = this.environment.getTileMaps();
        boolean haveAnItemOnHand = !Objects.isNull(this.getStackSelected());
        boolean goodPlace = tileMaps.isSkyTile(xBlock, yBlock);

        if (haveAnItemOnHand && goodPlace) {
            this.inventory.refreshStack();
            if (!(this.getStackSelected().getItem() instanceof PlaceableObjectType) && !(this.getStackSelected().getItem() instanceof ConsumableObjectType))
                return;

            if (this.getStackSelected().getItem() instanceof PlaceableObjectType)
                ((PlaceableObjectType) this.getStackSelected().getItem()).place(xBlock, yBlock);
        }
    }

    /** Lie les inputs du clavier à une ou des actions. */
    public void eventInput()
    {
        this.inventory.eventInput();

        this.keysInput.forEach((key, value) -> {
            if (Boolean.TRUE.equals(value)) {
                if (key == KeyCode.Z || key == KeyCode.SPACE)
                    if (this.isNotFalling()) this.jump();

                if (key == KeyCode.D)
                    this.moveRight();
                else if (key == KeyCode.Q)
                    this.moveLeft();
            }
        });
    }

    public void pickup(StowableObjectType pickupObject) { this.inventory.put(pickupObject); }

    public SimpleBooleanProperty drunkProperty() { return drunk; }


    public Map<MouseButton, Boolean> getMouseInput() { return this.mouseInput; }
    public Map<KeyCode, Boolean> getKeysInput() { return this.keysInput; }
    public Stack getStackSelected() { return this.stackSelected; }
    public Inventory getInventory() { return this.inventory; }

    public void setStackSelected(Stack stackSelected) { this.stackSelected = stackSelected; }
}
