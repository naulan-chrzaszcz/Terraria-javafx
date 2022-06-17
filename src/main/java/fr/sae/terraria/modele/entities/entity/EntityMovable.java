package fr.sae.terraria.modele.entities.entity;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.TileSet;

import java.util.HashMap;
import java.util.Map;


public abstract class EntityMovable extends Entity
{
    protected final Environment environment;
    protected final Gravity gravity;

    public final int[] offset;

    protected double velocity;
    protected boolean air;


    protected EntityMovable(final Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;

        this.gravity = new Gravity();
        this.offset = new int[2];
        this.air = false;
        this.velocity = 1;
    }

    protected EntityMovable(final Environment environment) { this(environment, 0, 0); }

    @Override public abstract void updates();
    public abstract void move();

    /**
     * Permet de detectéer les collisions avec son environnement.
     *
     * @param environment Permet de savoir les tailles des tiles et d'avoir la matrixe de données de la carte.
     * @return Elle va retourner une HashMap qui ne contiendra pas de clé ou 4 clés maximum
     *         left, right, top, bottom seront les clés qui peuvent être presentes lors d'une detection de collision.
     *         Elle permettra de faire des actions personnalisées selon l'endroit où l'entité rentre en collision.
     */
    public Map<String, Boolean> collide(final Environment environment)
    {
        int widthTile = environment.widthTile; int heightTile = environment.heightTile;
        TileMaps tileMaps = environment.getTileMaps();
        Map<String, Boolean> whereCollide = new HashMap<>();

        // Detection vide en dessous
        int yBottom = (int)  (this.getY()+getRect().getHeight()-CollideObjectType.COLLISION_TOLERANCE)/heightTile;
        int posX = (int) ((this.getX()+((this.offset[0] < 0) ? CollideObjectType.COLLISION_TOLERANCE : -CollideObjectType.COLLISION_TOLERANCE)) + ((this.offset[0] > 0) ? getRect().getWidth() : 0))/widthTile;

        boolean footInTheVoid = tileMaps.getTile(posX, yBottom) == TileSet.SKY.ordinal();
        if (footInTheVoid)
            this.air = true;

        // Detection collision gauche droite
        if (this.isMoving()) {
            int yTop = (int) (getY()+CollideObjectType.COLLISION_TOLERANCE)/heightTile;
            int futurPosXLeft = (int) ((getX()+CollideObjectType.COLLISION_TOLERANCE)+(velocity*offset[0]))/widthTile;
            int futurPosXRight = (int) ((getX()+(-CollideObjectType.COLLISION_TOLERANCE)+(velocity*offset[0])) + (getRect().getWidth()))/widthTile;

            whereCollide.put("right", !tileMaps.isSkyTile(futurPosXRight, yTop) || !tileMaps.isSkyTile(futurPosXRight, yBottom));
            whereCollide.put("left", !tileMaps.isSkyTile(futurPosXLeft, yTop) || !tileMaps.isSkyTile(futurPosXLeft, yBottom));
        }

        // Detection collision bas et haut
        if (this.offset[1] != Entity.IDLE) {
            int xLeft = (int) (getX()+CollideObjectType.COLLISION_TOLERANCE)/widthTile;
            int xRight = (int) ((getX()+getRect().getWidth())-CollideObjectType.COLLISION_TOLERANCE)/widthTile;

            // Tombe
            if (this.isFalling()) {
                this.gravity.degInit = 0;
                double futurPosY = gravity.formulaOfTrajectory() ;

                boolean isCollideBottom = !tileMaps.isSkyTile(xLeft, (futurPosY + this.rect.getHeight())/heightTile) || !tileMaps.isSkyTile(xRight, (futurPosY + CollideObjectType.COLLISION_TOLERANCE +this.rect.getHeight())/heightTile);
                if (isCollideBottom) {
                    this.gravity.setJumpPosInit(this);
                    this.gravity.timer = 0;
                    this.idleOnY();
                } else setY(futurPosY);
                // Saute
            } else if (this.isJumping()) {
                double futurPosY = gravity.formulaOfTrajectory();

                boolean isCollideTop = !tileMaps.isSkyTile(xLeft, (futurPosY + CollideObjectType.COLLISION_TOLERANCE)/heightTile) || !tileMaps.isSkyTile(xRight, (futurPosY + CollideObjectType.COLLISION_TOLERANCE)/heightTile);
                // Quand le joueur monte
                if (this.gravity.flightTime >= this.gravity.timer) {
                    if (isCollideTop) {
                        this.fall();
                        this.gravity.timer = 0;
                        this.gravity.setJumpPosInit(this);
                    } else this.setY(futurPosY);
                    // Quand le joueur descends
                } else {
                    boolean isCollideBottom = !tileMaps.isSkyTile(xLeft, ((futurPosY + this.rect.getHeight()) - CollideObjectType.COLLISION_TOLERANCE)/heightTile) || !tileMaps.isSkyTile(xRight, ((futurPosY + this.rect.getHeight()) - CollideObjectType.COLLISION_TOLERANCE)/heightTile);

                    if (isCollideTop) {
                        this.fall();
                    } else if (isCollideBottom) {
                        this.gravity.setJumpPosInit(this);
                        this.gravity.timer = 0;
                        this.idleOnY();
                        this.air = false;
                    } else this.setY(futurPosY);
                }
            }
        } else if (this.air) {
            this.gravity.degInit = 0;
            int xLeft = (int) (getX()+CollideObjectType.COLLISION_TOLERANCE)/widthTile;
            int xRight = (int) (getX()-CollideObjectType.COLLISION_TOLERANCE+getRect().getWidth())/widthTile;
            double futurPosY = this.gravity.formulaOfTrajectory() + this.rect.getHeight();

            boolean isCollideBottom = !tileMaps.isSkyTile(xLeft, (futurPosY - CollideObjectType.COLLISION_TOLERANCE)/heightTile) || !tileMaps.isSkyTile(xRight, (futurPosY - CollideObjectType.COLLISION_TOLERANCE)/heightTile);
            if (isCollideBottom) {
                this.idleOnY();
                this.air = false;
                this.gravity.setJumpPosInit(this);
            } else setY(futurPosY - this.rect.getHeight());
        }

        return whereCollide;
    }

