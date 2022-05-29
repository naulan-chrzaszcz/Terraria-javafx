package fr.sae.terraria.vue;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.Clock;
import fr.sae.terraria.modele.blocks.Dirt;
import fr.sae.terraria.modele.blocks.Stone;
import fr.sae.terraria.modele.blocks.TallGrass;
import fr.sae.terraria.modele.entities.Player;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;


public class HUDView
{
    private final ImageView inventoryBarImgView;
    private final ImageView cursorImgView;

    private final Image inventoryBarImg;
    private final Image healthBarImg;
    private final Image clockImg;
    private final Image clockCursorImg;

    private final Clock gameTime;
    private final Player player;
    private final Pane display;

    private double scaleMultiplicatorWidth;
    private double scaleMultiplicatorHeight;
    private double windowWidth;
    private double windowHeight;
    private int tileWidth;
    private int tileHeight;


    public HUDView(Player player, Pane display, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight, Clock gameTime)
    {
        this.player = player;
        this.display = display;
        this.scaleMultiplicatorWidth = scaleMultiplicatorWidth;
        this.scaleMultiplicatorHeight = scaleMultiplicatorHeight;
        this.gameTime = gameTime;

        this.windowWidth = (scaleMultiplicatorWidth * Terraria.DISPLAY_RENDERING_WIDTH);
        this.windowHeight = (scaleMultiplicatorHeight * Terraria.DISPLAY_RENDERING_HEIGHT);
        this.tileWidth = (int) (scaleMultiplicatorWidth * TileMaps.TILE_DEFAULT_SIZE);
        this.tileHeight = (int) (scaleMultiplicatorHeight * TileMaps.TILE_DEFAULT_SIZE);

        this.healthBarImg = View.loadAnImage("health.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.inventoryBarImg = View.loadAnImage("inventoryBar.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        Image cursorImg = View.loadAnImage("cursor.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.clockImg = View.loadAnImage("clock.png",scaleMultiplicatorWidth,scaleMultiplicatorHeight);
        this.clockCursorImg = View.loadAnImage("clock-cursor.png",scaleMultiplicatorWidth,scaleMultiplicatorHeight);

        this.inventoryBarImgView = new ImageView(inventoryBarImg);
        this.cursorImgView = new ImageView(cursorImg);

        new ItemSelectedView(display, player, scaleMultiplicatorWidth, scaleMultiplicatorHeight);

        this.refreshItemsInventoryBar();
    }

    /** Applique une bordure de couleur noire autour de la barre d'inventaire */
    private Rectangle setFrameInventoryBar(ImageView inventoryBarImgView)
    {
        Rectangle frameInventoryBar = new Rectangle();
        frameInventoryBar.setWidth(inventoryBarImg.getWidth() + (2*scaleMultiplicatorWidth));
        frameInventoryBar.setHeight(inventoryBarImg.getHeight() + (2*scaleMultiplicatorHeight));
        frameInventoryBar.setX(inventoryBarImgView.getX() - scaleMultiplicatorWidth);
        frameInventoryBar.setY(inventoryBarImgView.getY() - scaleMultiplicatorHeight);

        return frameInventoryBar;
    }

    /** Applique sur les toutes les cases de l'inventaire des écouteurs qui permet d'actualiser les images des items  */
    private void refreshItemsInventoryBar()
    {
        for (int i = 0; i < this.player.getInventory().size(); i++)
            this.player.getInventory().get(i).addListener((ListChangeListener<StowableObjectType>) c -> {
                while(c.next())
                    this.displayItemIntoInventoryBar();
            });
    }

    /** Affiche ou supprime les items qui rentrent ou sort de la barre d'inventaire  */
    private void displayItemIntoInventoryBar()
    {
        int itemInventoryWidth = (int) (tileWidth/1.5);
        int itemInventoryHeight = (int) (tileHeight/1.5);

        int nbElementDisplayed = 9;
        if (this.player.nbStacksIntoInventory() <= Player.NB_CASES_MAX_INVENTORY/Player.NB_LINES_INVENTORY)
            nbElementDisplayed = this.player.nbStacksIntoInventory();


        int[] compteur = new int[1];
        for (int integer = 0 ; integer < nbElementDisplayed; integer++) {

            Image item = null;
            ImageView itemView = new ImageView();
            if (this.player.getInventory().get(integer).get(0) instanceof Dirt)
                item = View.loadAnImage("tiles/floor-top.png", itemInventoryWidth, itemInventoryHeight);
            else if (this.player.getInventory().get(integer).get(0) instanceof Stone)
                item = View.loadAnImage("tiles/rock-fill.png", itemInventoryWidth, itemInventoryHeight);
            else if (this.player.getInventory().get(integer).get(0) instanceof TallGrass)
                item = View.loadAnImage("tiles/tall-grass.png", itemInventoryWidth, itemInventoryHeight);

            if (item != null) {
                itemView.setX(inventoryBarImgView.getX() + (((inventoryBarImgView.getImage().getWidth()/9) - item.getWidth())/2) + ((inventoryBarImgView.getImage().getWidth()/9)*compteur[0]));
                itemView.setY(inventoryBarImgView.getY() + ((inventoryBarImgView.getImage().getHeight() - item.getHeight())/2));
            }
            itemView.setImage(item);
            display.getChildren().add(itemView);

            Label nombreObjets = new Label();
            nombreObjets.setText(String.valueOf(this.player.getInventory().get(integer).size()));

            StringProperty stringProperty = new SimpleStringProperty(String.valueOf(this.player.getInventory().get(integer).size()));
            nombreObjets.textProperty().bind(stringProperty);
            this.player.getInventory().get(integer).addListener((ListChangeListener<StowableObjectType>) c -> stringProperty.setValue(String.valueOf(c.getList().size())));
            nombreObjets.setFont(new Font("Arial",5 * scaleMultiplicatorWidth));
            nombreObjets.setLayoutX(inventoryBarImgView.getX() + ((((inventoryBarImgView.getImage().getWidth()/9)/2) - item.getWidth())) + ((inventoryBarImgView.getImage().getWidth()/9)*compteur[0]) + 5);
            nombreObjets.setLayoutY(inventoryBarImgView.getY() + ((inventoryBarImgView.getImage().getHeight() - item.getHeight()/2)) - 6);
            nombreObjets.setTextFill(Color.WHITE);
            display.getChildren().add(nombreObjets);
            compteur[0]++;
        }
    }

    public void displayInventoryBar()
    {
        this.inventoryBarImgView.setX(((windowWidth - inventoryBarImg.getWidth())/2));
        this.inventoryBarImgView.setY((windowHeight - inventoryBarImg.getHeight()) - tileHeight);

        display.getChildren().add(setFrameInventoryBar(inventoryBarImgView));
        display.getChildren().add(inventoryBarImgView);
    }

    /** Affiche un carré qui se superpose sur la barre d'inventaire qui permet de savoir où on se situe */
    public void displayCursorInventoryBar()
    {
        this.cursorImgView.setX(((windowWidth - inventoryBarImg.getWidth())/2 - scaleMultiplicatorWidth));
        this.cursorImgView.setY(((windowHeight - inventoryBarImg.getHeight()) - tileHeight) - scaleMultiplicatorHeight);

        this.player.positionOfCursorInventoryBar.addListener((obs, oldValue, newValue) -> {
            this.cursorImgView.setX(((windowWidth - inventoryBarImg.getWidth())/2 + ((inventoryBarImg.getWidth()/9) * newValue.intValue() - scaleMultiplicatorWidth)));
            if (newValue.intValue() >= 0 && newValue.intValue() <= 8)
                if (!this.player.getInventory().get(newValue.intValue()).isEmpty()) {
                    this.player.setItemSelected(this.player.getInventory().get(newValue.intValue()).get(0));
                } else this.player.setItemSelected(null);
            System.out.println(this.player.getItemSelected());
        });

        display.getChildren().add(cursorImgView);
    }

    public void displayHealthBar()
    {
        // Crée et positionne les cœurs dans un tableau de longueur qui correspond à la vie max du joueur.
        ImageView[] healths = new ImageView[(int) (player.getPvMax())];
        for (int i = 0; i < player.getPvMax(); i++) {
            ImageView healthView = new ImageView(healthBarImg);
            Rectangle2D viewPort = new Rectangle2D((healthView.getImage().getWidth()/3)*2, 0, (healthView.getImage().getWidth()/3), healthView.getImage().getHeight());

            healthView.setViewport(viewPort);
            healthView.setX(inventoryBarImgView.getX() + ((healthView.getImage().getWidth()/3)*i));
            healthView.setY((inventoryBarImgView.getY() - healthView.getImage().getHeight()) - (2*scaleMultiplicatorHeight));
            display.getChildren().add(healthView);
            healths[i] = healthView;
        }

        // Modifie le cœur selon la vie du joueur
        player.getPvProperty().addListener((obs, oldPv, newPv) -> {
            if (oldPv.intValue() >= 0) {
                ImageView healthView = healths[oldPv.intValue()-1];

                Rectangle2D viewPort = new Rectangle2D((healthView.getImage().getWidth()/3)*0, 0, (healthView.getImage().getWidth()/3), healthView.getImage().getHeight());
                healthView.setViewport(viewPort);
            }
        });
    }

    /** Affiche une horloge à aiguille visuelle à l'écran. */
    public void displayTimer()
    {
        ImageView clockCursorView = new ImageView(clockCursorImg);
        clockCursorView.setX(windowWidth/2 + (clockImg.getWidth()/2) - (clockCursorImg.getWidth()/2));

        ImageView clockView = new ImageView(clockImg);
        clockView.setX(windowWidth/2);

        Rotate rotate = new Rotate();
        rotate.setPivotX(windowWidth/2 + (clockImg.getWidth()/2) - (clockCursorImg.getWidth()/2) + (clockCursorImg.getWidth()/2));
        rotate.setPivotY(clockCursorImg.getHeight());

        gameTime.minutesProperty().addListener(((obs, oldV, newV) -> rotate.setAngle((newV.intValue()/8) - 90)));
        clockCursorView.getTransforms().add(rotate);

        display.getChildren().add(clockView);
        display.getChildren().add(clockCursorView);



    }
}
