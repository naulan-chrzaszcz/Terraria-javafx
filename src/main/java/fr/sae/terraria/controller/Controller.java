package fr.sae.terraria.controller;

import fr.sae.terraria.vue.TileMapsView;
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


public class Controller implements Initializable
{
    private final static int displayRenderingWidth = 465;
    private final static int displayRenderingHeight = 256;

    @FXML
    private BorderPane root;
    @FXML
    private HBox title;
    @FXML
    private Pane display;

    private IntegerProperty tileWidth;
    private IntegerProperty tileHeight;

    private Environment environment;
    private TileMapsView tileMapsView;
    private TileMaps tileMaps;

    private int ticks = 0;


    public Controller(Stage primaryStage)
    {
        tileWidth = new SimpleIntegerProperty();
        tileHeight = new SimpleIntegerProperty();
        tileMaps = new TileMaps();

        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            environment.getKeysInput().put(key.getCode(), true);
            key.consume();
        });

        primaryStage.addEventFilter(KeyEvent.KEY_RELEASED, key -> {
            environment.getKeysInput().put(key.getCode(), false);
            key.consume();
        });

        // La taille des tiles apres le scalling
        primaryStage.heightProperty().addListener((obs, oldV, newV) -> tileHeight.setValue((int) (TileMaps.tileDefaultSize*((newV.intValue()-title.getPrefHeight()) / displayRenderingHeight))));
        primaryStage.widthProperty().addListener((obs, oldV, newV) -> tileWidth.setValue((int) (TileMaps.tileDefaultSize*((newV.intValue() / displayRenderingWidth)))));
    }

    public void initialize(URL location, ResourceBundle resources)
    {
        tileMaps.load("src/main/resources/fr/sae/terraria/maps/map_0.json");

        // La taille des tiles apres le scalling
        tileHeight.setValue((int) (TileMaps.tileDefaultSize*((root.getPrefHeight()-title.getPrefHeight()) / 256)));
        tileWidth.setValue((int) (TileMaps.tileDefaultSize*((root.getPrefWidth() / 465))));

        environment = new Environment(tileMaps.getWidth()*tileWidth.get(), tileMaps.getHeight()*tileHeight.get(),tileWidth.get(),tileHeight.get());

        tileMapsView = new TileMapsView(environment, display, tileWidth, tileHeight);
        tileMapsView.displayMaps(tileMaps);
        tileMapsView.displayPlayer();
    }
}