package fr.sae.terraria.modele.entities.entity;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.HashMap;
import java.util.Map;


public abstract class Entity
{
    public static final int IS_FALLING = -1;
    public static final int IS_JUMPING = 1;
    public static final int IS_MOVING_LEFT = -1;
    public static final int IS_MOVING_RIGHT = 1;
    public static final int IDLE = 0;

    protected DoubleProperty pv;
    protected DoubleProperty x;
    protected DoubleProperty y;

    protected final Gravity gravity = new Gravity();
    protected Animation animation = null;
    protected Rect rect = null;

    protected double velocity = 1;
    protected double pvMax;
    public int[] offset = new int[2];
    public boolean air = false;


    /**
     * @param x La localisation de l'entité a l'horizontal
     * @param y La localisation de l'entité a la verticale
     */
    protected Entity(int x, int y)
    {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.pv = new SimpleDoubleProperty(0);
        this.pvMax = pv.get();
    }


    /**
     * Permet de mettre à jour les valeurs qui concerne l'entité
     *   et qui doit rester au sain de l'objet
     */
    public abstract void updates();

    protected Map<String, Boolean> collide(Environment environment)
    {
        int widthTile = environment.widthTile; int heightTile = environment.heightTile;
        TileMaps tileMaps = environment.getTileMaps();
        Map<String, Boolean> whereCollide = new HashMap<>();

        // Detection vide en dessous
        int yBottom = (int)  (getY()+getRect().getHeight()-CollideObjectType.COLLISION_TOLERANCE)/heightTile;
        int posX = (int) ((getX()+((offset[0] < 0) ? CollideObjectType.COLLISION_TOLERANCE : -CollideObjectType.COLLISION_TOLERANCE)) + ((offset[0] > 0) ? getRect().getWidth() : 0))/widthTile;

        boolean footInTheVoid = tileMaps.getTile(posX, yBottom+1) == TileMaps.SKY;
        if (footInTheVoid)
            this.air = true;

        // Detection collision gauche droite
        if (this.offset[0] != Entity.IDLE) {
            int yTop = (int) (getY()+CollideObjectType.COLLISION_TOLERANCE)/heightTile;
            int futurePositionXLeft = (int) ((getX()+CollideObjectType.COLLISION_TOLERANCE)+(velocity*offset[0]))/widthTile;
            int futurePositionXRight = (int) ((getX()+(-CollideObjectType.COLLISION_TOLERANCE)+(velocity*offset[0])) + (getRect().getWidth()))/widthTile;

            whereCollide.put("right", tileMaps.getTile(futurePositionXRight, yTop) != TileMaps.SKY || tileMaps.getTile(futurePositionXRight, yBottom) != TileMaps.SKY);
            whereCollide.put("left", tileMaps.getTile(futurePositionXLeft, yTop) != TileMaps.SKY || tileMaps.getTile(futurePositionXLeft, yBottom) != TileMaps.SKY);
        }

        // Detection collision en bas et en haut
        if (this.offset[1] != Entity.IDLE) {
            int xLeft = (int) (getX()+CollideObjectType.COLLISION_TOLERANCE)/widthTile;
            int xRight = (int) ((getX()+getRect().getWidth())-CollideObjectType.COLLISION_TOLERANCE)/widthTile;

            // Tombe
            if (this.offset[1] == Entity.IS_FALLING) {
                this.gravity.degInit = 0;
                double futurePositionY = gravity.formulaOfTrajectory() + rect.getHeight();

                boolean isCollideBottom = tileMaps.getTile(xLeft, (int) (futurePositionY + CollideObjectType.COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY || tileMaps.getTile(xRight, (int) (futurePositionY + CollideObjectType.COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY;
                if (isCollideBottom) {
                    this.setJumpPosInit();
                    this.gravity.timer = 0;
                    this.offset[1] = Entity.IDLE;
                } else setY(futurePositionY);
                // Saute
            } else if (this.offset[1] == Entity.IS_JUMPING) {
                double futurePositionY = gravity.formulaOfTrajectory();
                this.air = true;

                boolean isCollideTop = tileMaps.getTile(xLeft, (int) (futurePositionY + CollideObjectType.COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY || tileMaps.getTile(xRight, (int) (futurePositionY + CollideObjectType.COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY;

                // Quand le joueur monte
                if ((gravity.flightTime/2) >= gravity.timer) {
                    if (isCollideTop) {
                        this.fall();
                    } else setY(futurePositionY);
                    // Quand le joueur decent
                } else {
                    boolean isCollideBottom = tileMaps.getTile(xLeft, (int) ((futurePositionY + rect.getHeight()) - CollideObjectType.COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY || tileMaps.getTile(xRight,(int) ((futurePositionY + rect.getHeight()) - CollideObjectType.COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY;

                    if (isCollideTop) {
                        this.fall();
                    } else if (isCollideBottom) {
                        this.setJumpPosInit();
                        this.gravity.timer = 0;
                        this.offset[1] = Entity.IDLE;
                        this.air = false;
                    } else setY(futurePositionY);
                }
            }
        } else if (this.air) {
            this.gravity.degInit = 0;
            int xLeft = (int) (getX()+CollideObjectType.COLLISION_TOLERANCE)/widthTile;
            int xRight = (int) (getX()-CollideObjectType.COLLISION_TOLERANCE+getRect().getWidth())/widthTile;
            double futurePositionY = this.gravity.formulaOfTrajectory() + this.rect.getHeight();

            boolean isCollideBottom = tileMaps.getTile(xLeft, (int) (futurePositionY - CollideObjectType.COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY || tileMaps.getTile(xRight, (int) (futurePositionY - CollideObjectType.COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY;
            if (isCollideBottom) {
                this.offset[1] = Entity.IDLE;
                this.air = false;
                this.setJumpPosInit();
            } else setY(futurePositionY - this.rect.getHeight());
        }

        return whereCollide;
    }

    /** Modifie le xInit et le yInit pour modifier le point de départ du saut ou de là où il tombe */
    public void setJumpPosInit() { this.gravity.xInit = this.x.get(); this.gravity.yInit = this.y.get(); }


    /** Modifie l'offset qui permet de le déplacer vers la droite */
    protected void moveRight() { offset[0] = Entity.IS_MOVING_RIGHT; }
    /** Modifie l'offset qui permet de le déplacer vers la gauche */
    protected void moveLeft() { offset[0] = Entity.IS_MOVING_LEFT; }
    /** Modifie l'offset qui permet de le faire sauter */
    protected void jump() { offset[1] = Entity.IS_JUMPING; }
    /** Modifie l'offset qui permet de tomber */
    protected void fall() { offset[1] = Entity.IS_FALLING; }

    public DoubleProperty getPvProperty() { return this.pv; }
    public DoubleProperty getXProperty() { return this.x; }
    public DoubleProperty getYProperty(){ return this.y; }
    public Animation getAnimation() { return this.animation; }
    public Gravity getGravity() { return this.gravity; }
    public Rect getRect() { return this.rect; }
    public double getPvMax() { return this.pv.get(); }
    public double getPv() { return this.pv.get(); }
    public double getX() { return this.x.get(); }
    public double getY() { return this.y.get(); }
    public double getVelocity() { return this.velocity; }

    public void setPv(double pv) { this.pv.setValue(pv); this.pvMax = pv;}
    public void setX(double x) { this.x.setValue(x); }
    public void setY(double y) { this.y.setValue(y); }
    public void setVelocity(double velocity) { this.velocity = velocity; }
    public void setRect(int width, int height) { this.rect = new Rect(x.get(), y.get(), width, height); }
}


