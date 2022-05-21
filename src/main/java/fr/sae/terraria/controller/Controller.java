package fr.sae.terraria.controller;

import fr.sae.terraria.vue.View;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.util.ResourceBundle;

import java.net.URL;

import fr.sae.terraria.vue.TileMapsView;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;


public class Controller implements Initializable
{
    // Constants
    public static final int DISPLAY_RENDERING_WIDTH = 465;
    public static final int DISPLAY_RENDERING_HEIGHT = 256;

    // FXML objects
    @FXML
    private BorderPane root;
    @FXML
    private HBox title;
    @FXML
    private Pane display;

    // Property variables
    private final IntegerProperty tileWidth;
    private final IntegerProperty tileHeight;

    // Local Object variable
    private Environment environment;
    private TileMaps tileMaps;


    public Controller(Stage primaryStage)
    {
        this.tileWidth = new SimpleIntegerProperty();
        this.tileHeight = new SimpleIntegerProperty();
        this.tileMaps = new TileMaps();

        this.addKeysEventListener(primaryStage);

        // Listener pour sync la taille des tiles pour scale les tiles
        primaryStage.widthProperty().addListener((obs, oldV, newV) -> tileWidth.setValue((int) (TileMaps.TILE_DEFAULT_SIZE *((newV.intValue() / DISPLAY_RENDERING_WIDTH)))));
        primaryStage.heightProperty().addListener((obs, oldV, newV) -> tileHeight.setValue((int) (TileMaps.TILE_DEFAULT_SIZE *((newV.intValue()-title.getPrefHeight()) / DISPLAY_RENDERING_HEIGHT))));
    }

    public void initialize(URL location, ResourceBundle resources)
    {
        this.tileMaps.load("src/main/resources/fr/sae/terraria/maps/map_0.json");

        // La taille des tiles apres le scaling
        this.tileWidth.setValue((int) (TileMaps.TILE_DEFAULT_SIZE *((root.getPrefWidth() / DISPLAY_RENDERING_WIDTH))));
        this.tileHeight.setValue((int) (TileMaps.TILE_DEFAULT_SIZE *((root.getPrefHeight()-title.getPrefHeight()) / DISPLAY_RENDERING_HEIGHT)));

        this.environment = new Environment(tileMaps, tileWidth.get(), tileHeight.get());

        View view = new View(tileMaps, environment, display, tileWidth, tileHeight);

        for (int i = 0; i < this.environment.getEntities().size(); i++)
            this.environment.getEntities().get(i).setRect(tileWidth.get(), tileWidth.get());
    }

    /**
     * Ajoute la fonctionnalité des listeners des event du clavier
     * @param stage Specifie l'application
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
    }
}