    public void moveRight() { this.offset[0] = Entity.IS_MOVING_RIGHT; }
    public void moveLeft() { this.offset[0] = Entity.IS_MOVING_LEFT; }
    public void jump() { this.offset[1] = Entity.IS_JUMPING; }
    public void fall() { this.offset[1] = Entity.IS_FALLING; }
    public void idleOnX() { this.offset[0] = Entity.IDLE; }
    public void idleOnY() { this.offset[1] = Entity.IDLE; }

    public boolean isMovingRight() { return this.offset[0] == Entity.IS_MOVING_RIGHT; }
    public boolean isMovingLeft() { return this.offset[0] == Entity.IS_MOVING_LEFT; }
    public boolean isMoving() { return this.offset[0] != Entity.IDLE; }
    public boolean isJumping() { return this.offset[1] == Entity.IS_JUMPING; }
    public boolean isFalling() { return this.offset[1] == Entity.IS_FALLING; }
    public boolean isNotFalling() { return this.offset[1] != Entity.IS_FALLING; }
    /** Quand l'entité ne bouge plus sur l'axes des X */
    public boolean isIDLEonX() { return this.offset[0] == Entity.IDLE; }
    /** Quand l'entité ne bouge plus sur l'axes des Y */
    public boolean isIDLEonY() { return this.offset[1] == Entity.IDLE; }

    /**
     * Lorsque le joueur sort de l'ecran et/ou de la carte, la fonction retourne une valeur boolean qui sera manipulable sur les classes qui heriteront EntityMovable.
     * @param environment Permet d'avoir les informations nécessaires sur la carte et l'écran pour que l'entité ne sorte pas.
     *
     * @return false = ne sort pas | true = sort de l'écran soit vers la droite ou la gauche
     */
    public boolean worldLimit(final Environment environment)
    {
        double widthMap = (environment.getTileMaps().getWidth()*environment.scaleMultiplicatorWidth*TileMaps.TILE_DEFAULT_SIZE) ;

        boolean exceedsScreenOnLeft = this.isMovingLeft() && this.getX() < 0;
        boolean exceedsScreenOnRight = this.isMovingRight() && this.getX()+CollideObjectType.COLLISION_TOLERANCE+this.velocity + this.getRect().getWidth() + 3 >= widthMap;
        return (exceedsScreenOnLeft || exceedsScreenOnRight);
    }


    public double getVelocity() { return this.velocity; }
    public Gravity getGravity() { return this.gravity; }
    public int getOffsetMoveX() { return this.offset[0]; }
    public int getOffsetMoveY() { return this.offset[1]; }

    public void setVelocity(double velocity) { this.velocity = velocity; }
}
