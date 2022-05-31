package fr.sae.terraria.controller;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.Player;
import fr.sae.terraria.modele.entities.blocks.Dirt;
import fr.sae.terraria.modele.entities.blocks.Stone;
import fr.sae.terraria.modele.entities.blocks.Torch;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.vue.View;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class GameController implements Initializable
{
    @FXML
    private HBox title;
    @FXML
    private BorderPane root;
    @FXML
    private Pane displayHUD;
    @FXML
    private Pane displayTiledMap;
    @FXML
    private  Pane filter;

    private Stage primaryStage;
    private Environment environment;

    // TODO: plutot mettre un DoubleProperty a c'est deux variables
    private double scaleMultiplicatorWidth = .0;    // Permet de scale correctement une image selon la largeur de l'écran
    private double scaleMultiplicatorHeight = .0;   // Permet de scale correctement une image selon la hauteur de l'écran


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
        new View(environment, displayTiledMap, displayHUD, scaleMultiplicatorWidth, scaleMultiplicatorHeight, filter);
        /*Circle c =new Circle(1000*scaleMultiplicatorWidth);
        c.setLayoutX(500);
        c.setLayoutY(500);
        c.setOpacity(1);
        c.setFill(Color.web("#f2f2c7"));
        filter.getChildren().add(c);

        Rectangle r = new Rectangle(16*30*scaleMultiplicatorWidth,16*16*scaleMultiplicatorHeight);
        r.setFill(Color.web("#0d0d38"));
*/
        this.addKeysEventListener(primaryStage);
/*
        Circle c =new Circle(50*scaleMultiplicatorWidth);
        c.setLayoutX(500);
        c.setLayoutY(500);
        c.setFill(Color.YELLOW);
        final Shape shape = Shape.subtract(r,c);
        filter.getChildren().clear();
        Circle ca = new Circle(300);
        ca.setTranslateX(50);
        ca.setTranslateY(50);
        shape.subtract(shape,c);
        final Shape shapdde = Shape.subtract(shape,ca);
        filter.getChildren().add(shapdde);
*/




    }

    /**
     * Ajoute la fonctionnalité des listeners des event du clavier
     * @param stage Specifier l'application
     */
    private void addKeysEventListener(Stage stage)
    {
        Player player = this.environment.getPlayer();

        stage.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            player.getKeysInput().put(key.getCode(), true);
            key.consume();
        });

        stage.addEventFilter(KeyEvent.KEY_RELEASED, key -> {
            player.getKeysInput().put(key.getCode(), false);
            key.consume();
        });

        stage.addEventFilter(ScrollEvent.SCROLL, scroll -> {
            boolean scrollUp = scroll.getDeltaY() > 0;
            if (scrollUp)
                player.positionOfCursorInventoryBar.setValue(player.positionOfCursorInventoryBar.get() + 1);
            boolean scrollDown = scroll.getDeltaY() < 0;
            if (scrollDown)
                player.positionOfCursorInventoryBar.setValue(player.positionOfCursorInventoryBar.get() - 1);

            if (player.positionOfCursorInventoryBar.get() > 8)
                player.positionOfCursorInventoryBar.setValue(0);
            if (player.positionOfCursorInventoryBar.get() < 0)
                player.positionOfCursorInventoryBar.setValue(8);
        });

        stage.addEventFilter(MouseEvent.MOUSE_CLICKED, click -> {
            double scaleMultiplicativeWidth = (root.getPrefWidth() / Terraria.DISPLAY_RENDERING_WIDTH);
            double scaleMultiplicativeHeight = ((root.getPrefHeight()-title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT);
            int tileWidth = (int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicativeWidth);
            int tileHeight = (int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicativeHeight);
            // La position correcte sur le Pane
            double mouseX = click.getSceneX();
            double mouseY = click.getSceneY()-title.getPrefHeight();
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
                            player.pickup((StowableObjectType) entity);

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
                        Entity entity = null;
                        if (player.getItemSelected() instanceof Dirt) {
                            entity = new Dirt(xBlock*tileWidth, yBlock*tileHeight);
                            environment.getTileMaps().setTile(TileMaps.DIRT, yBlock, xBlock);
                        } else if (player.getItemSelected() instanceof Stone) {
                            entity = new Stone(xBlock*tileWidth, yBlock*tileHeight);
                            environment.getTileMaps().setTile(TileMaps.STONE, yBlock, xBlock);
                        } else if (player.getItemSelected() instanceof Torch) {
                            if (environment.getTileMaps().getTile((int) mouseX/tileWidth, (int) (mouseY/tileHeight )+1) != TileMaps.SKY && environment.getTileMaps().getTile((int) mouseX/tileWidth, (int) (mouseY/tileHeight )) == TileMaps.SKY){
                                environment.getEntities().add(0, new Torch(xBlock*tileWidth, yBlock*tileWidth));
                            }

                        }

                        if(!Objects.isNull(entity)) {
                            entity.setRect(tileWidth, tileHeight);
                            environment.getEntities().add(0, entity);

                            // Si on le pose sur le joueur
                            boolean isIntoABlock = player.getRect().collideRect(entity.getRect());
                            if (isIntoABlock) {
                                // Place le joueur au-dessus du block posé.
                                player.setY(entity.getY() - player.getRect().getHeight());
                                player.getGravity().yInit = player.getY();
                                player.getGravity().xInit = player.getX();
                            }

                            player.getInventory().get(player.positionOfCursorInventoryBar.get()).remove(player.getItemSelected());
                            if (player.getInventory().get(player.positionOfCursorInventoryBar.get()).size()-1 >= 0)
                                player.setItemSelected(player.getInventory().get(player.positionOfCursorInventoryBar.get()).get(player.getInventory().get(player.positionOfCursorInventoryBar.get()).size()-1));
                        }
                    }
                }
            }
        });
    }
}