package fr.sae.terraria.vue.entities;

import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.Slime;
import fr.sae.terraria.vue.View;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class SlimeView extends EntityView
{
    private final Image slimeImg;

    private int widthTile;
    private int heightTile;


    public SlimeView(final Slime slime, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        super(slime);

        this.widthTile = (int) (scaleMultiplicatorWidth * TileMaps.TILE_DEFAULT_SIZE);
        this.heightTile = (int) (scaleMultiplicatorHeight * TileMaps.TILE_DEFAULT_SIZE);

        this.slimeImg = View.loadAnImage("sprites/slimes/green_slime.png", widthTile*4, heightTile);
        this.imgView.setImage(slimeImg);
    }

    @Override protected void animation(int frame) {
        Rectangle2D frameRect = new Rectangle2D(frame * this.widthTile, 0, this.widthTile, this.heightTile);
        this.imgView.setViewport(frameRect);
    }
}
