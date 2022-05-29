package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.CollideObjectType;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.entity.Animation;
import fr.sae.terraria.modele.entities.entity.Entity;


public class Rabbit extends Entity implements CollideObjectType
{
    public static final double LUCK_OF_JUMPING = .5;
    public static final int JUMP_FREQUENCY = 50;

    private Environment environment;
    private Animation animation;


    public Rabbit(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;

        this.animation = new Animation();
        this.velocity = 1;
        this.offset[0] = (Math.random() <= .5) ? Entity.IS_MOVING_RIGHT : Entity.IS_MOVING_LEFT;
        this.gravity.amplitude = 40;
    }

    public Rabbit(Environment environment) { this(environment, 0, 0); }

    public void updates()
    {
        if (this.offset[1] == Entity.IDLE && !air) {
            this.gravity.xInit = this.x.get();
            this.gravity.yInit = this.y.get();
            this.gravity.vInit = this.velocity;
            this.gravity.degInit =  -90;

            this.gravity.timer = .0;
        }

        this.setX(this.x.get() + this.offset[0] * this.velocity);

        if (this.rect != null)
            this.rect.update(x.get(), y.get());
        this.animation.loop();
    }

    public void collide()
    {
        int widthTile = environment.widthTile; int heightTile = environment.heightTile;
        TileMaps tileMaps = environment.getTileMaps();

        // Detection vide en dessous
        int yBottom = (int)  (getY()+getRect().getHeight()-COLLISION_TOLERANCE)/heightTile;
        int posX = (int) ((getX()+((offset[0] < 0) ? COLLISION_TOLERANCE : -COLLISION_TOLERANCE)) + ((offset[0] > 0) ? getRect().getWidth() : 0))/widthTile;

        boolean footInTheVoid = tileMaps.getTile(posX, yBottom+1) == TileMaps.SKY;
        if (footInTheVoid)
            this.air = true;

        // Detection collision gauche droite
        if (this.offset[0] != Entity.IDLE) {
            int yTop = (int) (getY()+COLLISION_TOLERANCE)/heightTile;
            int futurePositionX = (int) ((getX()+((offset[0] < 0) ? COLLISION_TOLERANCE : -COLLISION_TOLERANCE)+(velocity*offset[0])) + ((offset[0] > 0) ? getRect().getWidth() : 0))/widthTile;

            // Il gère les deux car futurePositionX change dynamiquement suivant l'offset
            boolean isCollideLeftOrRight = tileMaps.getTile(futurePositionX, yTop) != TileMaps.SKY || tileMaps.getTile(futurePositionX, yBottom) != TileMaps.SKY;
            if (isCollideLeftOrRight)
                this.offset[0] = (-1) * this.offset[0];
        }

        // Detection collision en bas et en haut
        if (this.offset[1] != Entity.IDLE) {
            int xLeft = (int) (getX()+COLLISION_TOLERANCE)/widthTile;
            int xRight = (int) ((getX()+getRect().getWidth())-COLLISION_TOLERANCE)/widthTile;

            // Tombe
            if (this.offset[1] == Entity.IS_FALLING) {
                this.gravity.degInit = 0;
                double futurePositionY = gravity.formulaOfTrajectory() + rect.getHeight();

                boolean isCollideBottom = tileMaps.getTile(xLeft, (int) (futurePositionY + COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY || tileMaps.getTile(xRight, (int) (futurePositionY + COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY;
                if (isCollideBottom) {
                    this.setJumpPosInit();
                    this.gravity.timer = 0;
                    this.offset[1] = Entity.IDLE;
                } else setY(futurePositionY);
                // Saute
            } else if (this.offset[1] == Entity.IS_JUMPING) {
                double futurePositionY = gravity.formulaOfTrajectory();
                this.air = true;

                boolean isCollideTop = tileMaps.getTile(xLeft, (int) (futurePositionY + COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY || tileMaps.getTile(xRight, (int) (futurePositionY + COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY;

                // Quand le joueur monte
                if ((gravity.flightTime/2) >= gravity.timer) {
                    if (isCollideTop) {
                        this.fall();
                    } else setY(futurePositionY);
                    // Quand le joueur decent
                } else {
                    boolean isCollideBottom = tileMaps.getTile(xLeft, (int) ((futurePositionY + rect.getHeight()) - COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY || tileMaps.getTile(xRight,(int) ((futurePositionY + rect.getHeight()) - COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY;

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
            int xLeft = (int) (getX()+COLLISION_TOLERANCE)/widthTile;
            int xRight = (int) (getX()-COLLISION_TOLERANCE+getRect().getWidth())/widthTile;
            double futurePositionY = this.gravity.formulaOfTrajectory() + this.rect.getHeight();

            boolean isCollideBottom = tileMaps.getTile(xLeft, (int) (futurePositionY - COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY || tileMaps.getTile(xRight, (int) (futurePositionY - COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY;
            if (isCollideBottom) {
                this.setJumpPosInit();
                this.offset[1] = Entity.IDLE;
                this.air = false;
            } else setY(futurePositionY - this.rect.getHeight());
        }

        // TODO: Ce n'est pas parfait
        if (environment.getPlayer().getRect().collideRect(rect))
            offset[0] = (-1) * offset[0];
    }


    public Animation getAnimation() { return animation; }
}




