package fr.sae.terraria;

import fr.sae.terraria.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;


public class Terraria extends Application
{
    // Constants
    public static final String srcPath = "src/main/resources/fr/sae/terraria/";
    public static final double TARGET_FPS = .017;
    public static final int DISPLAY_RENDERING_WIDTH = 465;
    public static final int DISPLAY_RENDERING_HEIGHT = 256;

    private final String titleWindow = "Terraria-Like";
    private final int widthWindow = 1280;
    private final int heightWindow = 720;


    public static void main(String[] args) { launch(); }

    public void start(Stage stage) throws IOException
    {
        URL pathFxml = Terraria.class.getResource("vue/game.fxml");
        if (pathFxml == null)
            pathFxml = new File(srcPath + "vue/game.fxml").toURI().toURL();
        
        FXMLLoader fxmlLoader = new FXMLLoader(pathFxml);
        Controller ctrl = new Controller(stage);
        fxmlLoader.setController(ctrl);
        Scene scene = new Scene(fxmlLoader.load(), this.widthWindow, this.heightWindow);
        
        stage.setTitle(this.titleWindow);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.sizeToScene();

        stage.show();
    }
}