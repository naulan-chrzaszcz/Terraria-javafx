package fr.sae.terraria.vue;

import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.Rabbit;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class RabbitView
{
    private final ImageView rabbitImgView;

    private final Image rabbitLeftImg;
    private final Image rabbitRightImg;

    private final Rabbit rabbit;

    private int widthTile;
    private int heightTile;


    public RabbitView(final Rabbit rabbit, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        super();
        this.rabbit = rabbit;

        this.widthTile = (int) (scaleMultiplicatorWidth * TileMaps.TILE_DEFAULT_SIZE);
        this.heightTile = (int) (scaleMultiplicatorHeight * TileMaps.TILE_DEFAULT_SIZE);

        this.rabbitLeftImg = View.loadAnImage("sprites/rabbit/rabbit_left.png", widthTile*4, heightTile);
        this.rabbitRightImg = View.loadAnImage("sprites/rabbit/rabbit_right.png", widthTile*4, heightTile);
        this.rabbitImgView = View.createImageView(rabbit, rabbitLeftImg);
    }

    private void setAnimation()
    {
        // Change la limite de frame de l'animation selon le sprite sheet chargé
        this.rabbitImgView.imageProperty().addListener((obs, oldImg, newImg) -> {
            int newEndFrame = (int) (newImg.getWidth() / this.widthTile);
            if (this.rabbit.getAnimation().getFrame() >= newEndFrame)
                this.rabbit.getAnimation().reset();
            this.rabbit.getAnimation().setEndFrame(newEndFrame);
        });

        this.rabbit.getAnimation().getFrameProperty().addListener((obs, oldFrame, newFrame) -> {
            this.rabbitImgView.setViewport(new Rectangle2D(0, 0, this.widthTile, this.heightTile));
            if (this.rabbit.isMoving()) {
                Rectangle2D frameRect = new Rectangle2D((newFrame.intValue() * this.widthTile), 0, this.widthTile, this.heightTile);

                this.rabbitImgView.setViewport(frameRect);
                this.rabbitImgView.setImage((this.rabbit.isMovingLeft()) ? this.rabbitLeftImg : this.rabbitRightImg);
            }
        });
    }

    public void displayRabbit(final Pane display)
    {
        this.setAnimation();
        display.getChildren().add(this.rabbitImgView);
    }


    public ImageView getRabbitImgView() { return this.rabbitImgView; }
}
