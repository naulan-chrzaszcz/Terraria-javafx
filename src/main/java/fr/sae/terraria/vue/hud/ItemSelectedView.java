package fr.sae.terraria.vue.hud;

import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.Arrow;
import fr.sae.terraria.modele.entities.blocks.Dirt;
import fr.sae.terraria.modele.entities.blocks.Stone;
import fr.sae.terraria.modele.entities.blocks.TallGrass;
import fr.sae.terraria.modele.entities.blocks.Torch;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.modele.entities.items.*;
import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.modele.entities.player.inventory.Stack;
import fr.sae.terraria.modele.entities.tools.Axe;
import fr.sae.terraria.modele.entities.tools.Bow;
import fr.sae.terraria.modele.entities.tools.Pickaxe;
import fr.sae.terraria.modele.entities.tools.Sword;
import fr.sae.terraria.vue.View;
import javafx.collections.ListChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.Objects;


public class ItemSelectedView
{
    private final ImageView itemSelectedImgView;

    private final Image stoneItemImg;
    private final Image dirtItemImg;
    private final Image coalItemImg;
    private final Image fibreItemImg;
    private final Image ironItemImg;
    private final Image pierreItemImg;
    private final Image silexItemImg;
    private final Image meatItemImg;
    private final Image woodItemImg;
    private final Image torchItemImg;
    private final Image pickaxeItemImg;
    private final Image vodkaItemImg;

    private final Pane display;


    public ItemSelectedView(Pane display, Player player, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.display = display;

        int widthTile = (int) (scaleMultiplicatorWidth * TileMaps.TILE_DEFAULT_SIZE);
        int heightTile = (int) (scaleMultiplicatorHeight * TileMaps.TILE_DEFAULT_SIZE);
        int widthItem = widthTile/2;
        int heightItem = heightTile/2;

        this.itemSelectedImgView = new ImageView();
        this.vodkaItemImg = View.loadAnImage("loots/vodka.png",widthItem,heightItem);
        this.dirtItemImg = View.loadAnImage("tiles/floor-top.png", widthItem, heightItem);
        this.stoneItemImg = View.loadAnImage("tiles/rock-fill.png", widthItem, heightItem);
        this.torchItemImg = View.loadAnImage("tiles/torch.png", widthItem, heightItem);
        this.coalItemImg = View.loadAnImage("loots/coal.png", widthItem, heightItem);
        this.fibreItemImg = View.loadAnImage("loots/fiber.png", widthItem, heightItem);
        this.ironItemImg = View.loadAnImage("loots/iron.png", widthItem, heightItem);
        this.pierreItemImg = View.loadAnImage("loots/pierre.png", widthItem, heightItem);
        this.silexItemImg = View.loadAnImage("loots/silex.png", widthItem, heightItem);
        this.meatItemImg = View.loadAnImage("loots/meat.png", widthItem, heightItem);
        this.woodItemImg = View.loadAnImage("loots/wood.png", widthItem, heightItem);
        this.pickaxeItemImg = View.loadAnImage("tools/pickaxe.png", widthItem, heightItem);

        // Reset
        player.getInventory().get().addListener((ListChangeListener<? super Stack>) c -> {
            while (c.next()) {
                if (c.wasRemoved())
                    this.itemSelectedImgView.setImage(null);
            }
        });

        player.getInventory().posCursorProperty().addListener((obs, oldItemSelected, newItemSelected) -> {
            this.itemSelectedImgView.setImage(null);
            if (!Objects.isNull(player.getStackSelected())) {
                StowableObjectType item = player.getStackSelected().getItem();

                if (item instanceof Dirt)
                    this.itemSelectedImgView.setImage(this.dirtItemImg);
                else if (item instanceof Stone)
                    this.itemSelectedImgView.setImage(this.stoneItemImg);
                else if (item instanceof TallGrass)
                    this.itemSelectedImgView.setImage(null);
                else if (item instanceof Torch)
                    this.itemSelectedImgView.setImage(this.torchItemImg);
                else if (item instanceof Coal)
                    this.itemSelectedImgView.setImage(this.coalItemImg);
                else if (item instanceof Fiber)
                    this.itemSelectedImgView.setImage(this.fibreItemImg);
                else if (item instanceof Iron)
                    this.itemSelectedImgView.setImage(this.ironItemImg);
                else if (item instanceof Meat)
                    this.itemSelectedImgView.setImage(this.meatItemImg);
                else if (item instanceof Vodka)
                    this.itemSelectedImgView.setImage(this.vodkaItemImg);
                else if (item instanceof Pierre)
                    this.itemSelectedImgView.setImage(this.pierreItemImg);
                else if (item instanceof Silex)
                    this.itemSelectedImgView.setImage(this.silexItemImg);
                else if (item instanceof Wood)
                    this.itemSelectedImgView.setImage(this.woodItemImg);
                else if (item instanceof Axe)
                    this.itemSelectedImgView.setImage(null);
                else if (item instanceof Bow)
                    this.itemSelectedImgView.setImage(null);
                else if (item instanceof Pickaxe)
                    this.itemSelectedImgView.setImage(this.pickaxeItemImg);
                else if (item instanceof Sword)
                    this.itemSelectedImgView.setImage(null);
                else if (item instanceof Arrow)
                    this.itemSelectedImgView.setImage(null);
            }
        });

        display.addEventFilter(MouseEvent.MOUSE_MOVED, mouse -> {
            itemSelectedImgView.setX(mouse.getX());
            itemSelectedImgView.setY(mouse.getY());
        });
    }

    public void display() { display.getChildren().add(itemSelectedImgView); }
}
