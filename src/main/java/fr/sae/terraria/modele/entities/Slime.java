package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.*;

import java.util.Map;
import java.util.Objects;


public class Slime extends Entity implements CollideObjectType, MovableObjectType, CollapsibleObjectType, SpawnableObjectType
{
    public static final int WHEN_SPAWN_A_SLIME = 100;
    public static final double SLIME_SPAWN_RATE = .2;

    private final Environment environment;
    private int nbIdle=0;


    public Slime(Environment environment, int x, int y)
    {
        super(x, y);
        this.environment = environment;
        this.velocity = 2;

        this.setPv(3);
        this.animation = new Animation();

        this.gravity.amplitude = 25;
    }

    public Slime(Environment environment) { this(environment, 0, 0); }

    @Override public void updates() { /* TODO document why this method is empty */
        if (this.offset[1] == Entity.IDLE && !this.air) {
            this.gravity.xInit = this.x.get();
            this.gravity.yInit = this.y.get();
            this.gravity.vInit = this.velocity;
            this.gravity.degInit = -90;
            this.gravity.timer = .0;
        }

        this.move();
        this.worldLimit();

        if (!Objects.isNull(this.rect))
            this.rect.updates(x.get(), y.get());
        this.animation.loop();
    }

    @Override public void collide() {
        Map<String, Boolean> whereCollide = super.collide(this.environment);

        if (!whereCollide.isEmpty()) {
            if (whereCollide.get("left").equals(Boolean.TRUE) || whereCollide.get("right").equals(Boolean.TRUE))
                this.offset[0] = Entity.IDLE;

        }
    }

    @Override public void move()
    {
        if (environment.getPlayer().getX() > this.x.getValue())
            this.offset[0] = IS_MOVING_RIGHT;
        else if (environment.getPlayer().getX() < this.x.getValue())
            this.offset[0] = IS_MOVING_LEFT;
        else this.offset[0] = IDLE;

        if (((int) (this.animation.getFrame()) == 3)){
            this.jump();
            this.gravity.degInit = -90;
        }

        if (this.offset[1] == Entity.IS_JUMPING)
            this.setX(this.getX() + this.offset[0] * this.velocity);


    }



    @Override public void hit()
    {
        Environment.playSound("sound/daggerswipe.wav", false);
    }

    @Override public void spawn(int x, int y)
    {
        this.setX(x);
        this.setY(y);
        this.getGravity().setXInit(x);
        this.getGravity().setYInit(y);
        this.setRect(environment.widthTile, environment.heightTile);
        this.environment.getEntities().add(0, this);
        this.environment.getSlimes().add(0, this);
    }

    @Override public void moveRight() { super.moveRight(); }

    @Override public void moveLeft() { super.moveLeft(); }

    @Override public void jump() { super.jump(); }

    @Override public void fall() { super.fall(); }

    @Override public void worldLimit() { super.worldLimit(this.environment); }


}
