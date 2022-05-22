package fr.sae.terraria.controller;

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
    private Pane display;

    // Property variables
    private final IntegerProperty tileWidth;
    private final IntegerProperty tileHeight;

    // Local Object variable
    private Environment environment;
    private TileMaps tileMaps;

    private double scaleMultiplicatorWidth = .0;
    private double scaleMultiplicatorHeight = .0;


    public Controller(Stage primaryStage)
    {
        this.tileWidth = new SimpleIntegerProperty();
        this.tileHeight = new SimpleIntegerProperty();
        this.tileMaps = new TileMaps();

        this.addKeysEventListener(primaryStage);

        // Listener pour sync la taille des tiles pour scale les tiles
        primaryStage.widthProperty().addListener((obs, oldV, newV) -> {
            scaleMultiplicatorWidth = (newV.intValue() / Terraria.DISPLAY_RENDERING_WIDTH);
            tileWidth.setValue((int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorWidth));
        });
        primaryStage.heightProperty().addListener((obs, oldV, newV) -> {
            scaleMultiplicatorHeight = ((newV.intValue()-title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT);
            tileHeight.setValue((int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorHeight));
        });
    }

    public void initialize(URL location, ResourceBundle resources)
    {
        this.tileMaps.load("src/main/resources/fr/sae/terraria/maps/map_0.json");

        // La taille des tiles apres le scaling
        scaleMultiplicatorWidth = (root.getPrefWidth() / Terraria.DISPLAY_RENDERING_WIDTH);
        scaleMultiplicatorHeight = ((root.getPrefHeight()-title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT);
        this.tileWidth.setValue((int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorWidth));
        this.tileHeight.setValue((int) (TileMaps.TILE_DEFAULT_SIZE * scaleMultiplicatorHeight));

        this.environment = new Environment(tileMaps, tileWidth.get(), tileHeight.get());

        new View(environment, display, tileWidth, tileHeight, scaleMultiplicatorWidth, scaleMultiplicatorHeight);

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