package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.GenerateEntity;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.entity.*;
import fr.sae.terraria.modele.entities.items.Meat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Rabbit extends EntityMovable implements CollideObjectType, ReproductiveObjectType, CollapsibleObjectType, SpawnableObjectType
{
    public static final int WHEN_SPAWN_A_RABBIT = 2_500;
    public static final double RABBIT_SPAWN_RATE = .2;

    public static final double REPRODUCTION_RATE = 1_250;
    public static final double LUCK_OF_JUMPING = .5;
    public static final int JUMP_FREQUENCY = 50;

    private final Animation animation;


    public Rabbit(final Environment environment, int x, int y)
    {
        super(environment, x, y);

        this.setPv(3);
        this.animation = new Animation();
        this.velocity = 1;
        this.offset[0] = (Math.random() <= .5) ? Entity.IS_MOVING_RIGHT : Entity.IS_MOVING_LEFT;
        this.gravity.amplitude = 40;
    }

    public Rabbit(final Environment environment) { this(environment, 0, 0); }

    @Override public void updates()
    {
        if (this.isIDLEonY() && !this.air) {
            this.gravity.xInit = this.x.get();
            this.gravity.yInit = this.y.get();
            this.gravity.vInit = this.velocity;
            this.gravity.degInit =  -90;

            this.gravity.timer = .0;
        }

        this.collide();
        this.move();
        this.worldLimit();

        if (!Objects.isNull(this.rect))
            this.rect.updates(x.get(), y.get());
        this.animation.loop();
    }

    @Override public void move()
    {
        TileMaps tileMaps = this.environment.getTileMaps();
        this.setX(this.x.get() + this.getOffsetMoveX() * this.velocity);

        if (this.isIDLEonY() && this.isMoving()) {
            int xProbablyVoid = (int) ((getX()+((this.isMovingLeft()) ? 0 : this.environment.widthTile)) / this.environment.widthTile);
            int yProbablyVoid = (int) (getY() / environment.heightTile);

            // Si du vide risque d'y avoir lors de son déplacement
            if (tileMaps.isSkyTile(xProbablyVoid, yProbablyVoid+2)) {
                this.offset[0] = (-1) * this.getOffsetMoveX();
            } else {
                boolean mustJump = this.environment.getTicks() % Rabbit.JUMP_FREQUENCY == 0;
                if (mustJump) {
                    boolean jumpOrNot = Math.random() < Rabbit.LUCK_OF_JUMPING;
                    if (jumpOrNot) {
                        this.jump();
                        this.gravity.degInit = -90;
                    }
                }
            }
        }
    }

    @Override public List<Entity> reproduction(final Environment environment)
    {
        List<Entity> children = new ArrayList<>();
        if (environment.getRabbits().size() < GenerateEntity.MAX_SPAWN_RABBIT) {
            // TODO les lapins doivent se reproduire
        }

        return children;
    }

    @Override public void collide()
    {
        Map<String, Boolean> whereCollide = super.collide(this.environment);

        if (!whereCollide.isEmpty()) {
            if (whereCollide.get("left").equals(Boolean.TRUE) || whereCollide.get("right").equals(Boolean.TRUE))
                this.offset[0] = (-1) * this.getOffsetMoveX();
        }
    }

    @Override public void hit()
    {
        Environment.playSound("sound/daggerswipe.wav", false);

        if (this.getPv() <= 0) {
            this.environment.getPlayer().pickup(new Meat(this.environment));

            this.environment.getRabbits().remove(this);
            this.environment.getEntities().remove(this);
        }
        this.setPv(this.getPv() - 1);
    }

    @Override public void spawn(final int x, final int y)
    {
        Environment.playSound("sound/Rabbit_Spawn.wav", false);

        this.setX(x);
        this.setY(y);
        this.getGravity().setXInit(x);
        this.getGravity().setYInit(y);
        this.setRect(environment.widthTile, environment.heightTile);

        this.environment.getEntities().add(0, this);
        this.environment.getRabbits().add(this);
    }

    public void worldLimit()
    {
        if (super.worldLimit(this.environment))
            this.offset[0] = (-1) * this.getOffsetMoveX();
    }


    @Override public Animation getAnimation() { return this.animation; }
}
