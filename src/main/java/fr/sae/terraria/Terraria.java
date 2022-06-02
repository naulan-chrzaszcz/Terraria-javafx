package fr.sae.terraria;

import fr.sae.terraria.controller.GameController;
import fr.sae.terraria.controller.MenuController;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ParallelCamera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;


public class Terraria extends Application
{
    // Constants
    public static final String SRC_PATH = "src/main/resources/fr/sae/terraria/";
    public static final double TARGET_FPS = .017;
    public static final int DISPLAY_RENDERING_WIDTH = 465;
    public static final int DISPLAY_RENDERING_HEIGHT = 256;

    private final String titleWindow = "Terraria-Like";
    private final int widthWindow = 1280;
    private final int heightWindow = 720;


    public static void main(String[] args) { launch(); }

    private FXMLLoader loadFXML(String path)
    {
        URL pathGameFxml = Terraria.class.getResource(path);
        if (Objects.isNull(pathGameFxml)) try {
            pathGameFxml = new File(SRC_PATH + path).toURI().toURL();
        } catch (MalformedURLException e) { throw new RuntimeException(e); }

        return new FXMLLoader(pathGameFxml);
    }

    public void start(Stage stage) throws IOException
    {
        GameController gameController = new GameController(stage);
        MenuController menuController = new MenuController(stage);

        FXMLLoader fxmlLoader = this.loadFXML("vue/game.fxml");
        fxmlLoader.setController(gameController);
        Scene game = new Scene(fxmlLoader.load(), this.widthWindow, this.heightWindow);
        game.setCursor(Cursor.NONE);

        fxmlLoader = this.loadFXML("vue/menu.fxml");
        fxmlLoader.setController(menuController);
        Scene menu = new Scene(fxmlLoader.load(), this.widthWindow, this.heightWindow);

        stage.setTitle(this.titleWindow);
        stage.setResizable(false);

        BooleanProperty switchScene = new SimpleBooleanProperty();
        // Change de scènes suivant les valeurs boolean
        switchScene.addListener((obs, oldB, newB) -> stage.setScene(newB.equals(Boolean.TRUE) ? game : menu));

        stage.setScene(game);
        int[] timePressedKey = new int[1];  // Permet que le menu ne clignote pas et reste afficher même si le bouton 'M' est encore enfoncer.
        stage.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode() == KeyCode.M && timePressedKey[0] <= 1)
                switchScene.set(!switchScene.get());

            timePressedKey[0]++;
            key.consume();
        });
        stage.addEventFilter(KeyEvent.KEY_RELEASED, key -> timePressedKey[0] = 1);
        stage.sizeToScene();

        // Sync les changements du joueur entre les contrôleurs.
        stage.sceneProperty().addListener(((obs, oldScene, newScene) -> {
            if (switchScene.get()) {
                if (!Objects.isNull(menuController.player)) {
                    gameController.player = menuController.player;
                    menuController.loop.stop();
                    gameController.environment.getLoop().play();
                }
            } else {
                if (!Objects.isNull(gameController.player)) {
                    menuController.player = gameController.player;
                    menuController.loop.play();
                    gameController.environment.getLoop().stop();
                }
            }
        }));
        stage.widthProperty().addListener((obs, oldV, newV) -> {
            gameController.scaleMultiplicatorWidth = (newV.intValue() / Terraria.DISPLAY_RENDERING_WIDTH);
            menuController.scaleMultiplicatorWidth = (newV.intValue() / Terraria.DISPLAY_RENDERING_WIDTH);
        });
        stage.heightProperty().addListener((obs, oldV, newV) -> {
            gameController.scaleMultiplicatorHeight = ((newV.intValue()-gameController.title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT);
            menuController.scaleMultiplicatorHeight = ((newV.intValue()-gameController.title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT);
        });

        stage.show();
    }
}