package fr.sae.terraria;

import fr.sae.terraria.controller.GameController;
import fr.sae.terraria.controller.MenuController;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;


/**
 * <a href="https://github.com/NaulaN/SAE-Terraria-Like/wiki/Document-utilisateur">Document utilisateur</a>
 * <a href="https://github.com/NaulaN/SAE-Terraria-Like">Repo Github</a>
 */
public class Terraria extends Application
{
    // Constantes
    public static final String SRC_PATH = "src/main/resources/fr/sae/terraria/";
    public static final double TARGET_FPS = .017;
    public static final int DISPLAY_RENDERING_WIDTH = 465;
    public static final int DISPLAY_RENDERING_HEIGHT = 256;

    private final String titleWindow = "Terraria-Like";
    private final boolean resizable = false;
    private final int widthWindow = 1280;
    private final int heightWindow = 720;


    public static void main(String[] args) { launch(); }

    private FXMLLoader loadFXML(String path)
    {
        URL pathGameFxml = Terraria.class.getResource(path);
        if (Objects.isNull(pathGameFxml)) try {
            pathGameFxml = new File(Terraria.SRC_PATH + path).toURI().toURL();
        } catch (MalformedURLException e) { throw new RuntimeException(e); }

        return new FXMLLoader(pathGameFxml);
    }

    public void start(Stage stage) throws IOException
    {
        GameController gameController = new GameController(stage);
        MenuController menuController = new MenuController();

        FXMLLoader fxmlLoader = this.loadFXML("vue/game.fxml");
        fxmlLoader.setController(gameController);
        Scene game = new Scene(fxmlLoader.load(), this.widthWindow, this.heightWindow);
        game.setCursor(Cursor.NONE);

        fxmlLoader = this.loadFXML("vue/menu.fxml");
        fxmlLoader.setController(menuController);
        Scene menu = new Scene(fxmlLoader.load(), this.widthWindow, this.heightWindow);

        stage.setTitle(this.titleWindow);
        stage.setResizable(this.resizable);

        BooleanProperty openMenu = new SimpleBooleanProperty();
        // Change de scènes suivant les valeurs boolean
        openMenu.addListener((obs, oldB, newB) -> stage.setScene(newB.equals(Boolean.TRUE) ? game : menu));

        stage.setScene(game);
        stage.sizeToScene();

        // Permet que le menu ne clignote pas et reste afficher même si le bouton 'M' est encore enfoncer.
        int[] timePressedKey = new int[] {1};
        stage.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode() == KeyCode.M && timePressedKey[0] <= 1)
                openMenu.set(!openMenu.get());

            timePressedKey[0]++;
            key.consume();
        });
        stage.addEventFilter(KeyEvent.KEY_RELEASED, key -> timePressedKey[0] = 1);

        // Synchronise les changements du joueur entre les contrôleurs.
        stage.sceneProperty().addListener(((obs, oldScene, newScene) -> {
            if (openMenu.get()) {
                if (!Objects.isNull(menuController.getPlayer())) {
                    gameController.transferPlayer(menuController.getPlayer());
                    gameController.getEnvironment().getLoop().play();
                }
            } else {
                if (!Objects.isNull(gameController.getPlayer())) {
                    menuController.transferPlayer(gameController.getPlayer());
                    menuController.transferEnvironment(gameController.getEnvironment());
                    gameController.getEnvironment().getLoop().stop();
                    menuController.background.setImage(game.snapshot(null));
                }
            }
        }));

        // Quand le joueur meurt, le jeu ce met à l'arrêt
        gameController.getEnvironment().getPlayer().pvProperty().addListener((obs, oldV, newV) -> {
            if (newV.intValue() == 0)
                stage.close();      // TODO Mettre un ecran de fin avec deux options
        });

        stage.widthProperty().addListener((obs, oldWidth, newWidth) -> gameController.scaleMultiplicatorWidth = (newWidth.intValue() / Terraria.DISPLAY_RENDERING_WIDTH));
        stage.heightProperty().addListener((obs, oldHeight, newHeight) -> gameController.scaleMultiplicatorHeight = ((newHeight.intValue()-gameController.title.getPrefHeight()) / Terraria.DISPLAY_RENDERING_HEIGHT));

        stage.show();
    }
}