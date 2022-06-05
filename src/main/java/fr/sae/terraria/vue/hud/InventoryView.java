package fr.sae.terraria.vue.hud;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.blocks.Dirt;
import fr.sae.terraria.modele.entities.blocks.Stone;
import fr.sae.terraria.modele.entities.blocks.TallGrass;
import fr.sae.terraria.modele.entities.blocks.Torch;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.modele.entities.items.Fiber;
import fr.sae.terraria.modele.entities.items.Meat;
import fr.sae.terraria.modele.entities.items.Wood;
import fr.sae.terraria.modele.entities.player.Inventory;
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

    private StringProperty[][] inventoryStringProperty;
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
    private void displayItemIntoInventoryBar() {
        int itemInventoryWidth = (int) (tileWidth / 1.5);
        int itemInventoryHeight = (int) (tileHeight / 1.5);

        for (int i = 0; i < Inventory.NB_LINES; i++)
            for (int j = 0; j < Inventory.NB_BOXES_MAX / Inventory.NB_LINES; j++)
                inventory.get()[i][j].addListener((ListChangeListener<? super StowableObjectType>) c -> {
                    while (c.next()) {
                        if (c.wasAdded()) {
                            for (int ii = 0; ii < Inventory.NB_LINES; ii++) {
                                for (int jj = 0; jj < Inventory.NB_BOXES_MAX / Inventory.NB_LINES; jj++) {
                                    inventoryStringProperty[inventory.getPosCursorVerticallyInventoryBar()][c.getAddedSize() - 1].set(String.valueOf(c.getAddedSize()));
                                    if (inventory.get()[ii][jj].equals(c.getList())) {
                                        ObservableList<StowableObjectType> item = inventory.get()[inventory.getPosCursorVerticallyInventoryBar()][jj];
                                        if (item.size() == 1) {
                                            Image itemImg = null;
                                            if (item.get(0) instanceof Dirt)
                                                itemImg = View.loadAnImage("tiles/floor-top.png", itemInventoryWidth, itemInventoryHeight);
                                            else if (item.get(0) instanceof Stone)
                                                itemImg = View.loadAnImage("tiles/rock-fill.png", itemInventoryWidth, itemInventoryHeight);
                                            else if (item.get(0) instanceof TallGrass)
                                                itemImg = View.loadAnImage("tiles/tall-grass.png", itemInventoryWidth, itemInventoryHeight);
                                            else if (item.get(0) instanceof Torch)
                                                itemImg = View.loadAnImage("tiles/torch.png", itemInventoryWidth, itemInventoryHeight);
                                            else if (item.get(0) instanceof Meat)
                                                itemImg = View.loadAnImage("loots/meat.png", itemInventoryWidth, itemInventoryHeight);
                                            else if (item.get(0) instanceof Fiber)
                                                itemImg = View.loadAnImage("loots/fiber.png", itemInventoryWidth, itemInventoryHeight);
                                            else if (item.get(0) instanceof Wood)
                                                itemImg = View.loadAnImage("loots/wood.png", itemInventoryWidth, itemInventoryHeight);
                                            else if (item.get(0) instanceof Pickaxe)
                                                itemImg = View.loadAnImage("tools/pickaxe.png", itemInventoryWidth, itemInventoryHeight);

                                            if (!Objects.isNull(itemImg)) {
                                                ImageView itemView = new ImageView();
                                                itemView.setImage(itemImg);
                                                itemView.setX(inventoryBarImgView.getX() + (((inventoryBarImgView.getImage().getWidth()/(Inventory.NB_BOXES_MAX/Inventory.NB_LINES)) - itemImg.getWidth())/2) + ((inventoryBarImgView.getImage().getWidth()/(Inventory.NB_BOXES_MAX/Inventory.NB_LINES)) * jj));
                                                itemView.setY(inventoryBarImgView.getY() + ((inventoryBarImgView.getImage().getHeight() - itemImg.getHeight()) / 2));

                                                display.getChildren().add(itemView);
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (c.wasRemoved())
                            inventoryStringProperty[inventory.getPosCursorVerticallyInventoryBar()][c.getRemovedSize() - 1].set(String.valueOf(c.getRemovedSize()));
                    }
                });
    }

    /**
     * Affiche la barre d'inventaire
     */
    private void displayInventoryBar() {
        this.inventoryBarImgView.setX(((windowWidth - inventoryBarImg.getWidth()) / 2));
        this.inventoryBarImgView.setY((windowHeight - inventoryBarImg.getHeight()) - tileHeight);

        display.getChildren().add(setFrameInventoryBar());
        display.getChildren().add(inventoryBarImgView);

        inventoryStringProperty = new StringProperty[inventory.NB_LINES][inventory.NB_BOXES_MAX / inventory.NB_LINES];
        for (int i = 0; i < inventory.NB_LINES; i++)
            for (int j = 0; j < inventory.NB_BOXES_MAX / inventory.NB_LINES; j++) {
                inventoryStringProperty[i][j] = new SimpleStringProperty();
                inventoryStringProperty[i][j].setValue(String.valueOf(inventory.get()[i][j].size()));
                System.out.println(inventory.get()[i][j].size());
                Label nbObjects = new Label();
                nbObjects.setId("textInventoryBar");
                int fontSize = (int) (5 * scaleMultiplicatorWidth);
                nbObjects.setFont(new Font("Arial", fontSize));
                nbObjects.setText("0");
                nbObjects.setLayoutX((int) inventoryBarImgView.getX() + (j * tileWidth));
                nbObjects.setLayoutY((int) (inventoryBarImgView.getY() + inventoryBarImg.getHeight() + (i * tileHeight)));
                nbObjects.translateXProperty().bind(new SimpleIntegerProperty((int) inventoryBarImgView.getX() + (j * tileWidth)));
                nbObjects.translateYProperty().bind(new SimpleIntegerProperty((int) (inventoryBarImgView.getY() + inventoryBarImg.getHeight() + (i * tileHeight))));
                nbObjects.textProperty().bind(inventoryStringProperty[i][j]);
                display.getChildren().add(nbObjects);
            }
    }

    /**
     * Affiche un carré qui se superpose sur la barre d'inventaire qui permet de savoir où on se situe
     */
    private void displayCursorInventoryBar() {
        this.cursorImgView.setX(((windowWidth - inventoryBarImg.getWidth()) / 2 - scaleMultiplicatorWidth));
        this.cursorImgView.setY(((windowHeight - inventoryBarImg.getHeight()) - tileHeight) - scaleMultiplicatorHeight);
        inventory.posCursorHorizontallyInventoryBarProperty().addListener((obs, oldV, newV) -> this.cursorImgView.setX(((windowWidth - inventoryBarImg.getWidth()) / 2 + ((inventoryBarImg.getWidth() / 9) * newV.intValue() - scaleMultiplicatorWidth))));

        display.getChildren().add(cursorImgView);
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
