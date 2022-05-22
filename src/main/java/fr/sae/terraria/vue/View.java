package fr.sae.terraria.vue;

import javafx.beans.property.IntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.Entity;


public class View
{


    public View(Environment environment,
                Pane display,
                IntegerProperty tileWidth,
                IntegerProperty tileHeight,
                double scaleMultiplicatorWidth,
                double scaleMultiplicatorHeight)
    {
        TileMapsView tileMapsView = new TileMapsView(environment, display, tileWidth, tileHeight);
        tileMapsView.displayMaps(environment.getTileMaps());

        PlayerView playerView = new PlayerView(environment.getPlayer(), scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        playerView.displayPlayer(display);
        playerView.displayInventory(display);
        playerView.displayHealthBar(display);
    }

    public static Image loadAnImage(String path, int tileWidth, int tileHeight) { return new Image(View.foundImage(path).getUrl(), tileWidth, tileHeight, false, false, false); }

    public static Image loadAnImage(String path, double scaleMultiplicatorWidth, double scaleMultiplicatorHeight)
    {
        Image img = View.foundImage(path);
        double width = img.getWidth();
        double height = img.getHeight();
        img.cancel();

        double widthScaled = width*scaleMultiplicatorWidth;
        double heightScaled = height*scaleMultiplicatorHeight;
        return new Image(img.getUrl(), widthScaled, heightScaled, false, false, false);
    }

    private static Image foundImage(String path)
    {
        Image img = null;
        try {
            URL pathImg = Terraria.class.getResource(path).toURI().toURL();
            if (pathImg == null)
                pathImg = new File(Terraria.srcPath + path).toURI().toURL();
            img = new Image(pathImg.toString());
        } catch (Exception ignored) {}

        return img;
    }


    public static ImageView createImageView(Entity entity, Image img)
    {
        ImageView imageView = new ImageView(img);
        imageView.translateXProperty().bind(entity.getXProperty());
        imageView.translateYProperty().bind(entity.getYProperty());

        return imageView;
    }
}
