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
        this.offset[0] = 1;
        this.gravity.amplitude = 40;
    }

    public Rabbit(Environment environment) { this(environment, 0, 0); }

    public void updates()
    {
        if (this.offset[1] == 0 && !air) {
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
        TileMaps tileMaps = environment.getTileMaps();
        int widthTile = environment.widthTile;
        int heightTile = environment.heightTile;

        // Detection vide en dessous
        int yBottom = (int)  (getY()+getRect().getHeight()-COLLISION_TOLERANCE)/heightTile;
        int posX = (int) ((getX()+((offset[0] < 0) ? COLLISION_TOLERANCE : -COLLISION_TOLERANCE)) + ((offset[0] > 0) ? getRect().getWidth() : 0))/widthTile;
        if (tileMaps.getTile(posX, yBottom+1) == TileMaps.SKY)
            air = true;

        // Detection collision gauche droite
        if (offset[0] != 0) {
            int yTop = (int) (getY()+COLLISION_TOLERANCE)/heightTile;
            int futurePositionX = (int) ((getX()+((offset[0] < 0) ? COLLISION_TOLERANCE : -COLLISION_TOLERANCE)+(velocity*offset[0])) + ((offset[0] > 0) ? getRect().getWidth() : 0))/widthTile;

            if (tileMaps.getTile(futurePositionX, yTop) != TileMaps.SKY ||
                    tileMaps.getTile(futurePositionX, yBottom) != TileMaps.SKY )
                offset[0] = (-1) * offset[0];
        }

        // Detection collision en bas et en haut
        if (offset[1] != 0) {
            int xLeft = (int) (getX()+COLLISION_TOLERANCE)/widthTile;
            int xRight = (int) (getX()-COLLISION_TOLERANCE+getRect().getWidth())/widthTile;

            // Tombe
            if (offset[1] == -1) {
                gravity.degInit = 0;
                double futurePositionY = gravity.formulaOfTrajectory() + rect.getHeight();

                if (tileMaps.getTile(xLeft, (int) (futurePositionY + COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY ||
                        tileMaps.getTile(xRight, (int) (futurePositionY + COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY) {
                    this.gravity.xInit = this.x.get();
                    this.gravity.yInit = this.y.get();
                    gravity.timer = 0;
                    offset[1] = 0;
                } else setY(futurePositionY);
                // Saute
            } else if (offset[1] == 1) {
                double futurePositionY = gravity.formulaOfTrajectory();

                // Quand le joueur monte
                if ((gravity.flightTime/2) >= gravity.timer) {
                    if (tileMaps.getTile(xLeft, (int) (futurePositionY + COLLISION_TOLERANCE) / heightTile) != TileMaps.SKY ||
                            tileMaps.getTile(xRight, (int) (futurePositionY + COLLISION_TOLERANCE) / heightTile) != TileMaps.SKY) {
                        this.gravity.xInit = this.x.get();
                        this.gravity.yInit = this.y.get();
                        gravity.timer = 0;
                        offset[1] = 0;
                    } else setY(futurePositionY);
                    // Quand le joueur decent
                } else {
                    if (tileMaps.getTile(xLeft, (int) (futurePositionY + COLLISION_TOLERANCE) / heightTile) != TileMaps.SKY ||
                            tileMaps.getTile(xRight, (int) (futurePositionY + COLLISION_TOLERANCE) / heightTile) != TileMaps.SKY ||
                            tileMaps.getTile(xLeft, (int) ((futurePositionY + rect.getHeight()) - COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY ||
                            tileMaps.getTile(xRight,(int) ((futurePositionY + rect.getHeight()) - COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY) {
                        this.gravity.xInit = this.x.get();
                        this.gravity.yInit = this.y.get();
                        gravity.timer = 0;
                        offset[1] = 0;
                    } else setY(futurePositionY);
                }
            }

            air = true;
        } else if (air) {
            gravity.degInit = 0;
            int xLeft = (int) (getX()+COLLISION_TOLERANCE)/widthTile;
            int xRight = (int) (getX()-COLLISION_TOLERANCE+getRect().getWidth())/widthTile;
            double futurePositionY = gravity.formulaOfTrajectory() + rect.getHeight();

            if (tileMaps.getTile(xLeft, (int) (futurePositionY - COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY ||
                    tileMaps.getTile(xRight, (int) (futurePositionY - COLLISION_TOLERANCE)/heightTile) != TileMaps.SKY) {
                this.gravity.xInit = this.x.get();
                this.gravity.yInit = this.y.get();
                offset[1] = 0;
                air = false;
            } else setY(futurePositionY - rect.getHeight());
        }

        if (environment.getPlayer().getRect().collideRect(rect))
            offset[0] = (-1) * offset[0];
    }


    public Animation getAnimation() { return animation; }
}




