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
    // TODO:  effet de profondeur dans la terre
    private String titleWindow = "Terraria-Like";
    private int widthWindow = 1280;
    private int heightWindow = 720;


    public static void main(String[] args) { launch(); }

    public void start(Stage stage) throws IOException
    {
        /* TODO: Commencer par le menu */
        URL url = Terraria.class.getResource("res*");
        
        File f = new File("src/main/resources/fr/sae/terraria/vue/game.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(f.toURI().toURL());
        Controller ctrl = new Controller(stage);
        fxmlLoader.setController(ctrl);
        Scene scene = new Scene(fxmlLoader.load(), this.widthWindow, this.heightWindow);

        stage.setTitle(this.titleWindow);
        stage.setResizable(false);
        stage.setScene(scene);

        stage.show();
    }
}