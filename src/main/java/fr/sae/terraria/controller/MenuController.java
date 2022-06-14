package fr.sae.terraria.controller;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.blocks.Block;
import fr.sae.terraria.modele.entities.blocks.BlockSet;
import fr.sae.terraria.modele.entities.items.Item;
import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.modele.entities.player.inventory.Stack;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.locks.ReentrantLock;


public class MenuController implements Initializable
{
    @FXML public Pane root;
    @FXML public Pane displayInventory;
    @FXML public Pane displayLexique;
    @FXML public HBox HBoxText;
    @FXML public HBox lexiconStone;
    @FXML public HBox lexiconPickaxe1;
    @FXML public HBox lexiconPickaxe2;
    @FXML public HBox lexiconPickaxe3;
    @FXML public HBox lexiconAxe1;
    @FXML public HBox lexiconAxe2;
    @FXML public HBox lexiconAxe3;
    @FXML public HBox lexiconSword1;
    @FXML public HBox lexiconSword2;
    @FXML public HBox lexiconSword3;



    public Timeline loop;
    private Stage stage;
    public Environment environment = null;
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
                // System.out.println(this.player.getInventory());
            }
        }));

        this.loop.getKeyFrames().add(keyFrame);
        this.loop.play();

        // Craft de la roche à partir de 3 pierres
        this.lexiconStone.addEventFilter(Event.ANY, ev -> {
            if (ev.getEventType().getName().equalsIgnoreCase("MOUSE_PRESSED")) {
                int i = this.player.getInventory().get().size()-1;
                if (player.getCraft().possibleToCreateTool(10)) {
                    player.getCraft().createTool(10);
                }
            }
        });
    }
}
