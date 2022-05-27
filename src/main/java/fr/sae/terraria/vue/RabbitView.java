package fr.sae.terraria.vue;

import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.Rabbit;
import fr.sae.terraria.modele.entities.entity.Rect;
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


    public RabbitView(Rabbit rabbit, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
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
            int newEndFrame = (int) (newImg.getWidth() / widthTile);
            if (this.rabbit.getAnimation().getFrame().get() >= newEndFrame)
                this.rabbit.getAnimation().reset();
            this.rabbit.getAnimation().setEndFrame(newEndFrame);
        });

        this.rabbit.getAnimation().getFrame().addListener((obs, oldFrame, newFrame) -> {
            this.rabbitImgView.setViewport(new Rectangle2D(0, 0, widthTile, heightTile));
            if (rabbit.offset[0] == 1 || rabbit.offset[0] == -1) {
                Rectangle2D frameRect = new Rectangle2D((newFrame.intValue() * widthTile), 0, widthTile, heightTile);

                rabbitImgView.setViewport(frameRect);
                rabbitImgView.setImage((rabbit.offset[0] == -1) ? rabbitLeftImg : rabbitRightImg);
            }
        });
    }

    public void displayRabbit(Pane display)
    {
        this.setAnimation();

        display.getChildren().add(rabbitImgView);
    }
}
