package fr.sae.terraria.vue;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Clock;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.blocks.Dirt;
import fr.sae.terraria.modele.entities.blocks.Stone;
import fr.sae.terraria.modele.entities.blocks.TallGrass;
import fr.sae.terraria.modele.entities.Player;
import fr.sae.terraria.modele.entities.blocks.Torch;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;

import java.util.Objects;


public class HUDView
{
    private final ImageView inventoryBarImgView;
    private final ImageView cursorImgView;

    private final Image inventoryBarImg;
    private final Image healthBarImg;
    private final Image clockImg;
    private final Image clockCursorImg;

    private final Rectangle frameInventoryBar;

    private final Clock gameTime;
    private final Player player;
    private final Pane display;

    private double scaleMultiplicatorWidth;
    private double scaleMultiplicatorHeight;
    private double windowWidth;
    private double windowHeight;
    private int tileWidth;
    private int tileHeight;


    /**
     * Affiche tout ce qui concerne l'HUD
     *
     * @param gameTime Pour afficher l'horloge
     * @param display Sur quel pane l'HUD doit s'afficher
     */
    public HUDView(Player player, Clock gameTime, Pane display, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        this.player = player;
        this.display = display;
        this.scaleMultiplicatorWidth = scaleMultiplicatorWidth;
        this.scaleMultiplicatorHeight = scaleMultiplicatorHeight;
        this.gameTime = gameTime;

        new ItemSelectedView(display, player, scaleMultiplicatorWidth, scaleMultiplicatorHeight);

        this.windowWidth = (scaleMultiplicatorWidth * Terraria.DISPLAY_RENDERING_WIDTH);
        this.windowHeight = (scaleMultiplicatorHeight * Terraria.DISPLAY_RENDERING_HEIGHT);
        this.tileWidth = (int) (scaleMultiplicatorWidth * TileMaps.TILE_DEFAULT_SIZE);
        this.tileHeight = (int) (scaleMultiplicatorHeight * TileMaps.TILE_DEFAULT_SIZE);

        this.frameInventoryBar = new Rectangle();
        this.healthBarImg = View.loadAnImage("health.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.inventoryBarImg = View.loadAnImage("inventoryBar.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        Image cursorImg = View.loadAnImage("cursor.png", scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.clockImg = View.loadAnImage("clock.png",scaleMultiplicatorWidth,scaleMultiplicatorHeight);
        this.clockCursorImg = View.loadAnImage("clock-cursor.png",scaleMultiplicatorWidth,scaleMultiplicatorHeight);

        this.inventoryBarImgView = new ImageView(inventoryBarImg);
        this.cursorImgView = new ImageView(cursorImg);

        this.refreshItemsInventoryBar();
    }

    /** Applique une bordure de couleur noire autour de la barre d'inventaire */
    private Rectangle setFrameInventoryBar(ImageView inventoryBarImgView)
    {
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

        int nbElementDisplayed = Player.NB_CASES_MAX_INVENTORY/Player.NB_LINES_INVENTORY;
        if (this.player.nbStacksIntoInventory() <= Player.NB_CASES_MAX_INVENTORY/Player.NB_LINES_INVENTORY)
            nbElementDisplayed = this.player.nbStacksIntoInventory();

        int compteur = 0;
        for (int integer = 0 ; integer < nbElementDisplayed; integer++) {
            ImageView itemView = new ImageView();
            Image item = null;

            if (this.player.getInventory().get(integer).get(0) instanceof Dirt)
                item = View.loadAnImage("tiles/floor-top.png", itemInventoryWidth, itemInventoryHeight);
            else if (this.player.getInventory().get(integer).get(0) instanceof Stone)
                item = View.loadAnImage("tiles/rock-fill.png", itemInventoryWidth, itemInventoryHeight);
            else if (this.player.getInventory().get(integer).get(0) instanceof TallGrass)
                item = View.loadAnImage("tiles/tall-grass.png", itemInventoryWidth, itemInventoryHeight);
            else if (this.player.getInventory().get(integer).get(0) instanceof Torch){
                item = View.loadAnImage("tiles/torch.png", itemInventoryWidth, itemInventoryHeight);
            }


            if (!Objects.isNull(item)) {
                itemView.setImage(item);
                itemView.setX(inventoryBarImgView.getX() + (((inventoryBarImgView.getImage().getWidth()/9) - item.getWidth())/2) + ((inventoryBarImgView.getImage().getWidth()/9)*compteur));
                itemView.setY(inventoryBarImgView.getY() + ((inventoryBarImgView.getImage().getHeight() - item.getHeight())/2));

                int fontSize = (int) (5*scaleMultiplicatorWidth);
                Label nbObjects = new Label();
                nbObjects.setId("textInventoryBar");
                nbObjects.setFont(new Font("Arial", fontSize));
                nbObjects.setText(String.valueOf(this.player.getInventory().get(integer).size()));
                nbObjects.setLayoutX(itemView.getX());
                nbObjects.setLayoutY(itemView.getY() + item.getHeight() - fontSize);

                StringProperty stringProperty = new SimpleStringProperty(String.valueOf(this.player.getInventory().get(integer).size()));
                this.player.getInventory().get(integer).addListener((ListChangeListener<StowableObjectType>) c -> stringProperty.setValue(String.valueOf(c.getList().size())));
                nbObjects.textProperty().bind(stringProperty);

                display.getChildren().add(itemView);
                display.getChildren().add(nbObjects);
                compteur++;
            }
        }
    }

    /** Affiche la barre d'inventaire */
    public void displayInventoryBar()
    {
        Torch torch = new Torch(0, 0);
        this.player.pickup(torch);
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

        this.player.positionOfCursorInventoryBar.addListener((obs, oldV, newV) -> {
            this.cursorImgView.setX(((windowWidth - inventoryBarImg.getWidth())/2 + ((inventoryBarImg.getWidth()/9) * newV.intValue() - scaleMultiplicatorWidth)));
            if (newV.intValue() >= 0 && newV.intValue() < (Player.NB_CASES_MAX_INVENTORY/Player.NB_LINES_INVENTORY))
                this.player.setItemSelected((!this.player.getInventory().get(newV.intValue()).isEmpty()) ? this.player.getInventory().get(newV.intValue()).get(0) : null);
        });

        display.getChildren().add(cursorImgView);
    }

    /** Affiche la barre de vie du joueur */
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
    public void displayClock()
    {
        ImageView clockCursorView = new ImageView(clockCursorImg);
        ImageView clockView = new ImageView(clockImg);
        Rotate rotate = new Rotate();
        double yClock = inventoryBarImgView.getY() - clockImg.getHeight();
        double xClock = inventoryBarImgView.getX() + (inventoryBarImg.getWidth()/2 - clockImg.getWidth()/2);

        clockCursorView.setX(xClock + (clockImg.getWidth()/2) - (clockCursorImg.getWidth()/2));
        clockCursorView.setY(yClock);
        clockView.setX(xClock);
        clockView.setY(yClock);

        rotate.setPivotX(xClock + (clockImg.getWidth()/2) - (clockCursorImg.getWidth()/2) + (clockCursorImg.getWidth()/2));
        rotate.setPivotY(yClock + clockCursorImg.getHeight());
        gameTime.minutesProperty().addListener(((obs, oldV, newV) -> rotate.setAngle((newV.intValue()/8) - 90)));
        clockCursorView.getTransforms().add(rotate);

        display.getChildren().add(clockView);
        display.getChildren().add(clockCursorView);
    }
}
