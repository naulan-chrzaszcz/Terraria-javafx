package fr.sae.terraria.vue.hud;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.blocks.Dirt;
import fr.sae.terraria.modele.entities.blocks.Stone;
import fr.sae.terraria.modele.entities.blocks.TallGrass;
import fr.sae.terraria.modele.entities.blocks.Torch;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.modele.entities.items.Fiber;
import fr.sae.terraria.modele.entities.items.Meat;
import fr.sae.terraria.modele.entities.items.Wood;
import fr.sae.terraria.modele.entities.player.inventory.Inventory;
import fr.sae.terraria.modele.entities.player.inventory.Stack;
import fr.sae.terraria.modele.entities.tools.Pickaxe;
import fr.sae.terraria.vue.View;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.Objects;


public class InventoryView {
    private final ImageView inventoryBarImgView;
    private final ImageView cursorImgView;

    private final Image inventoryBarImg;

    private final Rectangle frameInventoryBar;
    private final Inventory inventory;
    private final Pane display;

    private double scaleMultiplicatorWidth;
    private double scaleMultiplicatorHeight;
    private double windowWidth;
    private double windowHeight;
    private int tileWidth;
    private int tileHeight;


    public InventoryView(Inventory inventory, Pane display, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight) {
        this.inventory = inventory;
        this.display = display;
        this.scaleMultiplicatorHeight = scaleMultiplicatorHeight;
        this.scaleMultiplicatorWidth = scaleMultiplicatorWidth;

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
        int itemInventoryWidth = (int) (tileWidth / 1.5);
        int itemInventoryHeight = (int) (tileHeight / 1.5);

        this.inventory.get().addListener((ListChangeListener<? super Stack>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    Stack stack = c.getAddedSubList().get(0);
                    StowableObjectType item = stack.getItem();

                    if (item instanceof Dirt) {
                        ImageView dirtView = new ImageView();
                        dirtView.setImage(View.loadAnImage("tiles/floor-top.png", itemInventoryWidth, itemInventoryHeight));
                        int boxeInventoryWidth = (int) (this.inventoryBarImg.getWidth()/(Inventory.NB_BOXES_MAX/Inventory.NB_LINES));
                        System.out.println(boxeInventoryWidth);
                        dirtView.setX((this.inventoryBarImgView.getX() + ((c.getTo()-1) * boxeInventoryWidth)) + ((boxeInventoryWidth/2) - itemInventoryWidth));
                        dirtView.setY(this.inventoryBarImgView.getY() + ((this.inventoryBarImg.getHeight()/2) - itemInventoryHeight));
                        this.display.getChildren().add(dirtView);
                    }
                    System.out.println(item);
                }
            }
        });
    }

    /**
     * Affiche la barre d'inventaire
     */
    private void displayInventoryBar() {
        this.inventoryBarImgView.setX(((this.windowWidth - this.inventoryBarImg.getWidth()) / 2));
        this.inventoryBarImgView.setY((this.windowHeight - this.inventoryBarImg.getHeight()) - this.tileHeight);

        this.display.getChildren().add(setFrameInventoryBar());
        this.display.getChildren().add(this.inventoryBarImgView);
    }

    /**
     * Affiche un carré qui se superpose sur la barre d'inventaire qui permet de savoir où on se situe
     */
    private void displayCursorInventoryBar() {
        this.cursorImgView.setX(((windowWidth - inventoryBarImg.getWidth()) / 2 - scaleMultiplicatorWidth));
        this.cursorImgView.setY(((windowHeight - inventoryBarImg.getHeight()) - tileHeight) - scaleMultiplicatorHeight);
        this.inventory.posCursorProperty().addListener((obs, oldV, newV) -> this.cursorImgView.setX(((windowWidth - inventoryBarImg.getWidth()) / 2 + ((inventoryBarImg.getWidth() / 9) * newV.intValue() - scaleMultiplicatorWidth))));

        this.display.getChildren().add(cursorImgView);
    }

    public void display() {
        this.displayInventoryBar();
        this.displayItemIntoInventoryBar();
        this.displayCursorInventoryBar();
    }


    public double getX() {
        return this.inventoryBarImgView.getX();
    }

    public double getY() {
        return this.inventoryBarImgView.getY();
    }

    public Image getInventoryBarImg() {
        return inventoryBarImg;
    }
}
