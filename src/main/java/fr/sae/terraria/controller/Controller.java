package fr.sae.terraria.controller;

import fr.sae.terraria.modele.StowableObjectType;
import fr.sae.terraria.modele.blocks.Dirt;
import fr.sae.terraria.modele.blocks.Stone;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.util.ResourceBundle;

import java.net.URL;

import fr.sae.terraria.modele.entities.Player;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.Terraria;
import fr.sae.terraria.vue.View;


public class Controller implements Initializable
{
    // FXML objects
    @FXML
    private BorderPane root;
    @FXML
    private HBox title;
    @FXML
    private Pane displayHUD;
    @FXML
    private Pane displayTiledMap;

    // Local Object variable
    private Environment environment;

    // TODO: plutot mettre un DoubleProperty a c'est deux variables
    private double scaleMultiplicatorWidth = .0;
    private double scaleMultiplicatorHeight = .0;


    public Controller(Stage primaryStage)
    {
        // Listener pour sync la taille des tiles pour scale les tiles
        primaryStage.widthProperty().addListener((obs, oldV, newV) -> scaleMultiplicatorWidth = (newV.intValue() / Terraria.DISPLAY_RENDERING_WIDTH));
        primaryStage.heightProperty().addListener((obs, oldV, newV) -> scaleMultiplicatorHeight = ((newV.intValue()-title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT));

        this.addKeysEventListener(primaryStage);
    }

    public void initialize(URL location, ResourceBundle resources)
    {
        // La taille des tiles apres le scaling
        scaleMultiplicatorWidth = (root.getPrefWidth() / Terraria.DISPLAY_RENDERING_WIDTH);
        scaleMultiplicatorHeight = ((root.getPrefHeight()-title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT);

        this.environment = new Environment(scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        new View(environment, displayTiledMap, displayHUD, scaleMultiplicatorWidth, scaleMultiplicatorHeight);

        int tileWidth = (int) (scaleMultiplicatorWidth * TileMaps.TILE_DEFAULT_SIZE);
        int tileHeight = (int) (scaleMultiplicatorHeight * TileMaps.TILE_DEFAULT_SIZE);
        for (int i = 0; i < this.environment.getEntities().size(); i++)
            this.environment.getEntities().get(i).setRect(tileWidth, tileHeight);
    }

    /**
     * Ajoute la fonctionnalité des listeners des event du clavier
     * @param stage Specifier l'application
     */
    private void addKeysEventListener(Stage stage)
    {
        stage.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            this.environment.getPlayer().getKeysInput().put(key.getCode(), true);
            key.consume();
        });

        stage.addEventFilter(KeyEvent.KEY_RELEASED, key -> {
            this.environment.getPlayer().getKeysInput().put(key.getCode(), false);
            key.consume();
        });

        stage.addEventFilter(ScrollEvent.SCROLL, scroll -> {
            Player player = this.environment.getPlayer();

            if (scroll.getDeltaY() < 0)
                player.positionOfCursorInventoryBar.setValue(player.positionOfCursorInventoryBar.get() - 1);
            else if (scroll.getDeltaY() > 0)
                player.positionOfCursorInventoryBar.setValue(player.positionOfCursorInventoryBar.get() + 1);
            if (player.positionOfCursorInventoryBar.get() > 8)
                player.positionOfCursorInventoryBar.setValue(0);
            if (player.positionOfCursorInventoryBar.get() < 0)
                player.positionOfCursorInventoryBar.setValue(8);
        });

        stage.addEventFilter(MouseEvent.MOUSE_CLICKED, click -> {
            // TODO: Obliger de le recalculé, a voir dans le temps si c'est deplacable.
            double scaleMultiplicatorWidth = (root.getPrefWidth() / Terraria.DISPLAY_RENDERING_WIDTH);
            double scaleMultiplicatorHeight = ((root.getPrefHeight()-title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT);
            int tileWidth = (int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorWidth);
            int tileHeight = (int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorHeight);

            Player player = this.environment.getPlayer();
            int xBlock = (int) (click.getX()/tileWidth);
            int yBlock = (int) (click.getY()/tileHeight);
            int xPlayer = (int) ((player.getX()+(tileWidth/2))/tileWidth);
            int yPlayer = (int) ((player.getY()+(tileHeight/2))/tileHeight);
            int distanceBetweenBlockPlayerAxisX = Math.abs(xPlayer - xBlock);
            int distanceBetweenBlockPlayerAxisY = Math.abs(yPlayer - yBlock);

            if (distanceBetweenBlockPlayerAxisY >= 0 && distanceBetweenBlockPlayerAxisY <= Player.BREAK_BLOCK_DISTANCE
                    && distanceBetweenBlockPlayerAxisX >= 0 && distanceBetweenBlockPlayerAxisX <= Player.BREAK_BLOCK_DISTANCE) {
                if (click.getButton().equals(MouseButton.PRIMARY))
                    for (Entity entity : environment.getEntities()) {
                        int xEntity = (int) ((entity.getX()+(tileWidth/2))/tileWidth);
                        int yEntity = (int) ((entity.getY()+(tileHeight/2))/tileHeight);

                        if (xEntity == xBlock && yEntity == yBlock) {
                            System.out.println(entity);     // TODO: supp
                            player.pickup((StowableObjectType) entity);

                            Node nodeAtDelete = null; int i = 0;
                            do {
                                Node node = displayTiledMap.getChildren().get(i);
                                int xNode = (int) ((node.getTranslateX()+(tileWidth/2))/tileWidth);
                                int yNode = (int) ((node.getTranslateY()+(tileHeight/2))/tileHeight);

                                if (xNode == xEntity && yNode == yEntity) {
                                    nodeAtDelete = node;
                                    environment.getTileMaps().setTile(TileMaps.SKY, yNode, xNode);
                                    environment.getEntities().remove(entity);
                                    displayTiledMap.getChildren().remove(nodeAtDelete);
                                }
                                i++;
                            } while (i < displayTiledMap.getChildren().size() && nodeAtDelete == null);

                            break;
                        }
                    }

                if (click.getButton().equals(MouseButton.SECONDARY))
                {
                    if (player.getItemSelected() != null) {
                        System.out.println(player.getItemSelected());
                        if (player.getItemSelected() instanceof Dirt) {
                            environment.getEntities().add(0, new Dirt(xBlock*tileWidth, yBlock*tileHeight));
                            environment.getTileMaps().setTile(TileMaps.DIRT, yBlock, xBlock);
                        } else if (player.getItemSelected() instanceof Stone) {
                            environment.getEntities().add(0, new Stone(xBlock*tileWidth, yBlock*tileHeight));
                            environment.getTileMaps().setTile(TileMaps.STONE, yBlock, xBlock);
                        }
                    }
                }
            }
        });
    }
}