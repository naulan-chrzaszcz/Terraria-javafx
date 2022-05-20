package fr.sae.terraria.vue;

import javafx.beans.property.IntegerProperty;

import javafx.geometry.Rectangle2D;
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
    private Image playerMoveRightImg;
    private Image playerMoveLeftImg;
    private Image playerIdleImg;


    public TileMapsView(Environment environment, Pane display, IntegerProperty tileWidth, IntegerProperty tileHeight)
    {
        this.display = display;
        this.environment = environment;

        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;

        this.floorTopImg = TileMapsView.loadAnImage("tiles/floor-top.png", tileWidth.get(), tileWidth.get());
        this.floorLeftImg = TileMapsView.loadAnImage("tiles/floor-left.png", tileWidth.get(), tileWidth.get());
        this.floorRightImg = TileMapsView.loadAnImage("tiles/floor-right.png", tileWidth.get(), tileWidth.get());
        this.stoneImg = TileMapsView.loadAnImage("tiles/rock-fill.png", tileWidth.get(), tileWidth.get());
        this.dirtImg = TileMapsView.loadAnImage("tiles/dirt-top.png", tileWidth.get(), tileWidth.get());
        this.playerIdleImg = TileMapsView.loadAnImage("sprites/player/player_idle.png", tileWidth.get(), tileWidth.get());
        this.playerMoveRightImg = TileMapsView.loadAnImage("sprites/player/player_moveRight.png", tileWidth.get()*4, tileWidth.get());
        this.playerMoveLeftImg = TileMapsView.loadAnImage("sprites/player/player_moveLeft.png", tileWidth.get()*4, tileWidth.get());
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
        ImageView playerImgView = new ImageView();
        playerImgView.translateYProperty().bind(environment.getPlayer().getYProperty());
        playerImgView.translateXProperty().bind(environment.getPlayer().getXProperty());

        environment.getPlayer().getAnimation().getFrame().addListener((obs, oldFrame, newFrame) -> {
            playerImgView.setViewport(new Rectangle2D(0, 0, tileHeight.get(), tileHeight.get()));
            if (environment.getPlayer().offset[0] == 0 && environment.getPlayer().offset[1] == 0)
                playerImgView.setImage(playerIdleImg);

            if (environment.getPlayer().offset[0] == 1 || environment.getPlayer().offset[0] == -1) {
                Rectangle2D frameRect = new Rectangle2D(newFrame.intValue() * tileHeight.get(), 0, tileHeight.get()-3, tileHeight.get());

                playerImgView.setViewport(frameRect);
                playerImgView.setImage((environment.getPlayer().offset[0] == -1) ? playerMoveLeftImg : playerMoveRightImg);
            }
        });
        display.getChildren().add(playerImgView);
    }

    public static Image loadAnImage(String path, int tileWidth, int tileHeight)
    {
        InputStream pathImg = Terraria.class.getResourceAsStream(path);
        if (pathImg == null) try {
            pathImg = new FileInputStream(Terraria.srcPath + path);
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }

        return new Image(pathImg, tileWidth, tileHeight, false, false);
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
        display.getChildren().add(createImageView(stoneEntity, stoneImg));
        environment.getEntities().add(stoneEntity);
    }

    private void createDirt(int x, int y)
    {
        Dirt dirtSprite = new Dirt(x*tileWidth.get(), y*tileHeight.get());
        display.getChildren().add(createImageView(dirtSprite, dirtImg));
        environment.getEntities().add(dirtSprite);
    }

    private void createFloor(int typeOfFloor, int x, int y)
    {
        Dirt floorEntity = new Dirt(x*tileWidth.get(), y*tileHeight.get());

        Image floorImg = (typeOfFloor == TileMaps.FLOOR_TOP) ? floorTopImg : (typeOfFloor == TileMaps.FLOOR_RIGHT) ? floorRightImg : floorLeftImg;
        display.getChildren().add(createImageView(floorEntity, floorImg));

        environment.getEntities().add(floorEntity);
    }

    private void errorTile(int tile) { System.out.println("Le tile '" + tile + "' n'est pas reconnu."); }
}
