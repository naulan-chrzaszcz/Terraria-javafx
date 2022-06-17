package fr.sae.terraria.modele.entities;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.*;
import fr.sae.terraria.modele.entities.player.inventory.Stack;
import fr.sae.terraria.modele.entities.tools.Tool;

import java.util.Map;
import java.util.Objects;


public class Slime extends EntityMovable implements CollideObjectType, CollapsibleObjectType, SpawnableObjectType
{
    public static final int WHEN_SPAWN_A_SLIME = 2_500;
    public static final double SLIME_SPAWN_RATE = .2;
    public static final int TIME_BEFORE_HITTING_AGAIN_THE_PLAYER = 100;


    public Slime(Environment environment, int x, int y)
    {
        super(environment, x, y);
        this.velocity = 2;

        this.setPv(3);
        this.animation = new Animation();

        this.gravity.amplitude = 25;
        this.gravity.degInit = -90;
    }

    public Slime(Environment environment) { this(environment, 0, 0); }

    @Override public void updates()
    {
        if (this.isIDLEonY() && !this.air) {
            this.gravity.xInit = this.x.get();
            this.gravity.yInit = this.y.get();
            this.gravity.vInit = this.velocity;
            this.gravity.degInit = -90;
            this.gravity.timer = .0;
        }

        this.idleOnX();
        if (this.isJumping()) {
            if (this.environment.getPlayer().getX() > this.x.getValue())
                this.moveRight();
            else if (environment.getPlayer().getX() < this.x.getValue())
                this.moveLeft();
        }

        this.move();
        this.collide();
        this.worldLimit();

        if (!Objects.isNull(this.rect))
            this.rect.updates(x.get(), y.get());
        this.animation.loop();
    }

    @Override public void collide()
    {
        Map<String, Boolean> whereCollide = super.collide(this.environment);

        if (!whereCollide.isEmpty())
            if (whereCollide.get("left").equals(Boolean.TRUE) || whereCollide.get("right").equals(Boolean.TRUE))
                this.idleOnX();

            if (environment.getPlayer().getRect().collideRect(this.getRect()) && !environment.getPlayer().getHit()){
                environment.getPlayer().hit();
                environment.getPlayer().setInvicibilityFrame(this.environment.getTicks());
            }

            if (environment.getPlayer().getHit() &&  this.environment.getTicks() - environment.getPlayer().getInvicibilityFrame() == TIME_BEFORE_HITTING_AGAIN_THE_PLAYER)
                environment.getPlayer().setHit(false);
    }

    @Override public void move()
    {
        if (((int) (this.animation.getFrame()) == 3))
            if (this.isNotFalling()) this.jump();

        this.setX(this.getX() + (this.getOffsetMoveX() * this.velocity));
    }

    @Override public void hit()
    {
        Environment.playSound("sound/daggerswipe.wav", false);

        if (this.getPv() <= 0) {
            // this.environment.getPlayer().pickup(new SlimeBall());

            this.environment.getSlimes().remove(this);
            this.environment.getEntities().remove(this);
        }

        Stack stack = this.environment.getPlayer().getStackSelected();
        if (!Objects.isNull(stack)) {
            if (stack.getItem() instanceof Tool && Tool.isSword((Tool) stack.getItem())) {
                Tool tool = (Tool) stack.getItem();
                this.setPv(this.getPv() - tool.damage());
            }
        } else this.setPv(this.getPv() - .5);
    }

    @Override public void spawn(int x, int y)
    {
        this.setX(x);
        this.setY(y);
        this.getGravity().setXInit(x);
        this.getGravity().setYInit(y);
        this.setRect(environment.widthTile, environment.heightTile);

        this.environment.getEntities().add(this);
        this.environment.getSlimes().add(this);
    }

    public void worldLimit() { super.worldLimit(this.environment); }
}
