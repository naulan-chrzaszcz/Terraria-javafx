package fr.sae.terraria.vue;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.TileMaps;
import fr.sae.terraria.modele.entities.entity.Entity;
import javafx.beans.property.IntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class View
{


    public View(TileMaps tileMaps, Environment environment, Pane display, IntegerProperty tileWidth, IntegerProperty tileHeight)
    {
        TileMapsView tileMapsView = new TileMapsView(environment, display, tileWidth, tileHeight);
        tileMapsView.displayMaps(tileMaps);

        PlayerView playerView = new PlayerView(environment.getPlayer(), tileWidth.get(), tileHeight.get());
        playerView.displayPlayer(display);
        playerView.displayHealthBar();
    }

    public static Image loadAnImage(String path, int tileWidth, int tileHeight)
    {
        InputStream pathImg = Terraria.class.getResourceAsStream(path);
        if (pathImg == null) try {
            pathImg = new FileInputStream(Terraria.srcPath + path);
        } catch (FileNotFoundException e) { throw new RuntimeException(e); }

        return new Image(pathImg, tileWidth, tileHeight, false, false);
    }

    public static ImageView createImageView(Entity entity, Image img)
    {
        ImageView imageView = new ImageView(img);
        imageView.translateXProperty().bind(entity.getXProperty());
        imageView.translateYProperty().bind(entity.getYProperty());

        return imageView;
    }
}
