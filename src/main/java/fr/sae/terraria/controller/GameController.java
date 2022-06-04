package fr.sae.terraria.controller;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.blocks.*;
import fr.sae.terraria.modele.entities.entity.*;
import fr.sae.terraria.modele.entities.items.Fiber;
import fr.sae.terraria.modele.entities.items.Meat;
import fr.sae.terraria.modele.entities.items.Wood;
import fr.sae.terraria.modele.entities.player.Inventory;
import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.vue.Camera;
import fr.sae.terraria.vue.View;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
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

    // TODO: plutot mettre un DoubleProperty a c'est deux variables
    public double scaleMultiplicatorWidth = .0;    // Permet de scale correctement une image selon la largeur de l'écran
    public double scaleMultiplicatorHeight = .0;   // Permet de scale correctement une image selon la hauteur de l'écran


    public GameController(Stage primaryStage)
    {
        this.primaryStage = primaryStage;

        primaryStage.widthProperty().addListener((obs, oldV, newV) -> scaleMultiplicatorWidth = (newV.intValue() / Terraria.DISPLAY_RENDERING_WIDTH));
        primaryStage.heightProperty().addListener((obs, oldV, newV) -> scaleMultiplicatorHeight = ((newV.intValue()-title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT));
    }

    public void initialize(URL location, ResourceBundle resources)
    {
        scaleMultiplicatorWidth = (root.getPrefWidth() / Terraria.DISPLAY_RENDERING_WIDTH);
        scaleMultiplicatorHeight = ((root.getPrefHeight()-title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT);

        this.environment = new Environment(scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        this.player = this.environment.getPlayer();
        new View(environment,
                displayHostileBeings,
                displayTiledMap,
                displayHUD,
                displayCursorMouse,
                filter,
                scaleMultiplicatorWidth,
                scaleMultiplicatorHeight);

        Camera camera = new Camera(environment, paneHadCamera);
        this.addKeysEventListener(primaryStage);
    }

    /**
     * Ajoute la fonctionnalité des listeners des event du clavier
     * @param stage Specifier l'application
     */
    private void addKeysEventListener(Stage stage)
    {
        Player player = this.environment.getPlayer();
        Inventory inventory = player.getInventory();

        stage.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            player.getKeysInput().put(key.getCode(), true);
            inventory.getKeysInput().put(key.getCode(), true);
            key.consume();
        });

        stage.addEventFilter(KeyEvent.KEY_RELEASED, key -> {
            player.getKeysInput().put(key.getCode(), false);
            inventory.getKeysInput().put(key.getCode(), false);
            key.consume();
        });

        stage.addEventFilter(MouseEvent.MOUSE_CLICKED, mouse -> {
            player.getMouseInput().put(mouse.getButton(), true);
            mouse.consume();
        });

        stage.addEventFilter(MouseEvent.MOUSE_RELEASED, mouse -> {
            player.getMouseInput().put(mouse.getButton(), false);
            mouse.consume();
        });

        stage.addEventFilter(ScrollEvent.SCROLL, scroll -> {
            inventory.setScroll((int) scroll.getDeltaY());
            scroll.consume();
        });

        stage.addEventFilter(MouseEvent.MOUSE_CLICKED, click -> {
            double scaleMultiplicativeWidth = (root.getPrefWidth() / Terraria.DISPLAY_RENDERING_WIDTH);
            double scaleMultiplicativeHeight = ((root.getPrefHeight()-title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT);
            int tileWidth = (int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicativeWidth);
            int tileHeight = (int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicativeHeight);
            // La position correcte sur le Pane
            double mouseX = click.getSceneX()+((Rectangle) displayTiledMap.getParent().getClip()).getX();
            double mouseY = (click.getSceneY()-title.getPrefHeight())+((Rectangle) displayTiledMap.getParent().getClip()).getY();
            Rectangle2D rectangle = new Rectangle2D(mouseX, mouseY, scaleMultiplicativeWidth, scaleMultiplicativeHeight);
            // Le bloc où la souris à clicker
            int xBlock = (int) (mouseX/tileWidth);
            int yBlock = (int) (mouseY/tileHeight);
            // La position du joueur
            int xPlayer = (int) ((player.getX()+(tileWidth/2))/tileWidth);
            int yPlayer = (int) ((player.getY()+(tileHeight/2))/tileHeight);
            // La distance entre le bloc et le joueur
            int distanceBetweenBlockPlayerAxisX = Math.abs(xPlayer - xBlock);
            int distanceBetweenBlockPlayerAxisY = Math.abs(yPlayer - yBlock);

            boolean isOneBlockDistance = distanceBetweenBlockPlayerAxisY >= 0 && distanceBetweenBlockPlayerAxisY <= Player.BREAK_BLOCK_DISTANCE && distanceBetweenBlockPlayerAxisX >= 0 && distanceBetweenBlockPlayerAxisX <= Player.BREAK_BLOCK_DISTANCE;
            if (isOneBlockDistance)
            {
                // Casse les blocs
                if (click.getButton().equals(MouseButton.PRIMARY))
                    // Commence a cherché l'entité ciblée
                    for (Entity entity : environment.getEntities()) {
                        // La position de l'entité
                        int xEntity = (int) (entity.getRect().get().getMinX());
                        int yEntity = (int) (entity.getRect().get().getMinY());

                        if (entity.getRect().collideRect(rectangle)) {
                            if (entity instanceof BreakableObjectType)
                                ((BreakableObjectType) entity).breaks();

                            if (entity instanceof CollapsibleObjectType)
                                ((CollapsibleObjectType) entity).hit();

                            Node nodeAtDelete = null; int i = 0;
                            // Tant qu'on n'a pas trouvé l'objet sur le Pane, il continue la boucle.
                            do {
                                Node node = displayTiledMap.getChildren().get(i);
                                int xNode = (int) (node.getTranslateX());
                                int yNode = (int) (node.getTranslateY());

                                if (xNode == xEntity && yNode == yEntity) {
                                    nodeAtDelete = node;
                                    environment.getTileMaps().setTile(TileMaps.SKY, yNode/tileHeight, xNode/tileWidth);
                                    // Supprime dans le modele et dans la vue
                                    environment.getEntities().remove(entity);
                                    displayTiledMap.getChildren().remove(nodeAtDelete);
                                }
                                i++;
                            } while (i < displayTiledMap.getChildren().size() && Objects.isNull(nodeAtDelete));

                            // Quand tous c'est bien déroulés, aprés avoir trouvé l'entité et l'objet sur l'écran, il arrête de chercher d'autre entité d'où le break
                            break;
                        }
                    }

                // Pose les blocs
                if (click.getButton().equals(MouseButton.SECONDARY)) {
                    boolean haveAnItemOnHand = !Objects.isNull(player.getItemSelected());
                    if (haveAnItemOnHand) {
                        if (environment.getTileMaps().getTile(xBlock, yBlock) == TileMaps.SKY) {
                            Entity entity = null;
                            if (player.getItemSelected() instanceof Dirt) {
                                entity = new Dirt(this.environment, xBlock*tileWidth, yBlock*tileHeight);
                                environment.getTileMaps().setTile(TileMaps.DIRT, yBlock, xBlock);
                            } else if (player.getItemSelected() instanceof Stone) {
                                entity = new Stone(this.environment, xBlock*tileWidth, yBlock*tileHeight);
                                environment.getTileMaps().setTile(TileMaps.STONE, yBlock, xBlock);
                            } else if (player.getItemSelected() instanceof Torch) {
                                entity = new Torch(xBlock * tileWidth, yBlock * tileHeight);
                                environment.getEntities().add(0, entity);
                                environment.getTorches().add(0, (Torch) entity);
                            } else if (player.getItemSelected() instanceof Meat)
                                player.setPv(player.getPv() - 1);

                            if(!Objects.isNull(entity)) {
                                Environment.playSound("sound/axchop.wav", false);
                                entity.setRect(tileWidth, tileHeight);
                                environment.getEntities().add(0, entity);

                                // Si on le pose sur le joueur
                                boolean isIntoABlock = player.getRect().collideRect(entity.getRect());
                                if (entity instanceof CollideObjectType && isIntoABlock) {
                                    // Place le joueur au-dessus du block posé.
                                    player.setY(entity.getY() - player.getRect().getHeight());
                                    player.getGravity().yInit = player.getY();
                                    player.getGravity().xInit = player.getX();
                                }

                                ObservableList<StowableObjectType> stacksSelected = inventory.get()[inventory.getPosCursorVerticallyInventoryBar()][inventory.getPosCursorHorizontallyInventoryBar()];
                                stacksSelected.remove(player.getItemSelected());
                                if (stacksSelected.size()-1 >= 0) {
                                    int endLineStacks = stacksSelected.size()-1;
                                    player.setItemSelected(stacksSelected.get(endLineStacks));
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}