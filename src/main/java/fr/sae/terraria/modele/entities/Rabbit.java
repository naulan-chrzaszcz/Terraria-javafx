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


public class Rabbit extends Entity implements CollideObjectType, ReproductiveObjectType, MovableObjectType, CollapsibleObjectType
{
    public static final int WHEN_SPAWN_A_RABBIT = 2_500;
    public static final double RABBIT_SPAWN_RATE = .2;

    public static final double REPRODUCTION_RATE = 1_250;
    public static final double LUCK_OF_JUMPING = .5;
    public static final int JUMP_FREQUENCY = 50;

    private final Environment environment;
    private final Animation animation;


    public Rabbit(final Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;

        this.animation = new Animation();
        this.velocity = 1;
        this.offset[0] = (Math.random() <= .5) ? Entity.IS_MOVING_RIGHT : Entity.IS_MOVING_LEFT;
        this.gravity.amplitude = 40;
    }

    public Rabbit(final Environment environment) { this(environment, 0, 0); }

    @Override public void updates()
    {
        if (this.offset[1] == Entity.IDLE && !this.air) {
            this.gravity.xInit = this.x.get();
            this.gravity.yInit = this.y.get();
            this.gravity.vInit = this.velocity;
            this.gravity.degInit =  -90;

            this.gravity.timer = .0;
        }

        this.move();
        this.worldLimit();

        if (!Objects.isNull(this.rect))
            this.rect.updates(x.get(), y.get());
        this.animation.loop();
    }

    @Override public void move()
    {
        this.setX(this.x.get() + this.offset[0] * this.velocity);

        if (this.offset[1] == Entity.IDLE && this.offset[0] != Entity.IDLE) {
            int xProbablyVoid = (int) ((getX()+((this.offset[0] == Entity.IS_MOVING_LEFT) ? 0 : this.rect.getWidth()))/ this.environment.widthTile) + 1;
            int yProbablyVoid = (int) ((getY() + (this.rect.getHeight()/2)) / environment.heightTile);

            // Si du vide risque d'y avoir lors de son déplacement
            if (environment.getTileMaps().getTile(xProbablyVoid+((this.offset[0] == Entity.IS_MOVING_LEFT) ? 1 : (-1)), yProbablyVoid + 2) == TileMaps.SKY) {
                this.offset[0] = (-1) * this.offset[0];
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
                this.offset[0] = (-1) * this.offset[0];
        }
    }

    @Override public void hit()
    {
        Environment.playSound("sound/daggerswipe.wav", false);
        this.environment.getPlayer().pickup(new Meat(this.environment));
    }

    /** Modifie l'offset qui permet de le déplacer vers la droite */
    @Override public void moveRight() { super.moveRight(); }
    /** Modifie l'offset qui permet de le déplacer vers la gauche */
    @Override public void moveLeft() { super.moveLeft(); }
    /** Modifie l'offset qui permet de le faire sauter */
    @Override public void jump() { super.jump(); }
    /** Modifie l'offset qui permet de tomber */
    @Override public void fall() { super.fall(); }

    @Override public void worldLimit()
    {
        if (super.worldLimit(this.environment))
            this.offset[0] = (-1) * this.offset[0];
    }


    @Override public Animation getAnimation() { return this.animation; }
}
