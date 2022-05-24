package fr.sae.terraria.vue;

import javafx.collections.ListChangeListener;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.blocks.Dirt;
import fr.sae.terraria.modele.blocks.Stone;
import fr.sae.terraria.modele.blocks.Tree;
import fr.sae.terraria.modele.entities.entity.Entity;

import java.util.ArrayList;
import java.util.List;


public class TileMapsView
{
    private Pane display;

    private Environment environment;

    private double scaleMultiplicatorWidth;
    private double scaleMultiplicatorHeight;
    private int tileWidth;
    private int tileHeight;

    private Image floorTopImg;
    private Image floorLeftImg;
    private Image floorRightImg;
    private Image treeImg;
    private Image stoneImg;
    private Image dirtImg;


    public TileMapsView(Environment environment, Pane display, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.environment = environment;
        this.display = display;
        this.scaleMultiplicatorHeight = scaleMultiplicatorHeight;
        this.scaleMultiplicatorWidth = scaleMultiplicatorWidth;

        this.tileHeight = (int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorHeight);
        this.tileWidth = (int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorWidth);

        this.floorTopImg = View.loadAnImage("tiles/floor-top.png", tileWidth, tileHeight);
        this.floorLeftImg = View.loadAnImage("tiles/floor-left.png", tileWidth, tileHeight);
        this.floorRightImg = View.loadAnImage("tiles/floor-right.png", tileWidth, tileHeight);
        this.stoneImg = View.loadAnImage("tiles/rock-fill.png", tileWidth, tileHeight);
        this.dirtImg = View.loadAnImage("tiles/dirt-top.png", tileWidth, tileHeight);
        this.treeImg = View.loadAnImage("sprites/tree-sheet.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);

        environment.getEntities().addListener(new ListChangeListener<Entity>() {
            @Override
            public void onChanged(Change<? extends Entity> c) {
                while (c.next())
                    if (c.getList().get(0) instanceof Tree)
                        createTree((Tree) c.getList().get(0));
            }
        });
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

    private void createTree(Tree tree)
    {
        List<ImageView> imagesTree = new ArrayList<>();

        int nbFoliage = ((int) (Math.random()*2))+1;
        int nbTrunk = ((int) (Math.random()*3))+1;

        Rectangle2D viewportFirstFrame = new Rectangle2D(0, 0, tileWidth, tileHeight);
        ImageView firstFrameView = new ImageView();
        firstFrameView.setImage(treeImg);
        firstFrameView.setViewport(viewportFirstFrame);
        imagesTree.add(firstFrameView);
        for (int f = 0; f < nbFoliage; f++) {
            Rectangle2D viewportSecondFrame = new Rectangle2D(0, tileHeight, tileWidth, tileHeight);
            ImageView secondFrameView = new ImageView();
            secondFrameView.setImage(treeImg);
            secondFrameView.setViewport(viewportSecondFrame);
            imagesTree.add(secondFrameView);
        }

        if (nbTrunk > 1) {
            Rectangle2D viewportEndTrunk = new Rectangle2D(0, (tileHeight*2), tileWidth, tileHeight);
            ImageView endTrunkView = new ImageView();
            endTrunkView.setImage(treeImg);
            endTrunkView.setViewport(viewportEndTrunk);
            imagesTree.add(endTrunkView);

            for (int t = 0; t < nbTrunk-1; t++) {
                Rectangle2D viewportTrunk = new Rectangle2D(tileWidth, (tileHeight*2), tileWidth, tileHeight);
                ImageView trunkView = new ImageView();
                trunkView.setImage(treeImg);
                trunkView.setViewport(viewportTrunk);
                imagesTree.add(trunkView);
            }
        } else {
            Rectangle2D viewportTrunk = new Rectangle2D(0, (tileHeight*2), tileWidth, tileHeight);
            ImageView trunkView = new ImageView();
            trunkView.setImage(treeImg);
            trunkView.setViewport(viewportTrunk);
            imagesTree.add(trunkView);
        }
        Rectangle2D viewportTrunkFoot = new Rectangle2D(0, (tileHeight*3), tileWidth, tileHeight);
        ImageView trunkFootView = new ImageView();
        trunkFootView.setImage(treeImg);
        trunkFootView.setViewport(viewportTrunkFoot);
        imagesTree.add(trunkFootView);

        for (int i = 0; i < imagesTree.size(); i++) {
            ImageView treeView = imagesTree.get(i);

            treeView.setY((int) ((tree.getY() + (i * tileHeight)) - (tileHeight * imagesTree.size())));
            treeView.setX((int) tree.getX());
            display.getChildren().add(treeView);
        }
    }

    private void createStone(int x, int y)
    {
        Stone stoneEntity = new Stone(x*tileWidth, y*tileHeight);
        display.getChildren().add(View.createImageView(stoneEntity, stoneImg));
        environment.getEntities().add(stoneEntity);
    }

    private void createDirt(int x, int y)
    {
        Dirt dirtSprite = new Dirt(x*tileWidth, y*tileHeight);
        display.getChildren().add(View.createImageView(dirtSprite, dirtImg));
        environment.getEntities().add(dirtSprite);
    }

    private void createFloor(int typeOfFloor, int x, int y)
    {
        Dirt floorEntity = new Dirt(x*tileWidth, y*tileHeight);

        Image floorImg = (typeOfFloor == TileMaps.FLOOR_TOP) ? floorTopImg : (typeOfFloor == TileMaps.FLOOR_RIGHT) ? floorRightImg : floorLeftImg;
        display.getChildren().add(View.createImageView(floorEntity, floorImg));

        environment.getEntities().add(floorEntity);
    }

    private void errorTile(int tile) { if (tile != TileMaps.SKY) System.out.println("Le tile '" + tile + "' n'est pas reconnu."); }
}
