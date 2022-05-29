package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.CollideObjectType;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.StowableObjectType;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.blocks.Dirt;
import fr.sae.terraria.modele.blocks.Stone;
import fr.sae.terraria.modele.entities.entity.Animation;
import fr.sae.terraria.modele.entities.entity.Entity;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
        int widthTile = environment.widthTile; int heightTile = environment.heightTile;
        TileMaps tileMaps = environment.getTileMaps();

        // Detection vide en dessous
        int yBottom = (int)  (getY()+getRect().getHeight()-COLLISION_TOLERANCE)/heightTile;
        int posX = (int) ((getX()+((offset[0] < 0) ? COLLISION_TOLERANCE : -COLLISION_TOLERANCE)) + ((offset[0] > 0) ? getRect().getWidth() : 0))/widthTile;
        if (tileMaps.getTile(posX, yBottom+1) == TileMaps.SKY)
            this.air = true;

        // Detection collision gauche droite
        if (this.offset[0] != 0) {
            int yTop = (int) (getY()+COLLISION_TOLERANCE)/heightTile;
            int futurePositionX = (int) ((getX()+((offset[0] < 0) ? COLLISION_TOLERANCE : -COLLISION_TOLERANCE)+(velocity*offset[0])) + ((offset[0] > 0) ? getRect().getWidth() : 0))/widthTile;

            if (tileMaps.getTile(futurePositionX, yTop) != TileMaps.SKY ||
                    tileMaps.getTile(futurePositionX, yBottom) != TileMaps.SKY )
                this.offset[0] = 0;
        }

        // Detection collision en bas et en haut
        if (this.offset[1] != 0) {
            int xLeft = (int) (getX()+COLLISION_TOLERANCE)/widthTile;
            int xRight = (int) ((getX()+getRect().getWidth())-COLLISION_TOLERANCE)/widthTile;

            // Tombe
            if (this.offset[1] == -1) {
                this.gravity.degInit = 0;
                double futurePositionY = gravity.formulaOfTrajectory() + rect.getHeight();

                if (tileMaps.getTile(xLeft, (int) (futurePositionY + COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY ||
                        tileMaps.getTile(xRight, (int) (futurePositionY + COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY) {
                    this.gravity.xInit = this.x.get();
                    this.gravity.yInit = this.y.get();
                    this.gravity.timer = 0;
                    this.offset[1] = 0;
                } else setY(futurePositionY);
            // Saute
            } else if (this.offset[1] == 1) {
                double futurePositionY = gravity.formulaOfTrajectory();
                this.air = true;

                // Quand le joueur monte
                if ((gravity.flightTime/2) >= gravity.timer) {
                    if (tileMaps.getTile(xLeft, (int) (futurePositionY + COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY ||
                            tileMaps.getTile(xRight, (int) (futurePositionY + COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY) {
                        this.fall();
                    } else setY(futurePositionY);
                // Quand le joueur decent
                } else {
                    if (tileMaps.getTile(xLeft, (int) (futurePositionY + COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY ||
                            tileMaps.getTile(xRight, (int) (futurePositionY + COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY) {
                        this.fall();
                    } else if (tileMaps.getTile(xLeft, (int) ((futurePositionY + rect.getHeight()) - COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY ||
                            tileMaps.getTile(xRight,(int) ((futurePositionY + rect.getHeight()) - COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY) {
                        this.gravity.xInit = this.x.get();
                        this.gravity.yInit = this.y.get();
                        this.gravity.timer = 0;
                        this.offset[1] = 0;
                        this.air = false;
                    } else setY(futurePositionY);
                }
            }
        } else if (this.air) {
            this.gravity.degInit = 0;
            int xLeft = (int) (getX()+COLLISION_TOLERANCE)/widthTile;
            int xRight = (int) (getX()-COLLISION_TOLERANCE+getRect().getWidth())/widthTile;
            double futurePositionY = this.gravity.formulaOfTrajectory() + this.rect.getHeight();

            if (tileMaps.getTile(xLeft, (int) (futurePositionY - COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY ||
                    tileMaps.getTile(xRight, (int) (futurePositionY - COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY) {
                this.gravity.xInit = this.x.get();
                this.gravity.yInit = this.y.get();
                this.offset[1] = 0;
                this.air = false;
            } else setY(futurePositionY - this.rect.getHeight());
        }
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
