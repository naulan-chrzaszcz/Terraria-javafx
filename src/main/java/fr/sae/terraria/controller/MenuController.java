package fr.sae.terraria.controller;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.modele.entities.blocks.Dirt;
import fr.sae.terraria.modele.entities.blocks.Stone;
import fr.sae.terraria.modele.entities.blocks.TallGrass;
import fr.sae.terraria.modele.entities.blocks.Torch;
import fr.sae.terraria.modele.entities.entity.StowableObjectType;
import fr.sae.terraria.vue.View;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class MenuController implements Initializable
{
    @FXML
    private Pane displayInventory;
    @FXML
    private AnchorPane displayLexique;
    @FXML
    private Pane root;
    @FXML
    private HBox HBoxText;

    public Timeline loop;
    private Stage stage;
    public Player player = null;
    public double scaleMultiplicatorWidth;
    public double scaleMultiplicatorHeight;


    public MenuController(Stage stage) {
        this.stage = stage;
    }

    public void initialize(URL location, ResourceBundle resources)
    {
        scaleMultiplicatorWidth = (root.getPrefWidth() / Terraria.DISPLAY_RENDERING_WIDTH);
        scaleMultiplicatorHeight = ((root.getPrefHeight()-HBoxText.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT);


        this.loop = new Timeline();
        this.loop.setCycleCount(Animation.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(Terraria.TARGET_FPS), (ev -> {
            if (!Objects.isNull(player)) {
                System.out.println(player.getInventory());
            }
        }));

        this.loop.getKeyFrames().add(keyFrame);
        this.loop.play();
    }
}
