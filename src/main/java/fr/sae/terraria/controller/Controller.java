package fr.sae.terraria.controller;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;

import java.io.File;
import java.net.URISyntaxException;
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
    public void initialize(URL location, ResourceBundle resources) {
        environment = new Environment();
        tiles = new TileMaps();

        // Scalling
        Scale s = new Scale();
        s.setX(2.76);
        s.setY(2.42);
        gamePane.getTransforms().add(s);

        tiles.load("src/main/resources/fr/sae/terraria/maps/map_0.json");

        Image dirt = new Image(Terraria.class.getResourceAsStream("tiles/dirt-left.png"));
        Image rock = new Image(Terraria.class.getResourceAsStream("tiles/rock-fill.png"));
        Image sky = new Image(Terraria.class.getResourceAsStream("tiles/floor-top.png"));

        for (int y = 0; y < tiles.getHeight() ; y++){
            for (int x = 0 ; x < tiles.getWidth() ; x++){
                int tile = tiles.getTile(y,x);
                ImageView tileView = new ImageView();
                tileView.setX(x*16);
                tileView.setY(y*16);

                switch (tile) {
                    case 1:
                        tileView.setImage(rock);
                        break;
                    case 2:
                        tileView.setImage(dirt);
                        break;
                }
                gamePane.getChildren().add(tileView);
            }
        }
        Circle a = new Circle(5);
        a.setTranslateX(environment.getPlayer().getX()*16);
        a.setTranslateY(environment.getPlayer().getY()*30);
        gamePane.getChildren().add(a);

        gameLoop();
    }

    private void gameLoop()
    {
        int tick = 0;

        Timeline loop = new Timeline();
        loop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.017), (ev -> {
            this.environment.getPlayer().updates();
        }));

        loop.getKeyFrames().add(keyFrame);
        loop.play();
    }
}