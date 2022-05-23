package fr.sae.terraria.vue;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.entities.entity.Entity;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.geometry.Rectangle2D;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import fr.sae.terraria.modele.blocks.Dirt;
import fr.sae.terraria.modele.blocks.Stone;
import fr.sae.terraria.modele.entities.Player;
import javafx.scene.text.Font;


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

    private double scaleMultiplicatorWidth;
    private double scaleMultiplicatorHeight;

    private int widthPlayer;
    private int heightPlayer;


    public PlayerView(Player player, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.player = player;
        this.scaleMultiplicatorWidth = scaleMultiplicatorWidth;
        this.scaleMultiplicatorHeight = scaleMultiplicatorHeight;

        this.playerImgView = new ImageView();
        this.inventoryImgView = new ImageView();

        this.playerIdleImg = View.loadAnImage("sprites/player/player_idle.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.playerMoveRightImg = View.loadAnImage("sprites/player/player_moveRight.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.playerMoveLeftImg = View.loadAnImage("sprites/player/player_moveLeft.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.healthBarImg = View.loadAnImage("health.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);

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

    public void displayInventory(Pane display)
    {
        int nombreElementsAffichés = 9;
        this.inventoryImg = View.loadAnImage("inventory.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);

        this.player.ramasser(new Dirt(0,0));
        this.player.ramasser(new Dirt(1,1));
        this.player.ramasser(new Stone(1,1));
        this.player.ramasser(new Dirt(1,1));
        this.player.ramasser(new Dirt(1,1));
        this.player.ramasser(new Dirt(1,1));


        this.inventoryImgView.setImage(inventoryImg);
        this.inventoryImgView.setX(((scaleMultiplicatorWidth * Terraria.DISPLAY_RENDERING_WIDTH - inventoryImgView.getImage().getWidth())/2));
        this.inventoryImgView.setY(600);
        // System.out.println(this.player.nombreObjetsDansInventaire());
        if (this.player.nombreObjetsDansInventaire()<= 9)
            nombreElementsAffichés = this.player.nombreObjetsDansInventaire();

        Rectangle frameInventoryImg = new Rectangle();
        frameInventoryImg.setWidth(inventoryImg.getWidth()+(2*scaleMultiplicatorWidth));
        frameInventoryImg.setHeight(inventoryImg.getHeight()+(2*scaleMultiplicatorHeight));
        frameInventoryImg.setX(inventoryImgView.getX()-(1*scaleMultiplicatorWidth));
        frameInventoryImg.setY(inventoryImgView.getY()-(1*scaleMultiplicatorHeight));
        display.getChildren().add(frameInventoryImg);
        display.getChildren().add(inventoryImgView);
        System.out.println(nombreElementsAffichés);

        int[] compteur = new int[1];
       for (int integer =0 ; integer< nombreElementsAffichés; integer++){

            Image item = null;
            ImageView itemView = new ImageView();
            if (this.player.getInventory().get(integer).get(0) instanceof  Dirt)
                item = View.loadAnImage("tiles/floor-top.png",25,25);

            else if (this.player.getInventory().get(integer).get(0) instanceof Stone)
                item = View.loadAnImage("tiles/rock-fill.png",25,25);

            itemView.setX(inventoryImgView.getX() + (((inventoryImgView.getImage().getWidth()/9) - item.getWidth())/2) + ((inventoryImgView.getImage().getWidth()/9)*compteur[0]));
            itemView.setY(inventoryImgView.getY() + ((inventoryImgView.getImage().getHeight() - item.getHeight())/2));
            itemView.setImage(item);
            display.getChildren().add(itemView);


           Label nombreObjets = new Label();
           nombreObjets.setText(String.valueOf(this.player.getInventory().get(integer).size()));

           StringProperty stringProperty = new SimpleStringProperty(String.valueOf(this.player.getInventory().get(integer).size()));
           nombreObjets.textProperty().bind(stringProperty);
           this.player.getInventory().get(integer).addListener(new ListChangeListener<Entity>() {
               @Override
               public void onChanged(Change<? extends Entity> c) {
                   stringProperty.setValue(String.valueOf(c.getList().size()));
               }
           });
           nombreObjets.setFont(new Font("Arial",5 * scaleMultiplicatorWidth));
           nombreObjets.setLayoutX(inventoryImgView.getX() + (((inventoryImgView.getImage().getWidth()/9/2) - item.getWidth())) + ((inventoryImgView.getImage().getWidth()/9)*compteur[0]) + 3);
           nombreObjets.setLayoutY(inventoryImgView.getY() + ((inventoryImgView.getImage().getHeight() - item.getHeight()/2)) - 5);
           nombreObjets.setTextFill(Color.WHITE);
           display.getChildren().add(nombreObjets);

            compteur[0]++;

        }
    }

    public void displayHealthBar(Pane display)
    {
        ImageView[] healths = new ImageView[(int) (player.getPvMax())];
        for (int i = 0; i < player.getPvMax(); i++) {
            ImageView healthView = new ImageView(View.loadAnImage("health.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight));
            Rectangle2D viewPort = new Rectangle2D((healthView.getImage().getWidth()/3)*2, 0, (healthView.getImage().getWidth()/3), healthView.getImage().getHeight());

            healthView.setViewport(viewPort);
            healthView.setX(inventoryImgView.getX() + ((healthView.getImage().getWidth()/3)*i));
            healthView.setY((inventoryImgView.getY() - healthView.getImage().getHeight()) - (2*scaleMultiplicatorHeight));
            display.getChildren().add(healthView);
            healths[i] = healthView;
        }

        player.getPvProperty().addListener((obs, oldPv, newPv) -> {
            if (oldPv.intValue() >= 0) {
                ImageView healthView = healths[oldPv.intValue()-1];

                Rectangle2D viewPort = new Rectangle2D((healthView.getImage().getWidth()/3)*0, 0, (healthView.getImage().getWidth()/3), healthView.getImage().getHeight());
                healthView.setViewport(viewPort);
            }
        });
    }
}
