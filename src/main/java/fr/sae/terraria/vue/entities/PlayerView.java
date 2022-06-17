package fr.sae.terraria.vue.entities;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.vue.View;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.util.Duration;


public class PlayerView extends EntityView
{
    private final Image playerMoveRightImg;
    private final Image playerMoveLeftImg;
    private final Image playerIdleImg;

    private int widthPlayer;
    private int heightPlayer;


    public PlayerView(final Player player, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        super(player);

        this.playerIdleImg = View.loadAnImage("sprites/player/player_idle.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.playerMoveRightImg = View.loadAnImage("sprites/player/player_moveRight.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.playerMoveLeftImg = View.loadAnImage("sprites/player/player_moveLeft.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.imgView.setImage(playerIdleImg);

        this.widthPlayer = (int) this.playerIdleImg.getWidth();
        this.heightPlayer = (int) this.playerIdleImg.getHeight();

        boolean[] visible = new boolean[] {true};
        player.hitProperty().addListener((obs, oldBool, newBool) -> {
            if (newBool.booleanValue()) {
                Timeline timeLine = new Timeline();
                timeLine.setCycleCount(8);

                KeyFrame key = new KeyFrame(Duration.seconds((Player.TIME_BEFORE_HITTING_AGAIN_THE_PLAYER * Terraria.TARGET_FPS)/8), b -> {
                    visible[0] = !visible[0];
                    this.imgView.setVisible(visible[0]);
                });

                timeLine.getKeyFrames().add(key);
                timeLine.play();
            }
        });
    }

    @Override protected void animation(int frame)
    {
        this.imgView.setViewport(new Rectangle2D(0, 0, this.widthPlayer, this.heightPlayer));
        if (((Player) this.entity).isIDLEonX() && ((Player) this.entity).isIDLEonY())
            this.imgView.setImage(this.playerIdleImg);

        if (((Player) this.entity).isMoving()) {
            Rectangle2D frameRect = new Rectangle2D((frame * this.widthPlayer), 0, this.widthPlayer, this.heightPlayer);

            this.imgView.setViewport(frameRect);
            this.imgView.setImage((((Player) this.entity).isMovingLeft()) ? this.playerMoveLeftImg : this.playerMoveRightImg);
        }
    }
}
