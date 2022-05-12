package fr.sae.terraria.controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import java.net.URL;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;

import static javafx.scene.input.KeyCode.*;


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
    private Map<KeyCode, Boolean> keysInput = new HashMap<>();
    private TileMaps tiles;

    private int ticks = 0;


    public Controller(Stage primaryStage)
    {
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            keysInput.put(key.getCode(), true);
            key.consume();
        });

        primaryStage.addEventFilter(KeyEvent.KEY_RELEASED, key -> {
            keysInput.put(key.getCode(), false);
            key.consume();
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        environment = new Environment();
        tiles = new TileMaps();

        tiles.load("src/main/resources/fr/sae/terraria/maps/map_0.json");

        tileHeight.setValue((int) (TileMaps.tileDefaultSize*((root.getPrefHeight()-title.getPrefHeight()) / 256)));
        tileWidth.setValue((int) (TileMaps.tileDefaultSize*((root.getPrefWidth() / 465))));

        InputStream dirtPath = Terraria.class.getResourceAsStream("tiles/dirt-left.png");
        if (dirtPath == null) try {
            dirtPath = new FileInputStream("src/main/resources/fr/sae/terraria/tiles/dirt-left.png");
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }
        Image dirt = new Image(dirtPath, tileWidth.get(), tileHeight.get(), false, false);

        InputStream rockPath = Terraria.class.getResourceAsStream("tiles/rock-fill.png");
        if (rockPath == null) try {
            rockPath = new FileInputStream("src/main/resources/fr/sae/terraria/tiles/rock-fill.png");
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }
        Image rock = new Image(rockPath, tileWidth.get(), tileHeight.get(), false, false);

        for (int y = 0; y < tiles.getHeight() ; y++)
            for (int x = 0 ; x < tiles.getWidth() ; x++) {
                ImageView tileView = new ImageView();
                tileView.setX(x*tileWidth.get());
                tileView.setY(y*tileHeight.get());

                switch (tiles.getTile(y,x)) {
                    case 1:
                        tileView.setImage(rock);
                        break;
                    case 2:
                        tileView.setImage(dirt);
                        break;
                }

                display.getChildren().add(tileView);
            }

        InputStream playerPath = Terraria.class.getResourceAsStream("tiles/rock-bottom.png");
        if (playerPath == null) try {
            playerPath = new FileInputStream("src/main/resources/fr/sae/terraria/tiles/rock-bottom.png");
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }
        environment.getPlayer().setImage(new Image(playerPath, tileWidth.get(), tileHeight.get(), false, false));
        display.getChildren().add(environment.getPlayer().getImageView());

        gameLoop();
    }

    private void gameLoop()
    {
        Timeline loop = new Timeline();
        loop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.017), (ev -> {
            eventInput();
            this.environment.getPlayer().updates();

            ticks++;
        }));

        loop.getKeyFrames().add(keyFrame);
        loop.play();
    }

    public void eventInput()
    {
        int countKeys[] = new int[1];
        keysInput.forEach((key, value) -> {
            if (key == D && value)
                environment.getPlayer().moveRight();
            if (key == Q && value)
                environment.getPlayer().moveLeft();

            if (!value)
                countKeys[0]++;
            if (countKeys[0] == keysInput.size())
                environment.getPlayer().idle();
        });
    }
}