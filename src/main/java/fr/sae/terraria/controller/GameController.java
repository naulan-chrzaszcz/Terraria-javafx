package fr.sae.terraria.controller;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.entity.*;
import fr.sae.terraria.modele.entities.player.inventory.Inventory;
import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.vue.Camera;
import fr.sae.terraria.vue.View;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class GameController implements Initializable
{
    @FXML public HBox title;
    @FXML public BorderPane root;
    @FXML public Pane displayHUD;
    @FXML public Pane displayTiledMap;
    @FXML public Pane displayHostileBeings;
    @FXML public Pane filter;
    @FXML public Pane displayCursorMouse;
    @FXML public StackPane paneHadCamera;

    public final Stage primaryStage;

    public Environment environment;
    public Player player = null;

    // TODO plutot mettre un DoubleProperty a c'est deux variables
    public double scaleMultiplicatorWidth = .0;    // Permet de scale correctement une image selon la largeur de l'écran
    public double scaleMultiplicatorHeight = .0;   // Permet de scale correctement une image selon la hauteur de l'écran


    public GameController(final Stage primaryStage)
    {
        super();
        this.primaryStage = primaryStage;

        primaryStage.widthProperty().addListener((obs, oldV, newV) -> this.scaleMultiplicatorWidth = (newV.intValue() / Terraria.DISPLAY_RENDERING_WIDTH));
        primaryStage.heightProperty().addListener((obs, oldV, newV) -> this.scaleMultiplicatorHeight = ((newV.intValue()-this.title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT));
    }

    @Override public void initialize(URL location, ResourceBundle resources)
    {
        this.scaleMultiplicatorWidth = (this.root.getPrefWidth() / Terraria.DISPLAY_RENDERING_WIDTH);
        this.scaleMultiplicatorHeight = ((this.root.getPrefHeight()-this.title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT);

        this.environment = new Environment(this.scaleMultiplicatorWidth, this.scaleMultiplicatorHeight);
        this.player = this.environment.getPlayer();

        new View(this);
        new Camera(this.environment, this.paneHadCamera);

        this.addKeysEventListener(this.primaryStage);
    }

    /**
     * Ajoute la fonctionnalité des listeners des event du clavier
     * @param stage Specifier l'application
     */
    private void addKeysEventListener(final Stage stage)
    {
        final Inventory inventory = this.player.getInventory();

        stage.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            this.player.getKeysInput().put(key.getCode(), true);
            inventory.getKeysInput().put(key.getCode(), true);
            key.consume();
        });

        stage.addEventFilter(KeyEvent.KEY_RELEASED, key -> {
            this.player.getKeysInput().put(key.getCode(), false);
            inventory.getKeysInput().put(key.getCode(), false);
            key.consume();
        });

        stage.addEventFilter(MouseEvent.MOUSE_CLICKED, mouse -> {
            this.player.getMouseInput().put(mouse.getButton(), true);
            mouse.consume();
        });

        stage.addEventFilter(MouseEvent.MOUSE_RELEASED, mouse -> {
            this.player.getMouseInput().put(mouse.getButton(), false);
            mouse.consume();
        });

        stage.addEventFilter(ScrollEvent.SCROLL, scroll -> {
            inventory.setScroll((int) scroll.getDeltaY());
            scroll.consume();
        });

        stage.addEventFilter(MouseEvent.MOUSE_CLICKED, click -> {
            final double scaleMultiplicativeWidth = (this.root.getPrefWidth() / Terraria.DISPLAY_RENDERING_WIDTH);
            final double scaleMultiplicativeHeight = ((this.root.getPrefHeight()-this.title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT);
            final int tileWidth = (int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicativeWidth);
            final int tileHeight = (int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicativeHeight);
            // La position correcte sur le Pane
            double mouseX = click.getSceneX()+((Rectangle) this.displayTiledMap.getParent().getClip()).getX();
            double mouseY = (click.getSceneY()-this.title.getPrefHeight())+((Rectangle) this.displayTiledMap.getParent().getClip()).getY();
            // Le bloc où la souris à clicker
            int xBlock = (int) (mouseX/tileWidth);
            int yBlock = (int) (mouseY/tileHeight);
            Rectangle2D rectangle = new Rectangle2D(mouseX, mouseY, scaleMultiplicativeWidth, scaleMultiplicativeHeight);
            // La position du joueur
            int xPlayer = (int) ((this.player.getX()+(tileWidth/2))/tileWidth);
            int yPlayer = (int) ((this.player.getY()+(tileHeight/2))/tileHeight);
            // La distance entre le bloc et le joueur
            int distanceBetweenBlockPlayerAxisX = Math.abs(xPlayer - xBlock);
            int distanceBetweenBlockPlayerAxisY = Math.abs(yPlayer - yBlock);

            boolean isOneBlockDistance = distanceBetweenBlockPlayerAxisY >= 0 && distanceBetweenBlockPlayerAxisY <= Player.BREAK_BLOCK_DISTANCE && distanceBetweenBlockPlayerAxisX >= 0 && distanceBetweenBlockPlayerAxisX <= Player.BREAK_BLOCK_DISTANCE;
            if (this.player.getStackSelected() instanceof EatableObjectType) {
                ((EatableObjectType) this.player.getStackSelected()).eat();
            } else if (isOneBlockDistance) {
                if (click.getButton().equals(MouseButton.PRIMARY))
                    this.breakBlock(rectangle);
                if (click.getButton().equals(MouseButton.SECONDARY))
                    this.placeBlock(xBlock, yBlock);
            }
        });
    }

    private void breakBlock(final Rectangle2D rectangle)
    {
        // Commence a cherché l'entité ciblée
        for (Entity entity : this.environment.getEntities()) if (entity.getRect().collideRect(rectangle)) {
            if (entity instanceof BreakableObjectType)
                ((BreakableObjectType) entity).breaks();
            if (entity instanceof CollapsibleObjectType)    // TODO TEMP, à déplacer
                ((CollapsibleObjectType) entity).hit();

            // Quand tous c'est bien déroulés, aprés avoir trouvé l'entité et l'objet sur l'écran, il arrête de chercher d'autre entité d'où le break
            break;
        }
    }

    private void placeBlock(int xBlock, int yBlock)
    {
        boolean haveAnItemOnHand = !Objects.isNull(this.player.getStackSelected());
        boolean goodPlace = this.environment.getTileMaps().getTile(xBlock, yBlock) == TileMaps.SKY;

        if (haveAnItemOnHand && goodPlace) {
            if (!(this.player.getStackSelected().getItem() instanceof PlaceableObjectType) && !(this.player.getStackSelected() instanceof EatableObjectType))
                return;

            if (this.player.getStackSelected().getItem() instanceof PlaceableObjectType)
                ((PlaceableObjectType) this.player.getStackSelected().getItem()).place(xBlock, yBlock);
        }
    }
}