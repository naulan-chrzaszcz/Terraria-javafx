package fr.sae.terraria.modele.entities;

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
    }

    public void updates()
    {
        this.setX(this.x.get() + this.offset[0] * this.velocity);
        this.setY(this.y.get() + this.offset[1] * this.velocity);
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
            double futurePosition = GRAVITY.formulaOfTrajectory();

            if (GRAVITY.timer < GRAVITY.flightTime){
                boolean pTopLeft  = tileMaps.getTile((int) ((getX()+COLLISION_TOLERANCE)/widthTile), (int)(futurePosition/heightTile)) == 0;
                boolean pTopRight = tileMaps.getTile((int) (((getX()-COLLISION_TOLERANCE)+widthTile)/widthTile), (int)(futurePosition/heightTile)) == 0;

                if ( pTopLeft && pTopRight ) {
                    setY(futurePosition);
                    offset[1] = 1;
                } else { GRAVITY.setFall(getY()); offset[1] = 1; }
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
            GRAVITY.degInit = 0;
            double futurY = GRAVITY.formulaOfTrajectory();
            boolean pBotLeft = tileMaps.getTile((int) ((getX() + COLLISION_TOLERANCE) / widthTile), (int) ((futurY + rect.getHeight() + COLLISION_TOLERANCE) / heightTile)) == 0;
            boolean pBotRight = tileMaps.getTile((int) ((getX() - COLLISION_TOLERANCE + rect.getWidth()) / widthTile), (int) ((futurY + rect.getHeight() + COLLISION_TOLERANCE) / heightTile)) == 0;

            if (pBotLeft && pBotRight) {
                setY(futurY);
                offset[1] = -1;
            }  else offset[1] = 0;
            // TODO : PROX OFFSET 0 RESET YINIT
        }
    }
}




