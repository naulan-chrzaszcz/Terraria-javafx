package fr.sae.terraria.controller;

import fr.sae.terraria.modele.blocks.Dirt;
import fr.sae.terraria.modele.blocks.Stone;
import fr.sae.terraria.modele.entities.entity.Entity;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.util.ResourceBundle;

import java.net.URL;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;


public class Controller implements Initializable
{
    @FXML
    private BorderPane root;
    @FXML
    private HBox title;
    @FXML
    private Pane display;

    private IntegerProperty tileWidth = new SimpleIntegerProperty();
    private IntegerProperty tileHeight = new SimpleIntegerProperty();

    private Environment environment;
    private TileMaps tiles;

    private int ticks = 0;


    public Controller(Stage primaryStage)
    {

        tiles = new TileMaps();

        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            environment.getKeysInput().put(key.getCode(), true);
            key.consume();
        });

        primaryStage.addEventFilter(KeyEvent.KEY_RELEASED, key -> {
            environment.getKeysInput().put(key.getCode(), false);
            key.consume();
        });

        primaryStage.heightProperty().addListener((obs, oldV, newV) -> tileHeight.setValue((int) (TileMaps.tileDefaultSize*((newV.intValue()-title.getPrefHeight()) / 256))));
        primaryStage.widthProperty().addListener((obs, oldV, newV) -> tileWidth.setValue((int) (TileMaps.tileDefaultSize*((newV.intValue() / 465)))));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        tiles.load("src/main/resources/fr/sae/terraria/maps/map_0.json");

        tileHeight.setValue((int) (TileMaps.tileDefaultSize*((root.getPrefHeight()-title.getPrefHeight()) / 256)));
        tileWidth.setValue((int) (TileMaps.tileDefaultSize*((root.getPrefWidth() / 465))));
        environment = new Environment(tiles.getWidth()*tileWidth.get(),tiles.getHeight()*tileHeight.get(),tileWidth.get(),tileHeight.get());

        InputStream floorTopPath = Terraria.class.getResourceAsStream("tiles/floor-top.png");
        if (floorTopPath == null) try {
            floorTopPath = new FileInputStream("src/main/resources/fr/sae/terraria/tiles/floor-top.png");
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }
        Image floorTopImg = new Image(floorTopPath, tileWidth.get(), tileHeight.get(), false, false);

        InputStream floorLeftPath = Terraria.class.getResourceAsStream("tiles/floor-left.png");
        if (floorLeftPath == null) try {
            floorLeftPath = new FileInputStream("src/main/resources/fr/sae/terraria/tiles/floor-left.png");
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }
        Image floorLeftImg = new Image(floorLeftPath, tileWidth.get(), tileHeight.get(), false, false);

        InputStream floorRightPath = Terraria.class.getResourceAsStream("tiles/floor-right.png");
        if (floorRightPath == null) try {
            floorRightPath = new FileInputStream("src/main/resources/fr/sae/terraria/tiles/floor-right.png");
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }
        Image floorRightImg = new Image(floorRightPath, tileWidth.get(), tileHeight.get(), false, false);

        InputStream stonePath = Terraria.class.getResourceAsStream("tiles/rock-fill.png");
        if (stonePath == null) try {
            stonePath = new FileInputStream("src/main/resources/fr/sae/terraria/tiles/rock-fill.png");
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }
        Image stoneImg = new Image(stonePath, tileWidth.get(), tileHeight.get(), false, false);

        InputStream dirtPath = Terraria.class.getResourceAsStream("tiles/dirt-top.png");
        if (dirtPath == null) try {
            dirtPath = new FileInputStream("src/main/resources/fr/sae/terraria/tiles/dirt-top.png");
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }
        Image dirtImg = new Image(dirtPath, tileWidth.get(), tileHeight.get(), false, false);

        InputStream playerPath = Terraria.class.getResourceAsStream("sprites/player/player_idle.png");
        if (playerPath == null) try {
            playerPath = new FileInputStream("src/main/resources/fr/sae/terraria/sprites/player/player_idle.png");
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }

        for (int y = 0; y < tiles.getHeight() ; y++)
            for (int x = 0 ; x < tiles.getWidth() ; x++)
                switch (tiles.getTile(y,x))
                {
                    case TileMaps.STONE:
                        Stone stoneSprite = new Stone(x*tileWidth.get(), y*tileHeight.get());
                        stoneSprite.setRect((int) stoneImg.getWidth(), (int) stoneImg.getHeight());
                        display.getChildren().add(createImageView(stoneSprite, stoneImg));
                        environment.getEntities().add(stoneSprite);
                        break;
                    case TileMaps.DIRT:
                        Dirt dirtSprite = new Dirt(x*tileWidth.get(), y*tileHeight.get());
                        dirtSprite.setRect((int) dirtImg.getWidth(), (int) dirtImg.getHeight());
                        display.getChildren().add(createImageView(dirtSprite, dirtImg));
                        environment.getEntities().add(dirtSprite);
                        break;
                    case TileMaps.FLOOR_TOP:
                        Dirt floorTopSprite = new Dirt(x*tileWidth.get(), y*tileHeight.get());
                        floorTopSprite.setRect((int) floorTopImg.getWidth(), (int) floorTopImg.getHeight());
                        display.getChildren().add(createImageView(floorTopSprite, floorTopImg));
                        environment.getEntities().add(floorTopSprite);
                        break;
                    case TileMaps.FLOOR_LEFT:
                        Dirt floorLeftSprite = new Dirt(x*tileWidth.get(), y*tileHeight.get());
                        floorLeftSprite.setRect((int) floorLeftImg.getWidth(), (int) floorLeftImg.getHeight());
                        display.getChildren().add(createImageView(floorLeftSprite, floorLeftImg));
                        environment.getEntities().add(floorLeftSprite);
                        break;
                    case TileMaps.FLOOR_RIGHT:
                        Dirt floorRightSprite = new Dirt(x*tileWidth.get(), y*tileHeight.get());
                        floorRightSprite.setRect((int) floorRightImg.getWidth(), (int) floorRightImg.getHeight());
                        display.getChildren().add(createImageView(floorRightSprite, floorRightImg));
                        environment.getEntities().add(floorRightSprite);
                        break;
                }

        ImageView playerImgView = new ImageView(new Image(playerPath, tileWidth.get(), tileHeight.get()*2, false, false));
        environment.getPlayer().setRect((int) playerImgView.getFitWidth(), (int) playerImgView.getFitHeight());
        playerImgView.translateYProperty().bind(environment.getPlayer().getYProperty());
        playerImgView.translateXProperty().bind(environment.getPlayer().getXProperty());
        display.getChildren().add(playerImgView);
    }

    private ImageView createImageView(Entity entity, Image img)
    {
        ImageView imageView = new ImageView(img);
        imageView.translateXProperty().bind(entity.getXProperty());
        imageView.translateYProperty().bind(entity.getYProperty());

        return imageView;
    }
}