package fr.sae.terraria.vue;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.Player;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class PlayerView
{
    private Player player;

    private ImageView playerImgView;

    private Image playerMoveRightImg;
    private Image playerMoveLeftImg;
    private Image playerIdleImg;
    private Image healthBarImg;

    private int tileWidth;
    private int tileHeight;


    public PlayerView(Player player, int tileWidth, int tileHeight)
    {
        this.player = player;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        this.playerImgView = new ImageView();

        this.playerIdleImg = View.loadAnImage("sprites/player/player_idle.png", tileWidth, tileHeight);
        this.playerMoveRightImg = View.loadAnImage("sprites/player/player_moveRight.png", tileWidth*4, tileHeight);
        this.playerMoveLeftImg = View.loadAnImage("sprites/player/player_moveLeft.png", tileWidth*4, tileHeight);
        // this.healthBarImg = View.loadAnImage("healthBar.png", )
    }

    public void displayPlayer(Pane display)
    {
        this.playerImgView.translateYProperty().bind(player.getYProperty());
        this.playerImgView.translateXProperty().bind(player.getXProperty());
        this.playerImgView.setImage(playerIdleImg);
        this.setAnimation();

        display.getChildren().add(playerImgView);
    }

    private void setAnimation()
    {
        player.getAnimation().getFrame().addListener((obs, oldFrame, newFrame) -> {
            this.playerImgView.setViewport(new Rectangle2D(0, 0, tileWidth, tileHeight));
            if (player.offset[0] == 0 && player.offset[1] == 0)
                this.playerImgView.setImage(this.playerIdleImg);

            if (player.offset[0] == 1 || player.offset[0] == -1) {
                Rectangle2D frameRect = new Rectangle2D((newFrame.intValue() * tileWidth), 0, tileWidth, tileHeight);

                this.playerImgView.setViewport(frameRect);
                this.playerImgView.setImage((player.offset[0] == -1) ? playerMoveLeftImg : playerMoveRightImg);
            }
        });

        // Change la limite de frame de l'animation selon le sprite sheet chargé
        this.playerImgView.imageProperty().addListener((obs, oldImg, newImg) -> {
            int newEndFrame = (int) (newImg.getWidth() / tileWidth);
            if (this.player.getAnimation().getFrame().get() >= newEndFrame)
                this.player.getAnimation().reset();
            this.player.getAnimation().setEndFrame(newEndFrame);
        });
    }

    public void displayHealthBar()
    {
        player.getPvProperty().addListener((obs, oldPv, newPv) -> {

        });
    }
}
