package fr.sae.terraria.controller;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.Player;
import fr.sae.terraria.modele.entities.blocks.Dirt;
import fr.sae.terraria.modele.entities.blocks.Stone;
import fr.sae.terraria.modele.entities.blocks.TallGrass;
import fr.sae.terraria.modele.entities.blocks.Torch;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.vue.View;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class MenuController implements Initializable
{
    @FXML
    private Pane displayInventory;
    @FXML
    private AnchorPane displayLexique;
    @FXML
    private Pane root;
    @FXML
    private Pane title;

    public Timeline loop;
    private Stage stage;
    public Player player = null;
    public double scaleMultiplicatorWidth;
    public double scaleMultiplicatorHeight;


    public MenuController(Stage stage) {
        this.stage = stage;
    }

    public void initialize(URL location, ResourceBundle resources)
    {
        scaleMultiplicatorWidth = (root.getPrefWidth() / Terraria.DISPLAY_RENDERING_WIDTH);
        scaleMultiplicatorHeight = ((root.getPrefHeight()-title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT);


        this.loop = new Timeline();
        this.loop.setCycleCount(Animation.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(Terraria.TARGET_FPS), (ev -> {
            if (!Objects.isNull(player)) {
                displayItemIntoInventoryBar();
                System.out.println(player.getInventory());
            }
        }));

        this.loop.getKeyFrames().add(keyFrame);
        this.loop.play();
    }

    /** Affiche ou supprime les items qui rentrent ou sort de la barre d'inventaire  */
    private void displayItemIntoInventoryBar()
    {
        Image inventoryBarImg = View.loadAnImage("inventoryBar.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        ImageView inventoryBarImgView = new ImageView(inventoryBarImg);

        int tileWidth = (int) (scaleMultiplicatorWidth * TileMaps.TILE_DEFAULT_SIZE);
        int tileHeight = (int) (scaleMultiplicatorHeight * TileMaps.TILE_DEFAULT_SIZE);

        int itemInventoryWidth = (int) (tileWidth/1.5);
        int itemInventoryHeight = (int) (tileHeight/1.5);

        int nbElementDisplayed = Player.NB_CASES_MAX_INVENTORY/Player.NB_LINES_INVENTORY;
        if (this.player.nbStacksIntoInventory() <= Player.NB_CASES_MAX_INVENTORY/Player.NB_LINES_INVENTORY)
            nbElementDisplayed = this.player.nbStacksIntoInventory();

        int compteur = 0;
        for (int integer = 0 ; integer < nbElementDisplayed; integer++) {
            ImageView itemView = new ImageView();
            Image itemImg = null;

            StowableObjectType item = this.player.getInventory()[player.getPosCursorVerticallyInventoryBar()][integer].get(0);
            if (item instanceof Dirt)
                itemImg = View.loadAnImage("tiles/floor-top.png", itemInventoryWidth, itemInventoryHeight);
            else if (item instanceof Stone)
                itemImg = View.loadAnImage("tiles/rock-fill.png", itemInventoryWidth, itemInventoryHeight);
            else if (item instanceof TallGrass)
                itemImg = View.loadAnImage("tiles/tall-grass.png", itemInventoryWidth, itemInventoryHeight);
            else if (item instanceof Torch)
                itemImg = View.loadAnImage("tiles/torch.png", itemInventoryWidth, itemInventoryHeight);

            if (!Objects.isNull(itemImg)) {
                itemView.setImage(itemImg);
                itemView.setX(inventoryBarImgView.getX() + (((inventoryBarImgView.getImage().getWidth()/9) - itemImg.getWidth())/2) + ((inventoryBarImgView.getImage().getWidth()/9)*compteur));
                itemView.setY(inventoryBarImgView.getY() + ((inventoryBarImgView.getImage().getHeight() - itemImg.getHeight())/2));

                int fontSize = (int) (5*scaleMultiplicatorWidth);
                Label nbObjects = new Label();
                nbObjects.setId("textInventoryBar");
                nbObjects.setFont(new Font("Arial", fontSize));
                nbObjects.setText(String.valueOf(this.player.getInventory()[player.getPosCursorHorizontallyInventoryBar()][integer].size()));
                nbObjects.setLayoutX(itemView.getX());
                nbObjects.setLayoutY(itemView.getY() + itemImg.getHeight() - fontSize);

                StringProperty stringProperty = new SimpleStringProperty(String.valueOf(this.player.getInventory()[player.getPosCursorHorizontallyInventoryBar()][integer].size()));
                this.player.getInventory()[player.getPosCursorHorizontallyInventoryBar()][integer].addListener((ListChangeListener<StowableObjectType>) c -> stringProperty.setValue(String.valueOf(c.getList().size())));
                nbObjects.textProperty().bind(stringProperty);

                displayInventory.getChildren().add(itemView);
                displayInventory.getChildren().add(nbObjects);
                compteur++;
            }
        }
    }
}
