package fr.sae.terraria.controller;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable
{
    private TileMaps tiles;
    @FXML
    TilePane gamePane;
    @FXML
    HBox title;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tiles = new TileMaps();
        tiles.load("src/main/resources/fr/sae/terraria/maps/map_0.json");

        Image dirt = new Image(Terraria.class.getResourceAsStream("tiles/dirt-left.png"));
        Image rock = new Image(Terraria.class.getResourceAsStream("tiles/rock-fill.png"));
        Image sky = new Image(Terraria.class.getResourceAsStream("tiles/floor-top.png"));


        System.out.println(tiles.getHeight());
        System.out.println(tiles.getWidth());
        for (int y = 0; y < tiles.getHeight() ; y++){
            for (int x = 0 ; x < tiles.getWidth() ; x++){
                int tile = tiles.getTile(y,x);
                System.out.println(tile);

                switch (tile){
                    case 0:
                        gamePane.getChildren().add(new ImageView(sky));
                        break;
                    case 1:
                        gamePane.getChildren().add(new ImageView(rock));
                        break;
                    case 2:
                        gamePane.getChildren().add(new ImageView(dirt));
                        break;
                }
            }
        }
    }
}