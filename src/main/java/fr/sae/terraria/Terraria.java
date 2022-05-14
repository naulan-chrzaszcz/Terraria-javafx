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
    public static final String srcPath = "src/main/resources/fr/sae/terraria/";

    // TODO:  effet de profondeur dans la terre
    private final String titleWindow = "Terraria-Like";
    private final int widthWindow = 1280;
    private final int heightWindow = 720;


    public static void main(String[] args) { launch(); }

    public void start(Stage stage) throws IOException
    {
        /* TODO: Commencer par le menu */

        URL pathFxml = Terraria.class.getResource("vue/game.fxml");
        if (pathFxml == null)
            pathFxml = new File(this.srcPath + "vue/game.fxml").toURI().toURL();
        
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