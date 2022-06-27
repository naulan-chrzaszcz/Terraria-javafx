package fr.sae.terraria.controller;

import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.player.Player;
import fr.sae.terraria.modele.entities.player.craft.Craft;
import fr.sae.terraria.modele.entities.tools.MaterialSet;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;


public class MenuController implements Initializable
{
    @FXML public BorderPane root;
    @FXML public SplitPane split;
    @FXML public BorderPane lexiques;
    @FXML public BorderPane inventory;
    @FXML public Pane recipesList;

    @FXML public HBox recipeRock;
    @FXML public HBox recipeTorch;

    @FXML public HBox recipeWoodPickaxe;
    @FXML public HBox recipeStonePickaxe;
    @FXML public HBox recipeIronPickaxe;

    @FXML public HBox recipeWoodAxe;
    @FXML public HBox recipeStoneAxe;
    @FXML public HBox recipeIronAxe;

    @FXML public HBox recipeWoodSword;
    @FXML public HBox recipeStoneSword;
    @FXML public HBox recipeIronSword;

    private Environment environment;
    private Player player;
    public ImageView background;


    public MenuController()
    {
        this.background = new ImageView();
        this.environment = null;
        this.player = null;
    }

    @Override public void initialize(URL location, ResourceBundle resources)
    {
        this.root.setBackground(Background.EMPTY);
        this.split.setBackground(Background.EMPTY);
        this.lexiques.setBackground(Background.EMPTY);
        this.inventory.setBackground(Background.EMPTY);
        this.recipesList.setBackground(Background.EMPTY);
        this.background.setEffect(new GaussianBlur(15));
        this.root.getChildren().add(0, this.background);

        // Craft de la roche à partir de 3 pierres
        this.recipeRock.addEventFilter(Event.ANY, event -> {
            if (event.getEventType().getName().equalsIgnoreCase("MOUSE_PRESSED"))
                this.player.pickup(Craft.rock(this.environment));
        });

        this.recipeTorch.addEventFilter(Event.ANY, event -> {
            if (event.getEventType().getName().equalsIgnoreCase("MOUSE_PRESSED"))
                this.player.pickup(Craft.torch(this.environment));
        });


        // LES PIOCHES
        this.recipeWoodPickaxe.addEventFilter(Event.ANY, event -> {
            if (event.getEventType().getName().equalsIgnoreCase("MOUSE_PRESSED"))
                this.player.pickup(Craft.pickaxe(this.environment, MaterialSet.WOOD));
        });

        this.recipeStonePickaxe.addEventFilter(Event.ANY, event -> {
            if (event.getEventType().getName().equalsIgnoreCase("MOUSE_PRESSED"))
                this.player.pickup(Craft.pickaxe(this.environment, MaterialSet.STONE));
        });

        this.recipeIronPickaxe.addEventFilter(Event.ANY, event -> {
            if (event.getEventType().getName().equalsIgnoreCase("MOUSE_PRESSED"))
                this.player.pickup(Craft.pickaxe(this.environment, MaterialSet.IRON));
        });


        // LES HACHES
        this.recipeWoodAxe.addEventFilter(Event.ANY, event -> {

        });

        this.recipeStoneAxe.addEventFilter(Event.ANY, event -> {

        });

        this.recipeIronAxe.addEventFilter(Event.ANY, event -> {

        });


        // LES EPEES
        this.recipeWoodSword.addEventFilter(Event.ANY, event -> {

        });

        this.recipeStoneSword.addEventFilter(Event.ANY, event -> {

        });

        this.recipeIronSword.addEventFilter(Event.ANY, event -> {

        });
    }


    public void transferEnvironment(Environment environmentOnOtherController) { this.environment = environmentOnOtherController; }
    public void transferPlayer(Player playerOnOtherController) { this.player = playerOnOtherController; }


    public Environment getEnvironment() { return this.environment; }
    public Player getPlayer() { return this.player; }
}
