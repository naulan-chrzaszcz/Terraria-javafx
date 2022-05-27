package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.CollideObjectType;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.entity.Animation;
import fr.sae.terraria.modele.entities.entity.Entity;


public class Rabbit extends Entity implements CollideObjectType
{
    private Environment environment;
    private Animation animation;


    public Rabbit(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;

        this.animation = new Animation();
        this.velocity = 1;
        this.offset[0] = 1;
    }

    public void updates()
    {
        this.setX(this.x.get() + this.offset[0] * this.velocity);

        if (this.offset[1] == 0 && !air) {
            this.gravity.xInit = this.x.get();
            this.gravity.yInit = this.y.get();
            this.gravity.vInit = this.velocity;
            this.gravity.degInit =  -90;

            this.gravity.timer = .0;
        }

        this.animation.loop();
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

            if (pTopLeft || pBotLeft)
                offset[0] = 1;
            if (pTopRight || pBotRight)
                offset[0] = -1;
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


    public Animation getAnimation() { return animation; }
}




