package fr.sae.terraria.modele.entities.entity;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;

import java.util.HashMap;
import java.util.Map;


public abstract class EntityMovable extends Entity
{
    protected final Environment environment;
    protected final Gravity gravity;

    protected double velocity;
    protected boolean air;
    public int[] offset;


    protected EntityMovable(int x, int y, final Environment environment)
    {
        super(x, y);
        this.environment = environment;

        this.gravity = new Gravity();
        this.offset = new int[2];
        this.air = false;
        this.velocity = 1;
    }

    protected EntityMovable(final Environment environment) { this(0, 0, environment); }

    @Override public abstract void updates();
    public abstract void move();

    /**
     * Permet de detectés les collisions sur la carte et de son environment.
     *
     * @param environment Permet de savoir les tailles des tiles et d'avoir la matrixe de données de la carte.
     * @return Il vas retourner une HashMap qui ne contiendra pas de clé ou 4 clés maximum
     *         left, right, top, bottom sera les clés qui peuvent être present lors d'une detection de collision.
     *         Il permettra de faire des actions personnalisées les actions faites par l'entité suivant d'où il rentre en collision avec son environment.
     */
    protected Map<String, Boolean> collide(final Environment environment)
    {
        int widthTile = environment.widthTile; int heightTile = environment.heightTile;
        TileMaps tileMaps = environment.getTileMaps();
        Map<String, Boolean> whereCollide = new HashMap<>();

        // Detection vide en dessous
        int yBottom = (int)  (this.getY()+getRect().getHeight()-CollideObjectType.COLLISION_TOLERANCE)/heightTile;
        int posX = (int) ((this.getX()+((this.offset[0] < 0) ? CollideObjectType.COLLISION_TOLERANCE : -CollideObjectType.COLLISION_TOLERANCE)) + ((this.offset[0] > 0) ? getRect().getWidth() : 0))/widthTile;

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

                boolean isCollideTop = tileMaps.getTile(xLeft, (int) (futurePositionY + CollideObjectType.COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY || tileMaps.getTile(xRight, (int) (futurePositionY + CollideObjectType.COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY;
                // Quand le joueur monte
                if (this.gravity.flightTime >= this.gravity.timer ) {
                    if (isCollideTop) {
                        this.fall();
                        this.gravity.timer = 0;
                        this.gravity.setJumpPosInit(this);
                    } else this.setY(futurePositionY);
                    // Quand le joueur decent
                } else {
                    boolean isCollideBottom = tileMaps.getTile(xLeft, (int) ((futurePositionY + this.rect.getHeight()) - CollideObjectType.COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY || tileMaps.getTile(xRight,(int) ((futurePositionY + this.rect.getHeight()) - CollideObjectType.COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY;

                    if (isCollideTop) {
                        this.fall();
                    } else if (isCollideBottom) {
                        this.gravity.setJumpPosInit(this);
                        this.gravity.timer = 0;
                        this.offset[1] = Entity.IDLE;
                        this.air = false;
                    } else this.setY(futurePositionY);
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
    protected boolean worldLimit(final Environment environment)
    {
        double widthMap = (environment.getTileMaps().getWidth()*environment.scaleMultiplicatorWidth*TileMaps.TILE_DEFAULT_SIZE) ;

        boolean exceedsScreenOnLeft = offset[0] == Entity.IS_MOVING_LEFT && getX() < 0;
        boolean exceedsScreenOnRight = offset[0] == Entity.IS_MOVING_RIGHT && getX()+CollideObjectType.COLLISION_TOLERANCE+velocity + getRect().getWidth() + 3 >= widthMap;
        return (exceedsScreenOnLeft || exceedsScreenOnRight);
    }


    public double getVelocity() { return this.velocity; }
    public Gravity getGravity() { return this.gravity; }

    public void setVelocity(double velocity) { this.velocity = velocity; }
}
