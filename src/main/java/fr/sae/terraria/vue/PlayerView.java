package fr.sae.terraria.vue;

import fr.sae.terraria.modele.blocks.Dirt;
import fr.sae.terraria.modele.blocks.Stone;
import fr.sae.terraria.modele.entities.Player;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.security.Key;


public class PlayerView
{
    private Player player;

    private ImageView playerImgView;
    private ImageView inventoryImgView;

    private Image inventoryImg;

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
        this.inventoryImgView = new ImageView();

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

    public void displayInventory(Pane display)
    {
        int nombreElementsAffichés = 9;
        this.inventoryImg = View.loadAnImage("inventory.png",tileWidth*9,tileHeight);

        this.player.ramasser(new Dirt(0,0));
        this.player.ramasser(new Dirt(1,1));
        this.player.ramasser(new Stone(1,1));
        this.player.ramasser(new Dirt(1,1));
        this.player.ramasser(new Dirt(1,1));
        this.player.ramasser(new Dirt(1,1));

        System.out.println(this.player.getInventory());
        this.inventoryImgView.setImage(inventoryImg);
        this.inventoryImgView.setX((1280-44*9)/2);
        this.inventoryImgView.setY(600);
        display.getChildren().add(inventoryImgView);
        System.out.println(this.player.nombreObjetsDansInventaire());
        if (this.player.nombreObjetsDansInventaire()<= 9){
            nombreElementsAffichés = this.player.nombreObjetsDansInventaire();
        }
        int[] compteur = new int[1];
        this.player.getInventory().forEach((key,integer) -> {
            Image item = null;
            ImageView itemView = new ImageView();

            if (key instanceof Dirt){
                item = View.loadAnImage("tiles/floor-top.png",25,25);
            }

            else if (key instanceof Stone){
                item = View.loadAnImage("tiles/rock-fill.png",25,25);
            }
            itemView.setX(inventoryImgView.getX()+ (tileWidth * (compteur[0]+1))/4 + (tileWidth*compteur[0]*0.742));
            itemView.setY(inventoryImgView.getY()+10);
            itemView.setImage(item);
            display.getChildren().add(itemView);
            compteur[0]++;

        });


    }


    public void displayHealthBar()
    {
        player.getPvProperty().addListener((obs, oldPv, newPv) -> {

        });
    }
}
