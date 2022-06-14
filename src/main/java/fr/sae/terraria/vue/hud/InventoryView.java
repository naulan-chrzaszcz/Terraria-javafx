package fr.sae.terraria.vue.hud;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.Arrow;
import fr.sae.terraria.modele.entities.blocks.Block;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.modele.entities.items.Item;
import fr.sae.terraria.modele.entities.items.Meat;
import fr.sae.terraria.modele.entities.items.Vodka;
import fr.sae.terraria.modele.entities.player.inventory.Inventory;
import fr.sae.terraria.modele.entities.player.inventory.Stack;
import fr.sae.terraria.modele.entities.tools.Tool;
import fr.sae.terraria.vue.View;
import javafx.collections.ListChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class InventoryView
{
    private final ImageView inventoryBarImgView;
    private final ImageView cursorImgView;

    private final Image inventoryBarImg;

    private final Rectangle frameInventoryBar;
    private final Inventory inventory;
    private final Pane display;

    private final List<ImageView> itemsView;
    private final List<Text> texts;

    private double scaleMultiplicatorWidth;
    private double scaleMultiplicatorHeight;
    private double windowWidth;
    private double windowHeight;
    private int tileWidth;
    private int tileHeight;


    public InventoryView(Inventory inventory, Pane display, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.inventory = inventory;
        this.display = display;
        this.scaleMultiplicatorHeight = scaleMultiplicatorHeight;
        this.scaleMultiplicatorWidth = scaleMultiplicatorWidth;

        this.itemsView = new ArrayList<>();
        this.texts = new ArrayList<>();

        this.windowWidth = (scaleMultiplicatorWidth * Terraria.DISPLAY_RENDERING_WIDTH);
        this.windowHeight = (scaleMultiplicatorHeight * Terraria.DISPLAY_RENDERING_HEIGHT);
        this.tileWidth = (int) (scaleMultiplicatorWidth * TileMaps.TILE_DEFAULT_SIZE);
        this.tileHeight = (int) (scaleMultiplicatorHeight * TileMaps.TILE_DEFAULT_SIZE);

        this.frameInventoryBar = new Rectangle();
        this.inventoryBarImg = View.loadAnImage("inventoryBar.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.inventoryBarImgView = new ImageView(inventoryBarImg);
        Image cursorImg = View.loadAnImage("cursor.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.cursorImgView = new ImageView(cursorImg);
    }

    /**
     * Applique une bordure de couleur noire autour de la barre d'inventaire
     */
    private Rectangle setFrameInventoryBar() {
        frameInventoryBar.setWidth(inventoryBarImg.getWidth() + (2 * scaleMultiplicatorWidth));
        frameInventoryBar.setHeight(inventoryBarImg.getHeight() + (2 * scaleMultiplicatorHeight));
        frameInventoryBar.setX(inventoryBarImgView.getX() - scaleMultiplicatorWidth);
        frameInventoryBar.setY(inventoryBarImgView.getY() - scaleMultiplicatorHeight);

        return frameInventoryBar;
    }

    /**
     * Affiche ou supprime les items qui rentrent ou sort de la barre d'inventaire
     */
    private void displayItemIntoInventoryBar()
    {
        int boxeInventoryWidth = (int) (this.inventoryBarImg.getWidth()/(Inventory.NB_BOXES_MAX/Inventory.NB_LINES));
        int itemInventoryWidth = (int) (tileWidth / 1.5);
        int itemInventoryHeight = (int) (tileHeight / 1.5);

        this.inventory.get().addListener((ListChangeListener<? super Stack>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    ImageView view = new ImageView();
                    Stack stack = c.getAddedSubList().get(0);
                    StowableObjectType item = stack.getItem();

                    if (item instanceof Meat)
                        view.setImage(View.loadAnImage("loots/meat.png", itemInventoryWidth, itemInventoryHeight));
                    else if (item instanceof Arrow)
                        view.setImage(null);    // TODO
                    else if (item instanceof Vodka)
                        view.setImage(View.loadAnImage("loots/vodka.png", itemInventoryWidth,itemInventoryHeight));

                    if (item instanceof Block) {
                        if (Block.isFloorTop((Block) item))
                            view.setImage(View.loadAnImage("tiles/floor-top.png", itemInventoryWidth, itemInventoryHeight));
                        else if (Block.isRock((Block) item))
                            view.setImage(View.loadAnImage("tiles/rock-fill.png", itemInventoryWidth, itemInventoryHeight));
                        else if (Block.isTallGrass((Block) item))
                            view.setImage(View.loadAnImage("tiles/tall-grass.png.png", itemInventoryWidth, itemInventoryHeight));
                        else if (Block.isTorch((Block) item))
                            view.setImage(View.loadAnImage("tiles/torch.png", itemInventoryWidth, itemInventoryHeight));
                    }

                    if (item instanceof Tool) {
                        if (Tool.isAxe((Tool) item))
                            view.setImage(null);    // TODO
                        else if (Tool.isBow((Tool) item))
                            view.setImage(null);    // TODO
                        else if (Tool.isPickaxe((Tool) item))
                            view.setImage(View.loadAnImage("tools/pickaxe.png", itemInventoryWidth, itemInventoryHeight));
                        else if (Tool.isSword((Tool) item))
                            view.setImage(View.loadAnImage("tools/sword.png", itemInventoryWidth, itemInventoryHeight));
                    }

                    if (item instanceof Item) {
                        if (Item.isCoal(item))
                            view.setImage(View.loadAnImage("loots/coal.png", itemInventoryWidth, itemInventoryHeight));
                        else if (Item.isFiber(item))
                            view.setImage(View.loadAnImage("loots/fiber.png", itemInventoryWidth, itemInventoryHeight));
                        else if (Item.isIron(item))
                            view.setImage(View.loadAnImage("loots/iron.png", itemInventoryWidth, itemInventoryHeight));
                        else if (Item.isStone(item))
                            view.setImage(View.loadAnImage("loots/pierre.png", itemInventoryWidth, itemInventoryHeight));
                        else if (Item.isSilex(item))
                            view.setImage(View.loadAnImage("loots/silex.png", itemInventoryWidth, itemInventoryHeight));
                        else if (Item.isWood(item))
                            view.setImage(View.loadAnImage("loots/wood.png", itemInventoryWidth, itemInventoryHeight));
                    }

                    // Actualise le nombre d'item à l'écran
                    this.texts.get(c.getTo()-1).textProperty().bind(c.getAddedSubList().get(0).nbItemsProperty().asString());

                    if (!Objects.isNull(view.getImage())) {
                        view.setX((this.inventoryBarImgView.getX() + ((c.getTo()-1) * boxeInventoryWidth)) + ((boxeInventoryWidth/2) - (itemInventoryWidth/2)));
                        view.setY(this.inventoryBarImgView.getY() + ((boxeInventoryWidth/2) - (itemInventoryHeight/2)));

                        this.itemsView.add(view);
                        this.display.getChildren().add(view);
                    }
                }

                if (c.wasRemoved()) {
                    this.display.getChildren().remove(this.itemsView.get(c.getTo()));
                    this.itemsView.remove(c.getTo());

                    for (int i = c.getTo(); i < itemsView.size(); i++)
                        this.itemsView.get(i).setX(this.itemsView.get(i).getX() - boxeInventoryWidth);

                    for (int i = 0; i < inventory.get().size(); i ++){
                        this.texts.get(inventory.get().size()).textProperty().unbind();
                        this.texts.get(i).textProperty().bind(inventory.get().get(i).nbItemsProperty().asString());
                    }
                    this.texts.get(inventory.get().size()).textProperty().unbind();
                    this.texts.get(inventory.get().size()).setText("0");

                }
            }
        });
    }

    /**
     * Affiche la barre d'inventaire
     */
    private void displayInventoryBar()
    {
        this.inventoryBarImgView.setX(((this.windowWidth - this.inventoryBarImg.getWidth()) / 2));
        this.inventoryBarImgView.setY((this.windowHeight - this.inventoryBarImg.getHeight()) - this.tileHeight);

        this.display.getChildren().add(setFrameInventoryBar());
        this.display.getChildren().add(this.inventoryBarImgView);
    }

    /**
     * Affiche un carré qui se superpose sur la barre d'inventaire qui permet de savoir où on se situe
     */
    private void displayCursorInventoryBar()
    {
        this.cursorImgView.setX(((windowWidth - inventoryBarImg.getWidth()) / 2 - scaleMultiplicatorWidth));
        this.cursorImgView.setY(((windowHeight - inventoryBarImg.getHeight()) - tileHeight) - scaleMultiplicatorHeight);
        this.inventory.cursorProperty().addListener((obs, oldV, newV) -> this.cursorImgView.setX(((windowWidth - inventoryBarImg.getWidth()) / 2 + ((inventoryBarImg.getWidth() / 9) * newV.intValue() - scaleMultiplicatorWidth))));

        this.display.getChildren().add(cursorImgView);
    }

    public void display()
    {
        this.displayInventoryBar();
        this.displayCursorInventoryBar();

        // Range et affiche tous les compteurs d'item de l'inventaire
        int boxeInventoryWidth = (int) (this.inventoryBarImg.getWidth()/(Inventory.NB_BOXES_MAX/Inventory.NB_LINES));
        for (int i = 0; i < (Inventory.NB_BOXES_MAX/Inventory.NB_LINES); i++) {
            Text text = new Text("0");
            text.setX(this.inventoryBarImgView.getX() + (boxeInventoryWidth*i));
            text.setY(this.inventoryBarImgView.getY() + boxeInventoryWidth);
            text.setFont(new Font("Arial", 5*scaleMultiplicatorWidth));
            text.setStroke(Color.WHITE);    // TODO Bizarre cette histoire de bordure qui fait la couleur final du Font
            text.setStrokeWidth(1);

            this.texts.add(text);
            this.display.getChildren().add(text);
        }

        this.displayItemIntoInventoryBar();
    }


    public double getX() { return this.inventoryBarImgView.getX(); }

    public double getY() { return this.inventoryBarImgView.getY(); }

    public Image getInventoryBarImg() { return inventoryBarImg; }
}
