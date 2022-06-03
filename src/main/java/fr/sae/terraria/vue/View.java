package fr.sae.terraria.vue;

import fr.sae.terraria.Terraria;
import fr.sae.terraria.modele.Environment;
import fr.sae.terraria.modele.entities.entity.Entity;
import fr.sae.terraria.vue.hud.HUDView;
import fr.sae.terraria.vue.hud.MouseCursorView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;
import java.net.URL;


public class View
{


    /**
     * Cette classe lors de l'initialisation, crée et génére toutes les views du jeux
     *  Contient des fonctions essentiels au chargement des images et des creations de vue
     *
     * @param environment -
     * @param displayTiledMap Le pane qui se charge d'afficher la carte
     * @param displayHUD Le pane qui se charge d'afficher les elements du HUD
     * @param scaleMultiplicatorWidth Le multiplicateur en largeur qui permet de redimensionner les images
     * @param scaleMultiplicatorHeight Le multiplicateur en hauteur qui permet de redimensionner les images
     */
    public View(Environment environment,
                Pane displayHostileBeings,
                Pane displayTiledMap,
                Pane displayHUD,
                Pane displayCursorMouse,
                Pane filter,
                double scaleMultiplicatorWidth,
                double scaleMultiplicatorHeight)
    {
        TileMapsView tileMapsView = new TileMapsView(environment, displayTiledMap, displayHostileBeings, scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        tileMapsView.displayMaps(environment.getTileMaps());

        PlayerView playerView = new PlayerView(environment.getPlayer(), scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        playerView.displayPlayer(displayHostileBeings);

        LightView lightView = new LightView(environment.getGameClock(),filter,scaleMultiplicatorHeight,scaleMultiplicatorWidth, environment.getTileMaps());
        lightView.setLightElements();

        HUDView HUDView = new HUDView(environment.getPlayer(), environment.getGameClock(), displayHUD, scaleMultiplicatorWidth, scaleMultiplicatorHeight);
        HUDView.display();

        new MouseCursorView(displayHUD, displayCursorMouse, scaleMultiplicatorWidth, scaleMultiplicatorHeight);
    }

    /** Essaye de trouver et de charger l'image sinon renvoie null */
    private static Image foundImage(String path)
    {
        Image img = null;
        try {
            URL pathImg = Terraria.class.getResource(path).toURI().toURL();
            if (pathImg == null)
                pathImg = new File(Terraria.SRC_PATH + path).toURI().toURL();
            img = new Image(pathImg.toString());
        } catch (Exception ignored) {}

        return img;
    }

    /** Charge une image avec une resolution carré (Ex: 16x16) */
    public static Image loadAnImage(String path, int tileWidth, int tileHeight) { return new Image(View.foundImage(path).getUrl(), tileWidth, tileHeight, false, false, false); }

    /** Charge une image avec une resolution non carrée */
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

    public static ImageView createImageView(Entity entity, Image img)
    {
        ImageView imageView = new ImageView(img);
        imageView.translateXProperty().bind(entity.getXProperty());
        imageView.translateYProperty().bind(entity.getYProperty());

        return imageView;
    }
}
