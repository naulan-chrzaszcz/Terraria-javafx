package fr.sae.terraria;

import fr.sae.terraria.controller.GameController;
import fr.sae.terraria.controller.MenuController;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    public void start(Stage stage) throws IOException
    {
        URL pathGameFxml = Terraria.class.getResource("vue/game.fxml");
        if (Objects.isNull(pathGameFxml))
            pathGameFxml = new File(SRC_PATH + "vue/game.fxml").toURI().toURL();
        
        FXMLLoader fxmlLoader = new FXMLLoader(pathGameFxml);
        GameController gameController = new GameController(stage);
        fxmlLoader.setController(gameController);
        Scene game = new Scene(fxmlLoader.load(), this.widthWindow, this.heightWindow);

        URL pathMenuFxml = Terraria.class.getResource("vue/menu.fxml");
        if (Objects.isNull(pathMenuFxml))
            pathMenuFxml = new File(SRC_PATH + "vue/menu.fxml").toURI().toURL();
        fxmlLoader = new FXMLLoader(pathMenuFxml);
        MenuController menuController = new MenuController(stage);
        fxmlLoader.setController(menuController);
        Scene menu = new Scene(fxmlLoader.load(), this.widthWindow, this.heightWindow);

        stage.setTitle(this.titleWindow);
        stage.setResizable(false);

        BooleanProperty switchScene = new SimpleBooleanProperty();
        switchScene.addListener((obs, oldB, newB) -> stage.setScene(newB.equals(Boolean.TRUE) ? game : menu));

        stage.setScene(game);
        int[] timePressedKey = new int[1];
        stage.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode() == KeyCode.M && timePressedKey[0] <= 1)
                switchScene.set(!switchScene.get());
            key.consume();

            timePressedKey[0]++;
        });
        stage.addEventFilter(KeyEvent.KEY_RELEASED, key -> timePressedKey[0] = 1);
        stage.sizeToScene();

        stage.show();
    }
}