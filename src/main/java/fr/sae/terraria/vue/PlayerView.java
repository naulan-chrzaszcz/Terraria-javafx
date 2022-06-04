package fr.sae.terraria.vue;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.player.Player;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javax.sound.sampled.Clip;


public class PlayerView
{
    private final ImageView playerImgView;

    private final Image playerMoveRightImg;
    private final Image playerMoveLeftImg;
    private final Image playerIdleImg;

    private final Player player;

    private int widthPlayer;
    private int heightPlayer;


    public PlayerView(Player player, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.player = player;

        this.playerIdleImg = View.loadAnImage("sprites/player/player_idle.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.playerMoveRightImg = View.loadAnImage("sprites/player/player_moveRight.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.playerMoveLeftImg = View.loadAnImage("sprites/player/player_moveLeft.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);

        this.widthPlayer = (int) this.playerIdleImg.getWidth();
        this.heightPlayer = (int) this.playerIdleImg.getHeight();

        this.playerImgView = View.createImageView(player, playerIdleImg);
    }

    /** Applique l'animation sur le joueur */
    private void setAnimation()
    {
        // Change la limite de frame de l'animation selon le sprite sheet chargé
        this.playerImgView.imageProperty().addListener((obs, oldImg, newImg) -> {
            int newEndFrame = (int) (newImg.getWidth() / widthPlayer);
            if (this.player.getAnimation().getFrame() >= newEndFrame)
                this.player.getAnimation().reset();
            this.player.getAnimation().setEndFrame(newEndFrame);
        });

        player.getAnimation().getFrameProperty().addListener((obs, oldFrame, newFrame) -> {
            this.playerImgView.setViewport(new Rectangle2D(0, 0, widthPlayer, heightPlayer));
            if (player.offset[0] == Entity.IDLE && player.offset[1] == Entity.IDLE)
                this.playerImgView.setImage(this.playerIdleImg);

            if (player.offset[0] == Entity.IS_MOVING_RIGHT || player.offset[0] == Entity.IS_MOVING_LEFT) {
                Rectangle2D frameRect = new Rectangle2D((newFrame.intValue() * widthPlayer), 0, widthPlayer, heightPlayer);

                this.playerImgView.setViewport(frameRect);
                this.playerImgView.setImage((player.offset[0] == -1) ? playerMoveLeftImg : playerMoveRightImg);
            }
        });
    }

    /** Synchronise les coordonnées en x et y du joueur avec l'image et ensuite l'affiche sur le Pane */
    public void displayPlayer(Pane display)
    {
        this.setAnimation();
        display.getChildren().add(playerImgView);
    }
}
