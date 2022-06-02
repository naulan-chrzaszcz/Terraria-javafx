package fr.sae.terraria.modele.entities.entity;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.HashMap;
import java.util.Map;


/**
 * <h1><b>La classes Entity</b></h1>
 * <h2><u>Description:</u></h2>
 *
 * <p>Correspond à un element physique qui doit avoir des coordonnées sur un environment.</p>
 * <p>contient des fonctions privates qui peuvent êtres implémenter grâce au interface mise à disposition au sain du même package</p>
 * <p></p>
 * <p>C'est interfaces agissent comme une sorte de pâte à modelé, ils permettent surtout de
 * rendre public les fonctions de Entity qui sont eux protected</p>
 *
 * <h2><u>Comment l'utiliser ?</u></h2>
 * <p>Il suffit d'extend des objects qui vous souhaitez êtres des entitées</p>
 * <p><u>Exemple de code qui hérite de Entity:</u></p>
 * <img src="https://raw.githubusercontent.com/NaulaN/SAE-Terraria-Like/develop/src/main/resources/fr/sae/terraria/docs/ExampleExtendEntity.PNG"/>
 * <p>Donc sur cette exemple dans le jeu, le lapin est considèrer comme une entités.</p>
 * <p></p>
 * <p>Toutes les méthodes/fonctions protected sont appelable vers l'extérieur d'un classes qui l'hérite grâce à des interfaces.</p>
 * <p>Donc pour avoir correctement les fonctions:</p>
 *
 * <p>Sont implementable grâce à l'interface MovableObjectType</p>
 * @see CollideObjectType
 * @see MovableObjectType
 * @see ReproductiveObjectType
 * @see StowableObjectType
 *
 * @author CHRZASZCZ Naulan
 */
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
    protected double velocity;
    protected double pvMax;
    protected boolean air;

    public int[] offset;


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
        this.air = false;
        this.offset = new int[2];
        this.velocity = 1;
    }


    /**
     * Il permet à chaque passage de la boucle du jeu, de faire diverses fonctions liée à l'Entité.
     *  Certaine fonction qui sera implementé grâce à des interfaces sera probablement obligatoirement mise dans cette fonction.
     */
    public abstract void updates();

    /**
     * Permet de detectés les collisions sur la carte et de son environment.
     *
     * @param environment Permet de savoir les tailles des tiles et d'avoir la matrixe de données de la carte.
     * @return Il vas retourner une HashMap qui ne contiendra pas de clé ou 4 clés maximum
     *         left, right, top, bottom sera les clés qui peuvent être present lors d'une detection de collision.
     *         Il permettra de faire des actions personnalisées les actions faites par l'entité suivant d'où il rentre en collision avec son environment.
     */
    protected Map<String, Boolean> collide(Environment environment)
    {
        int widthTile = environment.widthTile; int heightTile = environment.heightTile;
        TileMaps tileMaps = environment.getTileMaps();
        Map<String, Boolean> whereCollide = new HashMap<>();

        // Detection vide en dessous
        int yBottom = (int)  (getY()+getRect().getHeight()-CollideObjectType.COLLISION_TOLERANCE)/heightTile;
        int posX = (int) ((getX()+((offset[0] < 0) ? CollideObjectType.COLLISION_TOLERANCE : -CollideObjectType.COLLISION_TOLERANCE)) + ((offset[0] > 0) ? getRect().getWidth() : 0))/widthTile;

        boolean footInTheVoid = tileMaps.getTile(posX, yBottom) == TileMaps.SKY;
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
                double futurePositionY = gravity.formulaOfTrajectory() ;
                boolean isCollideBottom = tileMaps.getTile(xLeft, (int) (futurePositionY + this.rect.getHeight())/heightTile) != TileMaps.SKY || tileMaps.getTile(xRight, (int) (futurePositionY + CollideObjectType.COLLISION_TOLERANCE +this.rect.getHeight())/heightTile) != TileMaps.SKY;
                if (isCollideBottom) {
                    this.gravity.setJumpPosInit(this);
                    this.gravity.timer = 0;
                    this.offset[1] = Entity.IDLE;
                } else setY(futurePositionY);
                // Saute
            } else if (this.offset[1] == Entity.IS_JUMPING) {
                double futurePositionY = gravity.formulaOfTrajectory();
                this.air = true;

                boolean isCollideTop = tileMaps.getTile(xLeft, (int) (futurePositionY + CollideObjectType.COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY || tileMaps.getTile(xRight, (int) (futurePositionY + CollideObjectType.COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY;

                // Quand le joueur monte
                if ((gravity.flightTime) >= gravity.timer && this.offset[1] == Entity.IS_JUMPING) {
                    if (isCollideTop) {
                        this.fall();
                        gravity.timer = 0;
                        gravity.setJumpPosInit(this);
                    } else setY(futurePositionY);
                    // Quand le joueur decent
                } else if (this.offset[1] == Entity.IS_JUMPING) {
                    boolean isCollideBottom = tileMaps.getTile(xLeft, (int) ((futurePositionY + rect.getHeight()) - CollideObjectType.COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY || tileMaps.getTile(xRight,(int) ((futurePositionY + rect.getHeight()) - CollideObjectType.COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY;

                    if (isCollideTop) {
                        this.fall();
                    } else if (isCollideBottom) {
                        this.gravity.setJumpPosInit(this);
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
                this.gravity.setJumpPosInit(this);
            } else setY(futurePositionY - this.rect.getHeight());
        }

        return whereCollide;
    }

    protected void moveRight() { this.offset[0] = Entity.IS_MOVING_RIGHT; }
    protected void moveLeft() { this.offset[0] = Entity.IS_MOVING_LEFT; }
    protected void jump() { this.offset[1] = Entity.IS_JUMPING; }
    protected void fall() { this.offset[1] = Entity.IS_FALLING; }

    /**
     * Lorsque le joueur sort de l'ecran et/ou de la carte, la fonction retourne une valeurs boolean qui sera manipulable sur les classes qui l'héritera.
     * @param environment Permet d'avoir les informations naicessaire sur la carte et l'écran pour que l'entité ne sorte pas.
     *
     * @return false = ne sort pas | true = sort de l'écran soit vers la droite ou soit vers le bas
     */
    protected boolean worldLimit(Environment environment)
    {
        double widthScreen = (environment.scaleMultiplicatorWidth * Terraria.DISPLAY_RENDERING_WIDTH);

        boolean exceedsScreenOnLeft = offset[0] == Entity.IS_MOVING_LEFT && getX() < 0;
        boolean exceedsScreenOnRight = offset[0] == Entity.IS_MOVING_RIGHT && getX() > (widthScreen - getRect().getWidth());
        return (exceedsScreenOnLeft || exceedsScreenOnRight);
    }


    public DoubleProperty getPvProperty() { return this.pv; }
    public DoubleProperty getXProperty() { return this.x; }
    public DoubleProperty getYProperty(){ return this.y; }
    public Animation getAnimation() { return this.animation; }
    public Gravity getGravity() { return this.gravity; }
    public Rect getRect() { return this.rect; }
    public double getVelocity() { return this.velocity; }
    public double getPvMax() { return this.pv.get(); }
    public double getPv() { return this.pv.get(); }
    public double getX() { return this.x.get(); }
    public double getY() { return this.y.get(); }

    public void setRect(int width, int height) { this.rect = new Rect(x.get(), y.get(), width, height); }
    public void setVelocity(double velocity) { this.velocity = velocity; }
    public void setPv(double pv) { this.pv.setValue(pv); this.pvMax = pv;}
    public void setX(double x) { this.x.setValue(x); }
    public void setY(double y) { this.y.setValue(y); }
}


