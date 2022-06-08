package fr.sae.terraria.vue;

import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.Slime;
import fr.sae.terraria.modele.entities.entity.Entity;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class SlimeView
{
    private final ImageView slimeImgView;

    private final Image slimeImg;

    private final Slime slime;
    private int widthTile;
    private int heightTile;


    public SlimeView(final Slime slime, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        super();
        this.slime = slime;

        this.widthTile = (int) (scaleMultiplicatorWidth * TileMaps.TILE_DEFAULT_SIZE);
        this.heightTile = (int) (scaleMultiplicatorHeight * TileMaps.TILE_DEFAULT_SIZE);

        this.slimeImg = View.loadAnImage("sprites/slimes/green_slime.png", widthTile*4, heightTile);
        this.slimeImgView = View.createImageView(slime, slimeImg);
    }

    private void setAnimation()
    {
        this.slime.getAnimation().getFrameProperty().addListener((obs, oldV, newV) -> {
            Rectangle2D frameRect = new Rectangle2D(newV.intValue() * this.widthTile, 0, this.widthTile, this.heightTile);
            this.slimeImgView.setViewport(frameRect);
        });
    }

    public void displaySlime(final Pane display)
    {
        this.setAnimation();
        display.getChildren().add(this.slimeImgView);
    }
}
