package fr.sae.terraria.vue;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.Player;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class PlayerView
{
    private Environment environment;

    private ImageView playerImgView;

    private Image playerMoveRightImg;
    private Image playerMoveLeftImg;
    private Image playerIdleImg;
    private Image healthBarImg;

    private int tileWidth;
    private int tileHeight;


    public PlayerView(Environment environment, int tileWidth, int tileHeight)
    {
        this.environment = environment;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        playerImgView = new ImageView();

        this.playerIdleImg = View.loadAnImage("sprites/player/player_idle.png", tileWidth, tileWidth);
        this.playerMoveRightImg = View.loadAnImage("sprites/player/player_moveRight.png", tileWidth*4, tileWidth);
        this.playerMoveLeftImg = View.loadAnImage("sprites/player/player_moveLeft.png", tileWidth*4, tileWidth);
        // this.healthBarImg = View.loadAnImage("healthBar.png", )
    }

    public void displayPlayer(Pane display)
    {
        this.playerImgView.translateYProperty().bind(environment.getPlayer().getYProperty());
        this.playerImgView.translateXProperty().bind(environment.getPlayer().getXProperty());
        this.playerImgView.setImage(playerIdleImg);
        this.setAnimation();

        display.getChildren().add(playerImgView);
    }

    private void setAnimation()
    {
        this.environment.getPlayer().getAnimation().getFrame().addListener((obs, oldFrame, newFrame) -> {
            Player player = this.environment.getPlayer();

            this.playerImgView.setViewport(new Rectangle2D(0, 0, tileHeight, tileHeight));
            if (player.offset[0] == 0 && player.offset[1] == 0)
                this.playerImgView.setImage(this.playerIdleImg);

            if (player.offset[0] == 1 || player.offset[0] == -1) {
                Rectangle2D frameRect = new Rectangle2D(newFrame.intValue() * tileHeight, 0, tileHeight-3, tileHeight);

                this.playerImgView.setViewport(frameRect);
                this.playerImgView.setImage((player.offset[0] == -1) ? playerMoveLeftImg : playerMoveRightImg);
            }
        });
    }

    public void displayHealthBar()
    {
        environment.getPlayer().getPvProperty().addListener((obs, oldPv, newPv) -> {

        });
    }
}
