package fr.sae.terraria;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Terraria extends javafx.application.Application
{
    // TODO:  effet de profondeur dans la terre
    private String titleWindow = "Terraria-Like";
    private int widthWindow = 1280;
    private int heightWindow = 720;


    @Override
    public void start(Stage stage) throws IOException
    {
        // TODO: Commencer par le menu
        FXMLLoader fxmlLoader = new FXMLLoader(Terraria.class.getResource("vue/game.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), this.widthWindow, this.heightWindow);

        stage.setTitle(this.titleWindow);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch(); }
}