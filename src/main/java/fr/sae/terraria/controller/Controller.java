package fr.sae.terraria.controller;

import fr.sae.terraria.modele.entities.Player;
import fr.sae.terraria.modele.entities.entity.Entity;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.util.ResourceBundle;

import java.net.URL;

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

    // Property variables
    private final IntegerProperty tileWidth;
    private final IntegerProperty tileHeight;

    // Local Object variable
    private Environment environment;

    private double scaleMultiplicatorWidth = .0;
    private double scaleMultiplicatorHeight = .0;


    public Controller(Stage primaryStage)
    {
        this.tileWidth = new SimpleIntegerProperty();
        this.tileHeight = new SimpleIntegerProperty();

        // Listener pour sync la taille des tiles pour scale les tiles
        primaryStage.widthProperty().addListener((obs, oldV, newV) -> {
            scaleMultiplicatorWidth = (newV.intValue() / Terraria.DISPLAY_RENDERING_WIDTH);
            tileWidth.setValue((int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorWidth));
        });
        primaryStage.heightProperty().addListener((obs, oldV, newV) -> {
            scaleMultiplicatorHeight = ((newV.intValue()-title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT);
            tileHeight.setValue((int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorHeight));
        });
        this.addKeysEventListener(primaryStage);
    }

    public void initialize(URL location, ResourceBundle resources)
    {

        // La taille des tiles apres le scaling
        scaleMultiplicatorWidth = (root.getPrefWidth() / Terraria.DISPLAY_RENDERING_WIDTH);
        scaleMultiplicatorHeight = ((root.getPrefHeight()-title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT);
        this.tileWidth.setValue((int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorWidth));
        this.tileHeight.setValue((int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorHeight));

        this.environment = new Environment(scaleMultiplicatorWidth, scaleMultiplicatorHeight);

        new View(environment, displayTiledMap, displayHUD, scaleMultiplicatorWidth, scaleMultiplicatorHeight);

        for (int i = 0; i < this.environment.getEntities().size(); i++)
            this.environment.getEntities().get(i).setRect(tileWidth.get(), tileWidth.get());
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
                player.deplacementScroll.setValue(player.deplacementScroll.get() - 1);
            else if (scroll.getDeltaY() > 0)
                player.deplacementScroll.setValue(player.deplacementScroll.get() + 1);
            if (player.deplacementScroll.get() > 8)
                player.deplacementScroll.setValue(0);
            if (player.deplacementScroll.get() < 0)
                player.deplacementScroll.setValue(8);
        });

        stage.addEventFilter(MouseEvent.MOUSE_CLICKED, click -> {
            Player player = this.environment.getPlayer();
            int xBlock = (int) (click.getX()/tileWidth.get());
            int yBlock = (int) (click.getY()/tileHeight.get());
            int xPlayer = (int) ((player.getX()+(tileWidth.get()/2))/tileWidth.get());
            int yPlayer = (int) ((player.getY()+(tileHeight.get()/2))/tileHeight.get());
            int distanceBetweenBlockPlayerAxisX = Math.abs(xPlayer - xBlock);
            int distanceBetweenBlockPlayerAxisY = Math.abs(yPlayer - yBlock);

            if (distanceBetweenBlockPlayerAxisY >= 0 && distanceBetweenBlockPlayerAxisY <= Player.BREAK_BLOCK_DISTANCE
                && distanceBetweenBlockPlayerAxisX >= 0 && distanceBetweenBlockPlayerAxisX <= Player.BREAK_BLOCK_DISTANCE) {
                for (Entity entity : environment.getEntities()) {
                    int xEntity = (int) ((entity.getX()+(tileWidth.get()/2))/tileWidth.get());
                    int yEntity = (int) ((entity.getY()+(tileHeight.get()/2))/tileHeight.get());

                    if (xEntity == xBlock && yEntity == yBlock) {
                        System.out.println(entity);
                        player.pickup(entity);
                        break;
                    }
                }
            }
        });
    }
}