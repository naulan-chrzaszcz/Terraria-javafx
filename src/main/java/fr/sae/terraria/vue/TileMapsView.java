package fr.sae.terraria.vue;

import javafx.beans.property.IntegerProperty;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.blocks.Dirt;
import fr.sae.terraria.modele.blocks.Stone;


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


    public TileMapsView(Environment environment, Pane display, IntegerProperty tileWidth, IntegerProperty tileHeight)
    {
        this.display = display;
        this.environment = environment;

        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;

        this.floorTopImg = View.loadAnImage("tiles/floor-top.png", tileWidth.get(), tileWidth.get());
        this.floorLeftImg = View.loadAnImage("tiles/floor-left.png", tileWidth.get(), tileWidth.get());
        this.floorRightImg = View.loadAnImage("tiles/floor-right.png", tileWidth.get(), tileWidth.get());
        this.stoneImg = View.loadAnImage("tiles/rock-fill.png", tileWidth.get(), tileWidth.get());
        this.dirtImg = View.loadAnImage("tiles/dirt-top.png", tileWidth.get(), tileWidth.get());
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

    private void createStone(int x, int y)
    {
        Stone stoneEntity = new Stone(x*tileWidth.get(), y*tileHeight.get());
        display.getChildren().add(View.createImageView(stoneEntity, stoneImg));
        environment.getEntities().add(stoneEntity);
    }

    private void createDirt(int x, int y)
    {
        Dirt dirtSprite = new Dirt(x*tileWidth.get(), y*tileHeight.get());
        display.getChildren().add(View.createImageView(dirtSprite, dirtImg));
        environment.getEntities().add(dirtSprite);
    }

    private void createFloor(int typeOfFloor, int x, int y)
    {
        Dirt floorEntity = new Dirt(x*tileWidth.get(), y*tileHeight.get());

        Image floorImg = (typeOfFloor == TileMaps.FLOOR_TOP) ? floorTopImg : (typeOfFloor == TileMaps.FLOOR_RIGHT) ? floorRightImg : floorLeftImg;
        display.getChildren().add(View.createImageView(floorEntity, floorImg));

        environment.getEntities().add(floorEntity);
    }

    private void errorTile(int tile) { if (tile != 0) System.out.println("Le tile '" + tile + "' n'est pas reconnu."); }
}
