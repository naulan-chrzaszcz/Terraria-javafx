package fr.sae.terraria.vue;

import fr.sae.terraria.modele.entities.Player;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class PlayerView
{
    private ImageView playerImgView;

    private Image playerMoveRightImg;
    private Image playerMoveLeftImg;
    private Image playerIdleImg;

    private Player player;

    private int widthPlayer;
    private int heightPlayer;


    public PlayerView(Player player, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.player = player;

        this.playerImgView = new ImageView();

        this.playerIdleImg = View.loadAnImage("sprites/player/player_idle.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.playerMoveRightImg = View.loadAnImage("sprites/player/player_moveRight.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.playerMoveLeftImg = View.loadAnImage("sprites/player/player_moveLeft.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);

        this.widthPlayer = (int) this.playerIdleImg.getWidth();
        this.heightPlayer = (int) this.playerIdleImg.getHeight();
    }

    /** Synchronise les coordonnées en x et y du joueur avec l'image et ensuite l'affiche sur le Pane */
    public void displayPlayer(Pane display)
    {
        this.playerImgView = View.createImageView(player, playerIdleImg);
        this.setAnimation();

        display.getChildren().add(playerImgView);
    }

    /** Applique l'animation sur le joueur */
    private void setAnimation()
    {
        player.getAnimation().getFrame().addListener((obs, oldFrame, newFrame) -> {
            this.playerImgView.setViewport(new Rectangle2D(0, 0, widthPlayer, heightPlayer));
            if (player.offset[0] == 0 && player.offset[1] == 0)
                this.playerImgView.setImage(this.playerIdleImg);

            if (player.offset[0] == 1 || player.offset[0] == -1) {
                Rectangle2D frameRect = new Rectangle2D((newFrame.intValue() * widthPlayer), 0, widthPlayer, heightPlayer);

                this.playerImgView.setViewport(frameRect);
                this.playerImgView.setImage((player.offset[0] == -1) ? playerMoveLeftImg : playerMoveRightImg);
            }
        });

        // Change la limite de frame de l'animation selon le sprite sheet chargé
        this.playerImgView.imageProperty().addListener((obs, oldImg, newImg) -> {
            int newEndFrame = (int) (newImg.getWidth() / widthPlayer);
            if (this.player.getAnimation().getFrame().get() >= newEndFrame)
                this.player.getAnimation().reset();
            this.player.getAnimation().setEndFrame(newEndFrame);
        });
    }
}
