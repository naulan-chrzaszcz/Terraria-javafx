package fr.sae.terraria.controller;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.entities.player.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class MenuController implements Initializable
{
    @FXML public Pane root;
    @FXML public Pane displayInventory;
    @FXML public Pane displayLexique;
    @FXML public HBox HBoxText;

    public Timeline loop;
    private Stage stage;
    public Player player = null;
    public double scaleMultiplicatorWidth;
    public double scaleMultiplicatorHeight;


    public MenuController(final Stage stage)
    {
        super();
        this.stage = stage;
    }

    @Override public void initialize(URL location, ResourceBundle resources)
    {
        this.scaleMultiplicatorWidth = (this.root.getPrefWidth() / Terraria.DISPLAY_RENDERING_WIDTH);
        this.scaleMultiplicatorHeight = ((this.root.getPrefHeight()-this.HBoxText.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT);

        this.loop = new Timeline();
        this.loop.setCycleCount(Animation.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(Terraria.TARGET_FPS), (ev -> {
            if (!Objects.isNull(this.player)) {
                System.out.println(this.player.getInventory());
            }
        }));

        this.loop.getKeyFrames().add(keyFrame);
        this.loop.play();
    }
}
