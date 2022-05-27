package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.blocks.Dirt;
import fr.sae.terraria.modele.blocks.Stone;
import fr.sae.terraria.modele.entities.entity.Animation;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.vue.View;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


public class Player extends Entity implements CollideObjectType
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

    public boolean air;


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

    public void collide()
    {
        TileMaps tileMaps = environment.getTileMaps();
        int widthTile = environment.widthTile;
        int heightTile = environment.heightTile;

        if (offset[0] != 0 || offset[1] != 0) {
            boolean pTopLeft  = tileMaps.getTile((int) (getX()+COLLISION_TOLERANCE+offset[0]*velocity)/widthTile, (int) (getY()+COLLISION_TOLERANCE)/heightTile) != 0;
            boolean pTopRight = tileMaps.getTile((int) (getX()-COLLISION_TOLERANCE+rect.getWidth()+velocity*offset[0])/widthTile, (int) (getY()+COLLISION_TOLERANCE)/heightTile) != 0;
            boolean pBotLeft  = tileMaps.getTile((int) (getX()+COLLISION_TOLERANCE+velocity*offset[0])/widthTile, (int) (getY()+rect.getHeight()-COLLISION_TOLERANCE)/heightTile) != 0;
            boolean pBotRight = tileMaps.getTile((int) (getX()-COLLISION_TOLERANCE+rect.getWidth()+velocity*offset[0])/widthTile , (int) (getY()+rect.getHeight()-COLLISION_TOLERANCE)/heightTile) != 0;

            if (pTopLeft|| pTopRight  || pBotLeft  || pBotRight)
                offset[0] = 0;
        }

        if (air) {
            double futurePosition = gravity.formulaOfTrajectory();

            if (gravity.timer < gravity.flightTime){
                boolean pTopLeft  = tileMaps.getTile((int) ((getX()+COLLISION_TOLERANCE)/widthTile), (int)(futurePosition/heightTile)) == 0;
                boolean pTopRight = tileMaps.getTile((int) (((getX()-COLLISION_TOLERANCE)+widthTile)/widthTile), (int)(futurePosition/heightTile)) == 0;

                if ( pTopLeft && pTopRight ) {
                    setY(futurePosition);
                    offset[1] = 1;
                } else { gravity.setFall(getY()); offset[1] = 1; }
            } else {
                boolean pBotLeft = tileMaps.getTile((int) ((getX()+COLLISION_TOLERANCE)/widthTile), (int) ((futurePosition+rect.getHeight()+COLLISION_TOLERANCE)/heightTile)) == 0;
                boolean pBotRight = tileMaps.getTile((int) ((getX()-COLLISION_TOLERANCE+rect.getWidth())/widthTile), (int) ((futurePosition+rect.getHeight()+COLLISION_TOLERANCE)/heightTile)) == 0;

                if (pBotLeft && pBotRight) {
                    setY(futurePosition);
                    offset[1] = 1;
                }
            }
        }

        if (tileMaps.getTile((int) ((getX() + COLLISION_TOLERANCE) / widthTile), (int) (getY()+rect.getHeight()+COLLISION_TOLERANCE) / heightTile) == 0 && tileMaps.getTile((int) ((getX() + rect.getWidth() - COLLISION_TOLERANCE) / widthTile), (int) (getY() + rect.getHeight() + COLLISION_TOLERANCE) / heightTile) == 0 && !air)
            fall();
        if (offset[1] == 0)
            air = false;

        if (offset[1] == -1) {
            gravity.degInit = 0;
            double futurY = gravity.formulaOfTrajectory();
            boolean pBotLeft = tileMaps.getTile((int) ((getX() + COLLISION_TOLERANCE) / widthTile), (int) ((futurY + rect.getHeight() + COLLISION_TOLERANCE) / heightTile)) == 0;
            boolean pBotRight = tileMaps.getTile((int) ((getX() - COLLISION_TOLERANCE + rect.getWidth()) / widthTile), (int) ((futurY + rect.getHeight() + COLLISION_TOLERANCE) / heightTile)) == 0;

            if (pBotLeft && pBotRight) {
                setY(futurY);
                offset[1] = -1;
            }  else offset[1] = 0;
            // TODO : PROX OFFSET 0 RESET YINIT
        }
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

    public void pickup(StowableObjectType pickupObject)
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

    public Map<Integer, ObservableList<StowableObjectType>> getInventory() { return inventory; }
    public Map<KeyCode, Boolean> getKeysInput() { return keysInput; }
    public StowableObjectType getItemSelected() { return itemSelected; }
    public void setItemSelected(StowableObjectType itemSelected) { this.itemSelected = itemSelected; }
}
