package fr.sae.terraria.controller;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.modele.entities.player.craft.Craft;
import fr.sae.terraria.modele.entities.tools.MaterialSet;
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
    @FXML public HBox recipeRock;
    @FXML public HBox recipeWoodPickaxe;
    @FXML public HBox recipeStonePickaxe;
    @FXML public HBox recipeIronPickaxe;

    public Environment environment = null;
    public Player player = null;


    @Override public void initialize(URL location, ResourceBundle resources)
    {
        // Craft de la roche à partir de 3 pierres
        this.recipeRock.addEventFilter(Event.ANY, ev -> {
            if (ev.getEventType().getName().equalsIgnoreCase("MOUSE_PRESSED"))
                this.player.pickup(Craft.rock(this.environment));
        });

        this.recipeWoodPickaxe.addEventFilter(Event.ANY, ev -> {
            if (ev.getEventType().getName().equalsIgnoreCase("MOUSE_PRESSED"))
                this.player.pickup(Craft.pickaxe(this.environment, MaterialSet.WOOD));
        });

        this.recipeStonePickaxe.addEventFilter(Event.ANY, ev -> {
            if (ev.getEventType().getName().equalsIgnoreCase("MOUSE_PRESSED"))
                this.player.pickup(Craft.pickaxe(this.environment, MaterialSet.STONE));
        });

        this.recipeIronPickaxe.addEventFilter(Event.ANY, ev -> {
            if (ev.getEventType().getName().equalsIgnoreCase("MOUSE_PRESSED"))
                this.player.pickup(Craft.pickaxe(this.environment, MaterialSet.IRON));
        });
    }
}
