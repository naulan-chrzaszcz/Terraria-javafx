package fr.sae.terraria.controller;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.blocks.Rock;
import fr.sae.terraria.modele.entities.items.Stone;
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


public class MenuController implements Initializable
{
    @FXML public Pane root;
    @FXML public Pane displayInventory;
    @FXML public Pane displayLexique;
    @FXML public HBox HBoxText;
    @FXML public HBox lexique4;

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
        this.lexique4.addEventFilter(Event.ANY, ev -> {
            if (ev.getEventType().getName().equalsIgnoreCase("MOUSE_PRESSED")) {
                int i = this.player.getInventory().get().size()-1;
                while (i > 0 && !(this.player.getInventory().get().get(i).getItem() instanceof Stone) && this.player.getInventory().get().get(i).getNbItems() >= Rock.STONE_LOOTS)
                    i--;

                Stack stack = this.player.getInventory().get().get(i);
                if (stack.getItem() instanceof Stone) {
                    for (int j = 0; j < Rock.STONE_LOOTS; j++)
                        stack.remove();
                    this.player.pickup(new Rock(this.environment));
                }
            }
        });
    }
}
