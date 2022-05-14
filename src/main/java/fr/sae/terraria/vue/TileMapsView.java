package fr.sae.terraria.vue;

import javafx.beans.property.IntegerProperty;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.blocks.Dirt;
import fr.sae.terraria.modele.blocks.Stone;
import fr.sae.terraria.modele.entities.entity.Entity;


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

        this.floorTopImg = this.loadAnImage("tiles/floor-top.png");
        this.floorLeftImg = this.loadAnImage("tiles/floor-left.png");
        this.floorRightImg = this.loadAnImage("tiles/floor-right.png");
        this.stoneImg = this.loadAnImage("tiles/rock-fill.png");
        this.dirtImg = this.loadAnImage("tiles/dirt-top.png");
        this.playerImg = this.loadAnImage("sprites/player/player_idle.png");
    }

    public void displayMaps(TileMaps tiles)
    {
        for (int y = 0; y < tiles.getHeight() ; y++)
            for (int x = 0 ; x < tiles.getWidth() ; x++)
                switch (tiles.getTile(x, y))
                {
                    case TileMaps.STONE:
                        this.createStone(x, y);
                        break;
                    case TileMaps.DIRT:
                        this.createDirt(x, y);
                        break;
                    case TileMaps.FLOOR_TOP:
                    case TileMaps.FLOOR_LEFT:
                    case TileMaps.FLOOR_RIGHT:
                        this.createFloor(tiles.getTile(x, y), x, y);
                        break;
                    default:
                        this.errorTile(tiles.getTile(x, y));
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

    public Image loadAnImage(String path)
    {
        InputStream pathImg = Terraria.class.getResourceAsStream(path);
        if (pathImg == null) try {
            pathImg = new FileInputStream(Terraria.srcPath + path);
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }

        return new Image(pathImg, tileWidth.get(), tileHeight.get(), false, false);
    }

    private ImageView createImageView(Entity entity, Image img)
    {
        ImageView imageView = new ImageView(img);
        imageView.translateXProperty().bind(entity.getXProperty());
        imageView.translateYProperty().bind(entity.getYProperty());

        return imageView;
    }

    private void createStone(int x, int y)
    {
        Stone stoneEntity = new Stone(x*tileWidth.get(), y*tileHeight.get());
        stoneEntity.setRect((int) stoneImg.getWidth(), (int) stoneImg.getHeight());
        display.getChildren().add(createImageView(stoneEntity, stoneImg));
        environment.getEntities().add(stoneEntity);
    }

    private void createDirt(int x, int y)
    {
        Dirt dirtSprite = new Dirt(x*tileWidth.get(), y*tileHeight.get());
        dirtSprite.setRect((int) dirtImg.getWidth(), (int) dirtImg.getHeight());
        display.getChildren().add(createImageView(dirtSprite, dirtImg));
        environment.getEntities().add(dirtSprite);
    }

    private void createFloor(int typeOfFloor, int x, int y)
    {
        Dirt floorEntity = new Dirt(x*tileWidth.get(), y*tileHeight.get());

        Image floorImg = (typeOfFloor == TileMaps.FLOOR_TOP) ? floorTopImg : (typeOfFloor == TileMaps.FLOOR_RIGHT) ? floorRightImg : floorLeftImg;
        floorEntity.setRect((int) floorImg.getWidth(), (int) floorImg.getHeight());
        display.getChildren().add(createImageView(floorEntity, floorImg));

        environment.getEntities().add(floorEntity);
    }

    private void errorTile(int tile) { System.out.println("Le tile '" + tile + "' n'est pas reconnu."); }
}
