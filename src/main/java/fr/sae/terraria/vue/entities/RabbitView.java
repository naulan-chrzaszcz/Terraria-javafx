package fr.sae.terraria.vue.entities;

import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.Rabbit;
import fr.sae.terraria.vue.View;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;


public class RabbitView extends EntityView
{

    private final Image rabbitLeftImg;
    private final Image rabbitRightImg;


    private int widthTile;
    private int heightTile;


    public RabbitView(final Rabbit rabbit, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        super(rabbit);

        this.widthTile = (int) (scaleMultiplicatorWidth * TileMaps.TILE_DEFAULT_SIZE);
        this.heightTile = (int) (scaleMultiplicatorHeight * TileMaps.TILE_DEFAULT_SIZE);

        this.rabbitLeftImg = View.loadAnImage("sprites/rabbit/rabbit_left.png", widthTile*4, heightTile);
        this.rabbitRightImg = View.loadAnImage("sprites/rabbit/rabbit_right.png", widthTile*4, heightTile);
    }

    @Override protected void animation(int frame) {
        this.imgView.setViewport(new Rectangle2D(0, 0, this.widthTile, this.heightTile));
        if (((Rabbit) this.entity).isMoving()) {
            Rectangle2D frameRect = new Rectangle2D((frame * this.widthTile), 0, this.widthTile, this.heightTile);

            this.imgView.setViewport(frameRect);
            this.imgView.setImage((((Rabbit) this.entity).isMovingLeft()) ? this.rabbitLeftImg : this.rabbitRightImg);
        }
    }
}
