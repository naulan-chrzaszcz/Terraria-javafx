package fr.sae.terraria.vue;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.Rabbit;
import fr.sae.terraria.modele.entities.Slime;
import fr.sae.terraria.modele.entities.blocks.*;
import fr.sae.terraria.modele.entities.entity.Entity;
import javafx.collections.ListChangeListener;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class TileMapsView
{
    private final Image torchImg;
    private final Image floorTopImg;
    private final Image floorLeftImg;
    private final Image floorRightImg;
    private final Image treeImg;
    private final Image stoneImg;
    private final Image dirtImg;
    private final Image tallGrassImg;

    private final Pane display;

    private final Environment environment;

    private int tileWidth;
    private int tileHeight;


    /**
     * @param environment Avoir des infos relatives à son environment
     * @param displayTileMap Affiche la carte tuilée
     * @param displayHostileBeings Affiche les êtres hostile (Animal, Joueur, ...)
     */
    public TileMapsView(Environment environment,
                        Pane displayTileMap,
                        Pane displayHostileBeings,
                        double scaleMultiplicatorWidth,
                        double scaleMultiplicatorHeight)
    {
        super();
        this.environment = environment;
        this.display = displayTileMap;

        this.tileHeight = (int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorHeight);
        this.tileWidth = (int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorWidth);

        this.floorTopImg = View.loadAnImage("tiles/floor-top.png", tileWidth, tileHeight);
        this.floorLeftImg = View.loadAnImage("tiles/floor-left.png", tileWidth, tileHeight);
        this.floorRightImg = View.loadAnImage("tiles/floor-right.png", tileWidth, tileHeight);
        this.stoneImg = View.loadAnImage("tiles/rock-fill.png", tileWidth, tileHeight);
        this.dirtImg = View.loadAnImage("tiles/dirt-top.png", tileWidth, tileHeight);
        this.torchImg = View.loadAnImage("tiles/torch.png", tileWidth, tileHeight);
        this.treeImg = View.loadAnImage("sprites/tree-sheet.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.tallGrassImg = View.loadAnImage("tiles/tall-grass.png",tileWidth,tileHeight);

        // Lorsqu'un élément est ajouté, il le dessine automatiquement à l'écran
        environment.getEntities().addListener((ListChangeListener<Entity>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    if (c.getList().get(0) instanceof Tree)
                        this.createTree((Tree) c.getList().get(0));
                    if (c.getList().get(0) instanceof TallGrass)
                        this.createTallGrass((TallGrass) c.getList().get(0));
                    if (c.getList().get(0) instanceof Rabbit) {
                        RabbitView rabbitView = new RabbitView((Rabbit) c.getList().get(0), scaleMultiplicatorWidth, scaleMultiplicatorHeight);
                        rabbitView.displayRabbit(displayHostileBeings);
                    }
                    if (c.getList().get(0) instanceof Slime) {
                        SlimeView slimeView = new SlimeView((Slime) c.getList().get(0), scaleMultiplicatorWidth, scaleMultiplicatorHeight);
                        slimeView.displaySlime(displayHostileBeings);
                    }

                    if (c.getList().get(0) instanceof Torch)
                        createTorch((Torch) c.getList().get(0));
                    if (c.getList().get(0) instanceof Dirt)
                        displayTileMap.getChildren().add(View.createImageView(c.getList().get(0), floorTopImg));
                    if (c.getList().get(0) instanceof Stone)
                        displayTileMap.getChildren().add(View.createImageView(c.getList().get(0), stoneImg));
                }

                if (c.wasRemoved()) {
                    // La position de l'entité
                    int xEntity = (int) (c.getRemoved().get(0).getRect().get().getMinX());
                    int yEntity = (int) (c.getRemoved().get(0).getRect().get().getMinY());

                    // Tant qu'on n'a pas trouvé l'objet sur le Pane, il continue la boucle.
                    Node nodeAtDelete = null; int i = 0;
                    do {
                        Node node = display.getChildren().get(i);
                        int xNode = (int) (node.getTranslateX());
                        int yNode = (int) (node.getTranslateY());

                        if (xNode == xEntity && yNode == yEntity) {
                            nodeAtDelete = node;
                            display.getChildren().remove(nodeAtDelete);
                        }
                        i++;
                    } while (i < display.getChildren().size() && Objects.isNull(nodeAtDelete));
                }
            }
        });
    }

    /** Decompose la carte pour afficher un à un les tiles à l'écran */
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

    /** Crée et affiche un block de type arbre, il sera +/- grand */
    private void createTree(final Tree tree)
    {
        List<ImageView> imagesTree = new ArrayList<>();

        int nbFoliage = ((int) (Math.random()*2))+1;
        int nbTrunk = ((int) (Math.random()*3))+1;

        Rectangle2D viewportFirstFrame = new Rectangle2D(0, 0, this.tileWidth, this.tileHeight);
        ImageView firstFrameView = new ImageView();
        firstFrameView.setImage(this.treeImg);
        firstFrameView.setViewport(viewportFirstFrame);
        imagesTree.add(firstFrameView);
        for (int f = 0; f < nbFoliage; f++) {
            Rectangle2D viewportSecondFrame = new Rectangle2D(0, this.tileHeight, this.tileWidth, this.tileHeight);
            ImageView secondFrameView = new ImageView();
            secondFrameView.setImage(this.treeImg);
            secondFrameView.setViewport(viewportSecondFrame);
            imagesTree.add(secondFrameView);
        }

        if (nbTrunk > 1) {
            Rectangle2D viewportEndTrunk = new Rectangle2D(0, (this.tileHeight*2), this.tileWidth, this.tileHeight);
            ImageView endTrunkView = new ImageView();
            endTrunkView.setImage(this.treeImg);
            endTrunkView.setViewport(viewportEndTrunk);
            imagesTree.add(endTrunkView);

            for (int t = 0; t < nbTrunk-1; t++) {
                Rectangle2D viewportTrunk = new Rectangle2D(this.tileWidth, (this.tileHeight*2), this.tileWidth, this.tileHeight);
                ImageView trunkView = new ImageView();
                trunkView.setImage(this.treeImg);
                trunkView.setViewport(viewportTrunk);
                imagesTree.add(trunkView);
            }
        } else {
            Rectangle2D viewportTrunk = new Rectangle2D(0, (this.tileHeight*2), this.tileWidth, this.tileHeight);
            ImageView trunkView = new ImageView();
            trunkView.setImage(this.treeImg);
            trunkView.setViewport(viewportTrunk);
            imagesTree.add(trunkView);
        }
        Rectangle2D viewportTrunkFoot = new Rectangle2D(0, (this.tileHeight*3), this.tileWidth, this.tileHeight);
        ImageView trunkFootView = new ImageView();
        trunkFootView.setImage(this.treeImg);
        trunkFootView.setViewport(viewportTrunkFoot);
        imagesTree.add(trunkFootView);

        for (int i = 0; i < imagesTree.size(); i++) {
            ImageView treeView = imagesTree.get(i);

            treeView.setY((int) (((tree.getY() + this.tileHeight) + (i * this.tileHeight)) - (this.tileHeight * imagesTree.size())));
            treeView.setX((int) tree.getX());

            this.display.getChildren().add(treeView);
        }

        tree.setRect(this.tileWidth, (2+nbFoliage+nbTrunk)*this.tileHeight);
    }

    /**
     * Crée et affiche un block de type Haute Herbe.
     *  Applique une animation de pousse à la haute herbe
     */
    private void createTallGrass(final TallGrass tallGrass)
    {
        ImageView tallGrassView = new ImageView(this.tallGrassImg);
        tallGrass.setRect(this.tileWidth, this.tileHeight);
        tallGrassView.setX(tallGrass.getX());
        tallGrassView.setY(tallGrass.getY());

        // L'animation de la pousse de la haute herbe
        tallGrass.getTallGrassGrowthProperty().addListener(((observable, oldValue, newValue) -> {
            tallGrassView.setViewport(new Rectangle2D(0, 0, this.tallGrassImg.getWidth(), (newValue.intValue() < 1) ? 1 : (this.tallGrassImg.getHeight()/TallGrass.GROWTH_TALL_GRASS_STEP)*newValue.intValue()));
            tallGrassView.setY((tallGrass.getY() - (this.tallGrassImg.getHeight()/TallGrass.GROWTH_TALL_GRASS_STEP)*newValue.intValue()) + this.tileHeight);
        }));

        this.display.getChildren().add(tallGrassView);
    }

    /** Crée et affiche un tile de type pierre */
    private void createStone(int x, int y)
    {
        Stone stoneEntity = new Stone(this.environment, x*this.tileWidth, y*this.tileHeight);
        stoneEntity.setRect(this.tileWidth, this.tileHeight);

        this.display.getChildren().add(View.createImageView(stoneEntity, this.stoneImg));
        this.environment.getEntities().add(stoneEntity);
    }

    /** Crée et affiche un block de type Torch */
    private void createTorch(final Torch torch)
    {
        torch.setRect(this.tileWidth, this.tileHeight);
        this.display.getChildren().add(View.createImageView(torch, this.torchImg));
    }

    /** Crée et affiche un tile de type terre */
    private void createDirt(int x, int y)
    {
        Dirt dirtSprite = new Dirt(this.environment, x*this.tileWidth, y*this.tileHeight);
        dirtSprite.setRect(this.tileWidth, this.tileHeight);

        this.display.getChildren().add(View.createImageView(dirtSprite, this.dirtImg));
        this.environment.getEntities().add(dirtSprite);
    }

    /**
     * Crée et affiche un tile de type sol
     * @param typeOfFloor Quel face du tile doit être affiché
     */
    private void createFloor(int typeOfFloor, int x, int y)
    {
        Dirt floorEntity = new Dirt(this.environment, x*this.tileWidth, y*this.tileHeight);
        Image floorImg = (typeOfFloor == TileMaps.FLOOR_TOP) ? this.floorTopImg : (typeOfFloor == TileMaps.FLOOR_RIGHT) ? this.floorRightImg : this.floorLeftImg;
        floorEntity.setRect(this.tileWidth, this.tileHeight);

        this.display.getChildren().add(View.createImageView(floorEntity, floorImg));
        this.environment.getEntities().add(floorEntity);
    }

    /** Affiche une erreur au cas où si un developer a fait une erreur lors de la saisie d'un tile sur le fichier .json */
    private void errorTile(final int tile) { if (tile != TileMaps.SKY) System.out.println("Le tile '" + tile + "' n'est pas reconnu."); }
}
