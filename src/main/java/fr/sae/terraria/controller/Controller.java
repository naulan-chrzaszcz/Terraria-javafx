package fr.sae.terraria.controller;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable
{
    @FXML
    private Pane gamePane;
    @FXML
    private HBox title;
    @FXML
    private Pane screen;
    @FXML
    private BorderPane root;

    private TileMaps tiles;
    private Environment environment;

    private int ticks = 0;


    public Controller(Stage primaryStage)
    {
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            // TODO: Pas propre tous ca zebi
            if (key.getCode() == KeyCode.D)
                environment.getPlayer().moveRight();
            key.consume();
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        environment = new Environment();
        tiles = new TileMaps();

        // Scalling
        Scale s = new Scale();
        s.setX(2.76);
        s.setY(2.42);
        // gamePane.getTransforms().add(s);

        tiles.load("src/main/resources/fr/sae/terraria/maps/map_0.json");

        // TODO pour le scale.
        int tileDefaultSize = 16;
        double tileWidth =  tileDefaultSize;

        InputStream dirtPath = Terraria.class.getResourceAsStream("tiles/dirt-left.png");
        if (dirtPath == null) try {
            dirtPath = new FileInputStream("src/main/resources/fr/sae/terraria/tiles/dirt-left.png");
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }
        Image dirt = new Image(dirtPath,16,16,false,false);

        InputStream rockPath = Terraria.class.getResourceAsStream("tiles/rock-fill.png");
        if (rockPath == null) try {
            rockPath = new FileInputStream("src/main/resources/fr/sae/terraria/tiles/rock-fill.png");
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }
        Image rock = new Image(rockPath,16,16,false,false);

        for (int y = 0; y < tiles.getHeight() ; y++){
            for (int x = 0 ; x < tiles.getWidth() ; x++){
                ImageView tileView = new ImageView();
                tileView.setX(x*16);
                tileView.setY(x*16);

                switch (tiles.getTile(y,x)) {
                    case 1:
                        tileView.setImage(rock);
                        break;
                    case 2:
                        tileView.setImage(dirt);
                        break;
                }

                screen.getChildren().add(tileView);
            }
        }
        Circle a = new Circle(5);

        a.translateXProperty().bind(environment.getPlayer().getXProperty());
        a.translateYProperty().bind(environment.getPlayer().getYProperty());

        screen.getChildren().add(a);

        gameLoop();
    }

    private void gameLoop()
    {
        Timeline loop = new Timeline();
        loop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.017), (ev -> {
            this.environment.getPlayer().updates();

            ticks++;
        }));

        loop.getKeyFrames().add(keyFrame);
        loop.play();
    }
}