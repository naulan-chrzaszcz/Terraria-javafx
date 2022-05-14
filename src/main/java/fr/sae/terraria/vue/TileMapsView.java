package fr.sae.terraria.vue;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.blocks.Dirt;
import fr.sae.terraria.modele.blocks.Stone;
import fr.sae.terraria.modele.entities.entity.Entity;
import javafx.beans.property.IntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class TileMapsView
{
    private Pane display;

    private Environment environment;

    private IntegerProperty tileWidth;
    private IntegerProperty tileHeight;

    private Image floorTopImg;
    private Image floorLeftImg;
    private Image floorRightImg;
    private Image stoneImg;
    private Image dirtImg;
    private Image playerImg;


    public TileMapsView(Environment environment, Pane display, IntegerProperty tileWidth, IntegerProperty tileHeight)
    {
        this.display = display;
        this.environment = environment;

        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;

        InputStream floorTopPath = Terraria.class.getResourceAsStream("tiles/floor-top.png");
        if (floorTopPath == null) try {
            floorTopPath = new FileInputStream("src/main/resources/fr/sae/terraria/tiles/floor-top.png");
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }
        floorTopImg = new Image(floorTopPath, tileWidth.get(), tileHeight.get(), false, false);

        InputStream floorLeftPath = Terraria.class.getResourceAsStream("tiles/floor-left.png");
        if (floorLeftPath == null) try {
            floorLeftPath = new FileInputStream("src/main/resources/fr/sae/terraria/tiles/floor-left.png");
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }
        floorLeftImg = new Image(floorLeftPath, tileWidth.get(), tileHeight.get(), false, false);

        InputStream floorRightPath = Terraria.class.getResourceAsStream("tiles/floor-right.png");
        if (floorRightPath == null) try {
            floorRightPath = new FileInputStream("src/main/resources/fr/sae/terraria/tiles/floor-right.png");
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }
        floorRightImg = new Image(floorRightPath, tileWidth.get(), tileHeight.get(), false, false);

        InputStream stonePath = Terraria.class.getResourceAsStream("tiles/rock-fill.png");
        if (stonePath == null) try {
            stonePath = new FileInputStream("src/main/resources/fr/sae/terraria/tiles/rock-fill.png");
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }
        stoneImg = new Image(stonePath, tileWidth.get(), tileHeight.get(), false, false);

        InputStream dirtPath = Terraria.class.getResourceAsStream("tiles/dirt-top.png");
        if (dirtPath == null) try {
            dirtPath = new FileInputStream("src/main/resources/fr/sae/terraria/tiles/dirt-top.png");
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }
        dirtImg = new Image(dirtPath, tileWidth.get(), tileHeight.get(), false, false);

        InputStream playerPath = Terraria.class.getResourceAsStream("sprites/player/player_idle.png");
        if (playerPath == null) try {
            playerPath = new FileInputStream("src/main/resources/fr/sae/terraria/sprites/player/player_idle.png");
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }
        playerImg = new Image(playerPath, (int) (tileWidth.get()/1.25), (int) (tileHeight.get()*2/1.25), false, false);
    }

    public void displayMaps(TileMaps tiles)
    {
        for (int y = 0; y < tiles.getHeight() ; y++)
            for (int x = 0 ; x < tiles.getWidth() ; x++)
                switch (tiles.getTile(y,x))
                {
                    case TileMaps.STONE:
                        Stone stoneEntity = new Stone(x*tileWidth.get(), y*tileHeight.get());
                        stoneEntity.setRect((int) stoneImg.getWidth(), (int) stoneImg.getHeight());
                        display.getChildren().add(createImageView(stoneEntity, stoneImg));
                        environment.getEntities().add(stoneEntity);
                        break;
                    case TileMaps.DIRT:
                        Dirt dirtSprite = new Dirt(x*tileWidth.get(), y*tileHeight.get());
                        dirtSprite.setRect((int) dirtImg.getWidth(), (int) dirtImg.getHeight());
                        display.getChildren().add(createImageView(dirtSprite, dirtImg));
                        environment.getEntities().add(dirtSprite);
                        break;
                    case TileMaps.FLOOR_TOP:
                        Dirt floorTopSprite = new Dirt(x*tileWidth.get(), y*tileHeight.get());
                        floorTopSprite.setRect((int) floorTopImg.getWidth(), (int) floorTopImg.getHeight());
                        display.getChildren().add(createImageView(floorTopSprite, floorTopImg));
                        environment.getEntities().add(floorTopSprite);
                        break;
                    case TileMaps.FLOOR_LEFT:
                        Dirt floorLeftSprite = new Dirt(x*tileWidth.get(), y*tileHeight.get());
                        floorLeftSprite.setRect((int) floorLeftImg.getWidth(), (int) floorLeftImg.getHeight());
                        display.getChildren().add(createImageView(floorLeftSprite, floorLeftImg));
                        environment.getEntities().add(floorLeftSprite);
                        break;
                    case TileMaps.FLOOR_RIGHT:
                        Dirt floorRightSprite = new Dirt(x*tileWidth.get(), y*tileHeight.get());
                        floorRightSprite.setRect((int) floorRightImg.getWidth(), (int) floorRightImg.getHeight());
                        display.getChildren().add(createImageView(floorRightSprite, floorRightImg));
                        environment.getEntities().add(floorRightSprite);
                        break;
                }
    }

    public void displayPlayer()
    {
        ImageView playerImgView = new ImageView(playerImg);
        environment.getPlayer().setRect((int) playerImg.getWidth(), (int) playerImg.getHeight());
        playerImgView.translateYProperty().bind(environment.getPlayer().getYProperty());
        playerImgView.translateXProperty().bind(environment.getPlayer().getXProperty());
        display.getChildren().add(playerImgView);
    }

    private ImageView createImageView(Entity entity, Image img)
    {
        ImageView imageView = new ImageView(img);
        imageView.translateXProperty().bind(entity.getXProperty());
        imageView.translateYProperty().bind(entity.getYProperty());

        return imageView;
    }
}
